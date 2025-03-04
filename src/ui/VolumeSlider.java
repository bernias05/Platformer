package ui;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import helpz.LoadSave;

import static helpz.Constants.UI.VolumeButton.*;

public class VolumeSlider extends Button {
	
	private BufferedImage[] handleImgs;
	private BufferedImage sliderImg;
	private Rectangle handleBounds;
	
	private int index;
	private int currentX;
	private int minX, maxX;
	private float floatValue = 0.8f;
	
	public VolumeSlider(int x, int y, int width, int height) {
		super(x, y, width, height);
		currentX = x + (int) (0.8 * width) - HANDLE_VOLUME_WIDTH / 2;
		minX = x;
		maxX = x + width - HANDLE_VOLUME_WIDTH;
		loadImgs();
		handleBounds = new Rectangle(currentX, y, HANDLE_VOLUME_WIDTH, height);
	}
	
	private void loadImgs() {
		BufferedImage atlas = LoadSave.GetSpriteAtlas(LoadSave.PAUSE_VOLUME_SLIDER);
		handleImgs = new BufferedImage[3];
		
		for (int i = 0; i < handleImgs.length; i++)
			handleImgs[i] = atlas.getSubimage(i * HANDLE_VOLUME_WIDTH_DEFAULT, 0, HANDLE_VOLUME_WIDTH_DEFAULT, VOLUME_HEIGHT_DEFAULT);
		
		sliderImg = atlas.getSubimage(3 * HANDLE_VOLUME_WIDTH_DEFAULT, 0, SLIDER_VOLUME_WIDTH_DEFAULT, VOLUME_HEIGHT_DEFAULT);
	}
	
	public void update() {
		index = 0;
		if (mouseOver)
			index = 1;
		if (mousePressed)
			index = 2;
	}
	
	public void draw(Graphics g) {
		g.drawImage(sliderImg, bounds.x, bounds.y, SLIDER_VOLUME_WIDTH, bounds.height, null);
		g.drawImage(handleImgs[index], currentX, bounds.y, HANDLE_VOLUME_WIDTH, bounds.height, null);
	}
	
	public void changeX(int newX) {
		if (newX < minX)
			currentX = minX;
		else if (newX > maxX)
			currentX = maxX;
		else
			currentX = newX;
		
		updateFloatValue();
		handleBounds.x = currentX;
	}
	
	private void updateFloatValue() {
		float range = maxX - minX;
		float value = currentX - minX;
		
		floatValue = value / range;
		
	}

	public Rectangle getHandleBounds() {
		return handleBounds;
	}
	
	public float getFloatValue() {
		return floatValue;
	}

}
