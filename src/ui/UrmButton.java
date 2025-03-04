package ui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import helpz.LoadSave;
import static helpz.Constants.UI.UrmButton.*;

public class UrmButton extends Button {
	
	private BufferedImage[] buttonImgs;
	private int rowIndex, index;
	
	public UrmButton(int x, int y, int width, int height, int rowIndex) {
		super(x, y, width, height);
		this.rowIndex = rowIndex;
		loadImgs();
	}
	
	private void loadImgs() {
		BufferedImage atlas = LoadSave.GetSpriteAtlas(LoadSave.PAUSE_URM_BUTTONS);
		buttonImgs = new BufferedImage[3];
		
		for (int i = 0; i < buttonImgs.length; i++)
			buttonImgs[i] = atlas.getSubimage(i * B_URM_SIZE_DEFAULT, rowIndex * B_URM_SIZE_DEFAULT, B_URM_SIZE_DEFAULT, B_URM_SIZE_DEFAULT);
		
	}
	
	public void update() {
		index = 0;
		if (mouseOver)
			index = 1;
		if (mousePressed)
			index = 2;
	}
	
	public void draw(Graphics g) {
		g.drawImage(buttonImgs[index], bounds.x, bounds.y, bounds.width, bounds.height, null);
	}

}
