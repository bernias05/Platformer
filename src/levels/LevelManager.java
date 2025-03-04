package levels;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import gameStates.GameState;
import gameStates.Playing;
import helpz.LoadSave;
import main.Game;

import static main.Game.TILE_SIZE;

public class LevelManager {

	private Playing playing;
	private LevelInfo levelInfo;
	private BufferedImage waterImg;
	private BufferedImage[] lvlSprites;
	private BufferedImage[] waterSurfaceSprites;
	private ArrayList<Level> lvls = new ArrayList<Level>();
	
	private int lvlIndex = 0;
	private int waterAniTick, waterAniIndex;
	
	public LevelManager(Playing playing) {
		this.playing = playing;
		importSprites();
		loadLevels();
		levelInfo = new LevelInfo(this);
	}
	
	public void loadLevels() {
		BufferedImage[] lvlImgs = LoadSave.GetAllLevels();
		
		for (BufferedImage img : lvlImgs) {
			lvls.add(new Level(img));
		}
	}
	
	private void importSprites() {
		waterImg = LoadSave.GetSpriteAtlas(LoadSave.WATER_IMG);
		waterSurfaceSprites = new BufferedImage[4];
		lvlSprites = new BufferedImage[48];
		
		BufferedImage levelAtlas = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_SPRITES);
		BufferedImage waterAtlas = LoadSave.GetSpriteAtlas(LoadSave.WATER_SPRITES);
		
		for (int i = 0; i < 4; i++) {
			waterSurfaceSprites[i] = waterAtlas.getSubimage(i * 32, 0, 32, 32);
			
			for (int j = 0; j < 12; j++) {
				int index = i * 12 + j;
				lvlSprites[index] = levelAtlas.getSubimage(j * 32, i * 32, 32, 32);
			}
		}
	}

	public void update() {		
		waterAniTick++;
		if (waterAniTick >=  Game.ANI_SPEED) {
			waterAniTick = 0;
			waterAniIndex++;
			if (waterAniIndex > 3)
				waterAniIndex = 0;
		}
	}
	
	public void draw(Graphics g, int xLvlOffset, int yLvlOffset) {
		for (int i = 0; i < lvls.get(lvlIndex).getLvlData().length; i++) {
			for (int j = 0; j < lvls.get(lvlIndex).getLvlData()[0].length; j++) {
				int value = lvls.get(lvlIndex).getTileIndexValue(i, j);
				
				if (value <= 47)
					g.drawImage(lvlSprites[value], i * TILE_SIZE - xLvlOffset, j * TILE_SIZE - yLvlOffset, TILE_SIZE, TILE_SIZE, null);
			}
		}
	}
	
	public void drawWater(Graphics g, int xLvlOffset, int yLvlOffset) {
		for (int i = 0; i < lvls.get(lvlIndex).getLvlData().length; i++) {
			for (int j = 0; j < lvls.get(lvlIndex).getLvlData()[0].length; j++) {
				int value = lvls.get(lvlIndex).getTileIndexValue(i, j);
				
				if (value == 48)
					g.drawImage(waterImg, i * TILE_SIZE - xLvlOffset, j * TILE_SIZE - yLvlOffset, TILE_SIZE, TILE_SIZE, null);
				else if (value == 49)
					g.drawImage(waterSurfaceSprites[waterAniIndex], i * TILE_SIZE - xLvlOffset, j * TILE_SIZE - yLvlOffset, TILE_SIZE, TILE_SIZE, null);
			}
		}
	}
	
	public void drawLevelInfo(Graphics g) {
		levelInfo.draw(g);
	}
	
	public Playing getPlaying() {
		return playing;
	}
	
	public LevelInfo getLevelInfo() {
		return levelInfo;
	}
	
	public BufferedImage getLevelTile() {
		return lvlSprites[39];
	}
	
	public BufferedImage getWaterTile() {
		return waterImg;
	}
	
	public ArrayList<Level> getLevels() {
		return lvls;
	}
	
	public Level getCurrentLevel() {
		return lvls.get(lvlIndex);
	}
	
	public int getLvlIndex() {
		return lvlIndex;
	}
	
	public void setLevel(int lvlIndex) {
		this.lvlIndex = lvlIndex;
	}
	
}
