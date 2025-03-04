package ui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import helpz.LoadSave;

import static helpz.Constants.UI.LevelButton.*;

public class LevelButton extends Button {
	
	private BufferedImage[] imgs;
	private int lvlIndex;
	private int index = 0;
	private boolean unlocked;
	
	public LevelButton(int lvlIndex, int x, int y, int width, int height, boolean unlocked) {
		super(x, y, width, height);
		this.lvlIndex = lvlIndex;
		this.unlocked = unlocked;
		loadImgs();
	}
	
	private void loadImgs() {
		BufferedImage atlas = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_BUTTONS);
		imgs = new BufferedImage[4];
		
		for (int i = 0; i < imgs.length; i++) {
			imgs[i] = atlas.getSubimage(i * B_LEVEL_SIZE_DEFAULT, lvlIndex * B_LEVEL_SIZE_DEFAULT, B_LEVEL_SIZE_DEFAULT, B_LEVEL_SIZE_DEFAULT);
		}
	}
	
	public void update() {
		if (!unlocked)
			return;
		index = 1;
		if (mouseOver)
			index = 2;
		if (mousePressed)
			index = 3;
	}
	
	public void draw(Graphics g) {
		g.drawImage(imgs[index], bounds.x, bounds.y, bounds.width, bounds.height, null);
	}

	public int getLvlIndex() {
		return lvlIndex;
	}
	
	public void unlock() {
		unlocked = true;
	}
	
	public boolean isUnlocked() {
		return unlocked;
	}

}
