package ui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import gameStates.GameState;
import gameStates.State;
import gameStates.Title;
import helpz.LoadSave;
import static helpz.Constants.UI.Button.*;

public class MenuButton extends Button {

	private int index;
	private int rowIndex;
	
	private Title title;
	private GameState state;
	private BufferedImage[] imgs = new BufferedImage[3];
	
	public MenuButton(Title title, int x, int y, int rowIndex, GameState state) {
		super(x, y, B_WIDTH, B_HEIGHT);
		this.title = title;
		this.rowIndex = rowIndex;
		this.state = state;
		loadImgs();
	}
	
	private void loadImgs() {
		BufferedImage tempAtlas = LoadSave.GetSpriteAtlas(LoadSave.TITLE_BUTTONS);
		
		for (int i = 0; i < imgs.length; i++) {
			imgs[i] = tempAtlas.getSubimage(i * B_WIDTH_DEFAULT, rowIndex * B_HEIGHT_DEFAULT, B_WIDTH_DEFAULT, B_HEIGHT_DEFAULT);
		}
		
	}
	
	public void draw(Graphics g) {
		g.drawImage(imgs[index], bounds.x, bounds.y, B_WIDTH, B_HEIGHT, null);
	}
	
	public void update() {
		index = 0;
		if (mouseOver)
			index = 1;
		if (mousePressed)
			index = 2;
	}
	
	public void applyAction() {
		title.setGamestate(state);
	}
	
}
