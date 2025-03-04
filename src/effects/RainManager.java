package effects;

import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import gameStates.Playing;
import helpz.LoadSave;
import main.Game;

public class RainManager {

	private Random random;
	private Playing playing;
	private BufferedImage rainImg;
	private Point2D.Float[] raindrops;
	
	private float fallSpeed = 3f * Game.SCALE;
	
	private final int rW = (int) (1.5 * Game.SCALE);
	private final int rH = (int) (6.5 * Game.SCALE);
	
	private boolean stopRain = false;
	private boolean sunny = true;
	private boolean raining;
	
	private int weatherTick;
	private int weatherSeconds;
	private int sunDuration;
	private int rainDuration;
	
	public RainManager(Playing playing) {
		this.playing = playing;
		
		random = new Random();
		rainImg = LoadSave.GetSpriteAtlas(LoadSave.RAIN_DROP);
		createRain();
	}
	
	public void createRain() {
		sunDuration = getNewSunDuration();
		rainDuration = getNewRainDuration();
		raindrops = new Point2D.Float[5000];
		for (int i = 0; i < raindrops.length; i++)
			raindrops[i] = new Point2D.Float(newX(), newY());
	}
	
	private int newX() {
		return random.nextInt(playing.getLevelManager().getCurrentLevel().getLvlWidth());
	}
	
	private int newY() {
		int lvlHeight = playing.getLevelManager().getCurrentLevel().getLvlHeight();
		return random.nextInt(lvlHeight * 2) - lvlHeight * 2 - rH;
	}
	
	public void update() {
		updateWeatherTick();
		if (stopRain)
			stopRain();
		else if (!sunny) {
			for (Point2D.Float p : raindrops) {
				p.y += fallSpeed;
				if (p.y > playing.getLevelManager().getCurrentLevel().getLvlHeight()) {
					p.x = newX();
					p.y = newY();
				}
			}
		}
	}
	
	private void stopRain() {
		raining = false;
		for (Point2D.Float p : raindrops) {
			if (p.y > -20 * Game.SCALE) {
				p.y += fallSpeed;
				raining = true;
			}
			if (p.y >= playing.getLevelManager().getCurrentLevel().getLvlHeight())
				p.y = -20 * Game.SCALE;
		}
		
		if (raining == false)
			stopRain = false;
	}
	
	private void updateWeatherTick() {
		weatherTick++;
		if (weatherTick >= 120) {
			weatherTick = 0;
			weatherSeconds++;
			if (sunny) {
				if (weatherSeconds > sunDuration) {
					rainDuration = getNewRainDuration();
					weatherSeconds = 0;
					sunny = false;
					createRain();
				}
			} else {
				if (weatherSeconds > rainDuration) {
					sunDuration = getNewSunDuration();
					weatherSeconds = 0;
					sunny = true;
					stopRain = true;
				}
			}
		}
	}
	
	private int getNewSunDuration() {
		return random.nextInt(30) + 20;
	}
	
	private int getNewRainDuration() {
		return random.nextInt(15) + 20;
	}
	
	public void draw(Graphics g, int xLvlOffset, int yLvlOffset) {
		for (Point2D.Float p : raindrops)
			g.drawImage(rainImg, (int) p.x - xLvlOffset, (int) p.y - yLvlOffset, rW, rH, null);
	}
	
	public void reset() {
		weatherTick = 0;
		weatherSeconds = 0;
		sunny = true;
		raining = false;
		stopRain = false;
		createRain();
	}
	
}
