package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import gameStates.Editing;
import helpz.LoadSave;
import main.Game;

public class Toolbar {
	
	private Editing editing;
	private Rectangle bounds;
	private EditButton[] buttons;
	
	private BufferedImage bg;
	
	private BufferedImage[] groundImg    = new BufferedImage[1], 
							waterImg     = new BufferedImage[1],
							spikeImg     = new BufferedImage[1], 
							boatImg      = new BufferedImage[1],
							coinImg      = new BufferedImage[1],
							startPosImg  = new BufferedImage[1],

							containerImgs = new BufferedImage[2], 
							potionImgs    = new BufferedImage[2],
							cannonImgs	  = new BufferedImage[2],
							
							enemyImgs	  = new BufferedImage[3], 
							treeImgs	  = new BufferedImage[3];
	
	private int selectedButtonIndex = -1;
	
	private boolean buttonSelected;
	
	public Toolbar(Editing editing) {
		this.editing = editing;
		int x = (int) (Game.GAME_WIDTH / 8);
		int y = (int) (Game.GAME_HEIGHT * 0.7);
		int w = (int) (Game.GAME_WIDTH / 8 * 6);
		int h = (int) (Game.GAME_HEIGHT * 0.283);
		bounds = new Rectangle(x, y, w, h);
		
		loadImgs();
		initButtons();
	}
	
	private void loadImgs() {
		bg = LoadSave.GetSpriteAtlas(LoadSave.TOOLBAR_BG);
		groundImg[0] = editing.getGame().getPlaying().getLevelManager().getLevelTile();
		waterImg[0] = editing.getGame().getPlaying().getLevelManager().getWaterTile();
		spikeImg[0] = editing.getGame().getPlaying().getObjectManager().getSpikeImg();
		boatImg[0] = editing.getGame().getPlaying().getObjectManager().getBoatImg();
		coinImg[0] = editing.getGame().getPlaying().getObjectManager().getCoinImg();
		startPosImg[0] = LoadSave.GetSpriteAtlas(LoadSave.START_POS);

		BufferedImage containerAtlas = LoadSave.GetSpriteAtlas(LoadSave.ATLAS_CONTAINER);
		BufferedImage potionAtlas = LoadSave.GetSpriteAtlas(LoadSave.ATLAS_POTION);
		BufferedImage cannonAtlas = LoadSave.GetSpriteAtlas(LoadSave.ATLAS_CANNON);
		BufferedImage curvedTreeAtlas = LoadSave.GetSpriteAtlas(LoadSave.ATLAS_CURVED_TREE);
		
		for (int i = 0; i < 2; i++) {
			containerImgs[i] = containerAtlas .getSubimage(i * containerAtlas.getWidth() / containerImgs.length, 0, containerAtlas.getWidth() / containerImgs.length, containerAtlas.getHeight());
			potionImgs[i]    = potionAtlas    .getSubimage(i * potionAtlas   .getWidth() / potionImgs.length,    0, potionAtlas   .getWidth() / potionImgs.length,    potionAtlas   .getHeight());
			cannonImgs[i]    = cannonAtlas    .getSubimage(i * cannonAtlas   .getWidth() / cannonImgs.length,    0, cannonAtlas   .getWidth() / cannonImgs.length,    cannonAtlas   .getHeight());
			treeImgs[i]      = curvedTreeAtlas.getSubimage(i * curvedTreeAtlas.getWidth() / 2, 0, curvedTreeAtlas.getWidth() / 2, curvedTreeAtlas.getHeight());
		}
		enemyImgs[0] = LoadSave.GetSpriteAtlas(LoadSave.ATLAS_CRABBY);
		enemyImgs[1] = LoadSave.GetSpriteAtlas(LoadSave.ATLAS_PINKSTAR);
		enemyImgs[2] = LoadSave.GetSpriteAtlas(LoadSave.ATLAS_SHARK);
		
		treeImgs[2] = LoadSave.GetSpriteAtlas(LoadSave.ATLAS_STRAIGHT_TREE);
	}
	
	private void initButtons() {
		buttons = new EditButton[12];
		
		int length = (int) (bounds.width / (buttons.length * 5) * 4);
		int xOffset = (int) (length * 1.25);
		
		int x = bounds.x + xOffset - length;
		int y = bounds.y - xOffset + bounds.height;
		
		buttons[0]  = new EditButton(null, 		    x + xOffset * 0,  y, length);
		buttons[1]  = new EditButton(groundImg,     x + xOffset * 1,  y, length);
		buttons[2]  = new EditButton(waterImg,      x + xOffset * 2,  y, length);
		buttons[3]  = new EditButton(spikeImg,      x + xOffset * 3,  y, length);
		buttons[4]  = new EditButton(boatImg,       x + xOffset * 4,  y, length);
		buttons[5]  = new EditButton(containerImgs, x + xOffset * 5,  y, length);
		buttons[6]  = new EditButton(potionImgs,    x + xOffset * 6,  y, length);
		buttons[7]  = new EditButton(cannonImgs,    x + xOffset * 7,  y, length);
		buttons[8]  = new EditButton(enemyImgs,     x + xOffset * 8,  y, length);
		buttons[9]  = new EditButton(treeImgs, 	    x + xOffset * 9,  y, length);
		buttons[10] = new EditButton(coinImg,       x + xOffset * 10, y, length);
		buttons[11] = new EditButton(startPosImg,   x + xOffset * 11, y, length);
	}
	
	public void update() {
		for (EditButton eb : buttons)
			eb.update();
	}
	
	public void draw(Graphics g) {
		g.drawImage(bg, bounds.x, bounds.y, bounds.width, bounds.height, null);
		for (EditButton b : buttons) {
			b.draw(g);
		}
		g.setColor(Color.BLACK);
		g.setFont(new Font("Kalam", Font.PLAIN, 50));
		if (selectedButtonIndex >= 5 && selectedButtonIndex < 10)
			g.drawString("Press R to switch item", bounds.x + (int) (10 * Game.SCALE), bounds.y + (int) (42 * Game.SCALE));
		if (selectedButtonIndex == 10)
			g.drawString(editing.getGame().getPlaying().getLevelManager().getCurrentLevel().getCoins().size() + "/3", bounds.x + (int) (10 * Game.SCALE), bounds.y + (int) (42 * Game.SCALE));
	}
	
	public void mouseMoved(MouseEvent e) {
		for (EditButton eb : buttons) {
			eb.setMouseOver(false);
			if (eb.getBounds().contains(e.getX(), e.getY()))
				eb.setMouseOver(true);
		}
	}
	
	public void mousePressed(MouseEvent e) {
		for (int i = 0; i < buttons.length; i++) {
			if (buttons[i].getBounds().contains(e.getX(), e.getY())) {
				for (EditButton eb : buttons)
					if (eb != buttons[i])
						eb.setSelected(false);
				selectedButtonIndex = i;
				buttons[i].setSelected(true);
				buttonSelected = true;
				return;
			}
		}
		for (EditButton eb : buttons)
			eb.setSelected(false);
		buttonSelected = false;
		selectedButtonIndex = -1;
	}
	
	public void mouseReleased(MouseEvent e) {
		for (EditButton eb : buttons)
			eb.setMousePressed(false);
	}

	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_R)
			if (buttonSelected)
				buttons[selectedButtonIndex].rotate();
	}
	
	public Rectangle getBounds() {
		return bounds;
	}
	
	public boolean isButtonSelected() {
		return buttonSelected;
	}
	
	public int getSelectedButtonIndex() {
		return selectedButtonIndex;
	}
	
	public EditButton[] getButtons() {
		return buttons;
	}
	
	public BufferedImage getSpawnPointImg() {
		return startPosImg[0];
	}
	
}
