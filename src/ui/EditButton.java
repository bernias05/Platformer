package ui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import helpz.LoadSave;
import main.Game;

public class EditButton extends Button {

	private BufferedImage[] bg;
	private BufferedImage[] icons;
	private BufferedImage selectedMark;
	
	private boolean selected;
	
	private int index, rotationIndex;
	
	private int imgX, imgY, imgWidth, imgHeight;
	
	public EditButton(BufferedImage[] icons, int x, int y, int width) {
		super(x, y, width, width);
		this.icons = icons;
		loadImgs();
		if (icons != null)
			normalizeImg();
	}
	
	private void loadImgs() {
		selectedMark = LoadSave.GetSpriteAtlas(LoadSave.EDIT_BUTTON_BG_MARK);
		BufferedImage bgAtlas = LoadSave.GetSpriteAtlas(LoadSave.EDIT_BUTTON_BG);
		bg = new BufferedImage[2];
		for (int i = 0; i < 2; i++) {
			bg[i] = bgAtlas.getSubimage(i * 84, 0, 84, 84);
		}
	}
	
	private void normalizeImg() {
		int w = icons[rotationIndex].getWidth();
		int h = icons[rotationIndex].getHeight();
		int drawWidth = (int) (bounds.width - 8 * Game.SCALE);
		int drawOriginX = (int) (bounds.x + 4 * Game.SCALE);
		int drawOriginY = (int) (bounds.y + 4 * Game.SCALE);
		
		if (w > h) {
			imgWidth = drawWidth;
			imgHeight = h * drawWidth / w;
			imgX = drawOriginX;
			imgY = drawOriginY + (drawWidth - imgHeight) / 2;
		} else if (w == h) {
			imgWidth = imgHeight = drawWidth;
			imgX = drawOriginX;
			imgY = drawOriginY;
		} else {
			imgHeight = drawWidth;
			imgWidth = w * drawWidth / h;
			imgX = drawOriginX + (drawWidth - imgWidth) / 2;
			imgY = drawOriginY;
		}
	}
	
	public void update() {
		index = 0;
		if (mouseOver)
			index = 1;
	}
	
	public void draw(Graphics g) {
		g.drawImage(bg[index], bounds.x, bounds.y, bounds.width, bounds.height, null);
		if (selected) {
			g.drawImage(selectedMark, bounds.x, bounds.y, bounds.width, bounds.height, null);
		}
		if (icons != null) {
			g.drawImage(icons[rotationIndex], 
					imgX, 
					imgY, 
					imgWidth, 
					imgHeight, null);
		}
	}
	
	public void rotate() {
		if (icons != null) {
			rotationIndex = (rotationIndex + 1) % icons.length;
			normalizeImg();
		}
	}
	
	public boolean isSelected() {
		return selected;
	}
	
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	public int getRotationIndex() {
		return rotationIndex;
	}

}
