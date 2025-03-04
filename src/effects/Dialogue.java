package effects;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import main.Game;

import static helpz.Constants.Object.Size.*;

public class Dialogue {

	private BufferedImage[] imgs;
	private int x, y;
	private int aniTick = 0, aniIndex = 0;
	
	private boolean active = true;
	
	public Dialogue(BufferedImage[] imgs, int x, int y) {
		this.imgs = imgs;
		this.x = x;
		this.y = y;
	}
	
	public void reuse(BufferedImage[] imgs, int x, int y) {
		this.imgs = imgs;
		this.x = x;
		this.y = y;
		active = true;
	}
	
	public void update() {
		aniTick++;
		if (aniTick >= Game.ANI_SPEED * 1.5) {
			aniTick = 0;
			aniIndex++;
			
			if (aniIndex >= 5) {
				active = false;
				aniIndex = 0;
			}
		}
	}
	
	public void draw(Graphics g, int xLvlOffset, int yLvlOffset) {
		g.drawImage(imgs[aniIndex], x - xLvlOffset, y - yLvlOffset, PUNCTUATION_MARK_WIDTH, PUNCTUATION_MARK_HEIGHT, null);
	}
	
	public boolean isActive() {
		return active;
	}
	
	public int getAniIndex() {
		return aniIndex;
	}

}
