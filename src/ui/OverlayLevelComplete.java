package ui;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import audio.AudioPlayer;
import gameStates.Playing;
import helpz.LoadSave;
import main.Game;

import static helpz.Constants.UI.UrmButton.*;
import static gameStates.GameState.*;

public class OverlayLevelComplete {

	private int bgX, bgY, bgWidth, bgHeight;
	
	private Playing playing;
	private UrmButton bMenu, bPlayAgain;
	private BufferedImage img;
	
	public OverlayLevelComplete(Playing playing) {
		this.playing = playing;
		
		loadImg();
		initButtons();
	}

	private void loadImg() {
		img = LoadSave.GetSpriteAtlas(LoadSave.COMPLETED_MENU);
		bgWidth = (int) (img.getWidth() * Game.SCALE);
		bgHeight = (int) (img.getHeight() * Game.SCALE);
		bgX = (Game.GAME_WIDTH - bgWidth) / 2;
		bgY = (int) (75 * Game.SCALE);
	}

	private void initButtons() {
		int y = (int) (195 * Game.SCALE);
		
		bMenu = new UrmButton((int) (330 * Game.SCALE), y, B_URM_SIZE, B_URM_SIZE, 2);
		bPlayAgain = new UrmButton((int) (445 * Game.SCALE), y, B_URM_SIZE, B_URM_SIZE, 1);
	}
	
	public void update() {
		bMenu.update();
		bPlayAgain.update();
	}
	
	public void draw(Graphics g) {
		g.drawImage(img, bgX, bgY, bgWidth, bgHeight, null);
		bMenu.draw(g);
		bPlayAgain.draw(g);
	}
	
	public void mouseMoved(MouseEvent e) {
		bMenu.setMouseOver(false);
		bPlayAgain.setMouseOver(false);
		
		if (bMenu.getBounds().contains(e.getX(), e.getY()))
			bMenu.setMouseOver(true);
		else if (bPlayAgain.getBounds().contains(e.getX(), e.getY()))
			bPlayAgain.setMouseOver(true);
	}
	
	public void mousePressed(MouseEvent e) {
		if (bMenu.getBounds().contains(e.getX(), e.getY())) {
			bMenu.setMousePressed(true);
			playing.getGame().getAudioPlayer().playEffect(AudioPlayer.BUTTON_CLICKED);
		} else if (bPlayAgain.getBounds().contains(e.getX(), e.getY())) {
			bPlayAgain.setMousePressed(true);
			playing.getGame().getAudioPlayer().playEffect(AudioPlayer.BUTTON_CLICKED);
		}
	}
	
	public void mouseReleased(MouseEvent e) {
		if (bMenu.getBounds().contains(e.getX(), e.getY()) && bMenu.isMouseOver()) {
			playing.setGamestate(LEVEL_SELECT);
			bMenu.setMouseOver(false);
		} else if (bPlayAgain.getBounds().contains(e.getX(), e.getY()) && bPlayAgain.isMouseOver())
			playing.loadLevel(playing.getLevelManager().getLvlIndex());
		
		bMenu.setMousePressed(false);
		bPlayAgain.setMousePressed(false);
	}
	
}
