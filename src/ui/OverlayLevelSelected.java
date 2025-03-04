package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import audio.AudioPlayer;
import gameStates.GameState;
import gameStates.LevelSelect;
import helpz.LoadSave;
import main.Game;

import static helpz.Constants.UI.UrmButton.*;

public class OverlayLevelSelected {
	
	private LevelSelect levelSelect;
	private UrmButton bEdit, bPlay;
	private BufferedImage menu;
	private Rectangle bounds;
	private int lvlIndex;
	
	public OverlayLevelSelected(LevelSelect levelSelect) {
		this.levelSelect = levelSelect;
		init();
	}
	
	public void init() {
		menu = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_SELECT_MENU_BG);
		
		int menuW = (int) (menu.getWidth() * Game.SCALE);
		int menuH = (int) (menu.getHeight() * Game.SCALE);
		int menuX = (int) (Game.GAME_WIDTH - menu.getTileWidth() * Game.SCALE) / 2;
		int menuY = (int) (120 * Game.SCALE);
		
		bounds = new Rectangle(menuX, menuY, menuW, menuH);
		
		bPlay = new UrmButton((int) (590 * Game.SCALE), (int) (290 * Game.SCALE), B_URM_SIZE, B_URM_SIZE, 0);
		bEdit = new UrmButton((int) (190 * Game.SCALE), (int) (290 * Game.SCALE), B_URM_SIZE, B_URM_SIZE, 3);
	}
	
	public void update() {
		bPlay.update();
		bEdit.update();
	}
	
	public void draw(Graphics g) {
		g.drawImage(menu, bounds.x, bounds.y, bounds.width, bounds.height, null);

		g.setColor(Color.orange);
		g.setFont(new Font("Kalam", Font.BOLD, 100));
		if (lvlIndex == 0) {
			g.drawString("Tutorial", (int) (330 * Game.SCALE), (int) (105 * Game.SCALE));

			g.setColor(Color.black);
			g.setFont(new Font("Kalam", Font.BOLD, 50));
			g.drawString("Reach the boat to complete the level.", (int) (200 * Game.SCALE), (int) (185 * Game.SCALE));
		} else
			g.drawString("Level " + lvlIndex, (int) (330 * Game.SCALE), (int) (105 * Game.SCALE));
		bPlay.draw(g);
		bEdit.draw(g);
	}
	
	public void mouseMoved(MouseEvent e) {
		bPlay.setMouseOver(false);
		bEdit.setMouseOver(false);
		if (bPlay.getBounds().contains(e.getX(), e.getY()))
			bPlay.setMouseOver(true);
		else if (bEdit.getBounds().contains(e.getX(), e.getY()))
			bEdit.setMouseOver(true);
	}
	
	public void mousePressed(MouseEvent e) {
		if (!bounds.contains(e.getX(), e.getY())) {
			levelSelect.setLevelSelected(false);
		} else if (bPlay.getBounds().contains(e.getX(), e.getY())) {
			bPlay.setMousePressed(true);
			levelSelect.getGame().getAudioPlayer().playEffect(AudioPlayer.BUTTON_CLICKED);
		} else if (bEdit.getBounds().contains(e.getX(), e.getY())) {
			bEdit.setMousePressed(true);
			levelSelect.getGame().getAudioPlayer().playEffect(AudioPlayer.BUTTON_CLICKED);
		}
	}
	
	public void mouseReleased(MouseEvent e) {
		if (bPlay.getBounds().contains(e.getX(), e.getY())) {
			levelSelect.setGamestate(GameState.PLAYING);
			levelSelect.getGame().getPlaying().loadLevel(lvlIndex);
			levelSelect.setLevelSelected(false);
		} else if (bEdit.getBounds().contains(e.getX(), e.getY())) {
			levelSelect.getGame().getPlaying().loadLevel(lvlIndex);
			levelSelect.getGame().getEditing().setLevel(levelSelect.getGame().getPlaying().getLevelManager().getCurrentLevel());
			levelSelect.setGamestate(GameState.EDITING);
		}
		
		bPlay.setMousePressed(false);
		bEdit.setMousePressed(false);
		bPlay.setMouseOver(false);
		bEdit.setMouseOver(false);
	}
	
	public void setLevel(int level) {
		lvlIndex = level;
	}
	
}