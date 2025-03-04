package ui;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import audio.AudioPlayer;
import gameStates.Playing;
import helpz.LoadSave;
import main.Game;

import static gameStates.GameState.*;

import static helpz.Constants.UI.UrmButton.*;

public class OverlayPause {
	
	private int bgX, bgY, bgWidth, bgHeight;
	
	private Playing playing;
	private AudioOptions audioOptions;
	private BufferedImage backgroundImg;
	private UrmButton bContinue, bRestart, bMenu;
	
	public OverlayPause(Playing playing, AudioOptions audioOptions) {
		this.playing = playing;
		this.audioOptions = audioOptions;
		
		loadBackground();
		initUrmButtons();
	}
	
	private void loadBackground() {
		backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.PAUSE_MENU);
		bgWidth = (int) (backgroundImg.getWidth() * Game.SCALE);
		bgHeight = (int) (backgroundImg.getHeight() * Game.SCALE);
		bgX = (Game.GAME_WIDTH - bgWidth) / 2;
		bgY = (Game.GAME_HEIGHT - bgHeight) / 2;
		
	}

	private void initUrmButtons() {
		int bHomeX = (int) (313 * Game.SCALE);
		int bRestartX = (int) (387 * Game.SCALE);
		int bContinueX = (int) (461 * Game.SCALE);
		int bY = (int) (330 * Game.SCALE);
		
		bMenu = new UrmButton(bHomeX, bY, B_URM_SIZE, B_URM_SIZE, 2);
		bRestart = new UrmButton(bRestartX, bY, B_URM_SIZE, B_URM_SIZE, 1);
		bContinue = new UrmButton(bContinueX, bY, B_URM_SIZE, B_URM_SIZE, 0);
	}
	
	public void update() {
		audioOptions.update();
		bContinue.update();
		bRestart.update();
		bMenu.update();
	}
	
	public void draw(Graphics g) {
		g.drawImage(backgroundImg, bgX, bgY, bgWidth, bgHeight, null);
		
		audioOptions.draw(g);
		bContinue.draw(g);
		bRestart.draw(g);
		bMenu.draw(g);	
	}
	
	public void mouseMoved(MouseEvent e) {
		bContinue.setMouseOver(false);
		bRestart.setMouseOver(false);
		bMenu.setMouseOver(false);
		
		if (bContinue.getBounds().contains(e.getX(), e.getY()))
			bContinue.setMouseOver(true);
		else if (bRestart.getBounds().contains(e.getX(), e.getY()))
			bRestart.setMouseOver(true);
		else if (bMenu.getBounds().contains(e.getX(), e.getY()))
			bMenu.setMouseOver(true);
		else
			audioOptions.mouseMoved(e);
	}
	
	public void mousePressed(MouseEvent e) {
		if (bContinue.getBounds().contains(e.getX(), e.getY())) {
			bContinue.setMousePressed(true);
			playing.getGame().getAudioPlayer().playEffect(AudioPlayer.BUTTON_CLICKED);
		} else if (bRestart.getBounds().contains(e.getX(), e.getY())) {
			bRestart.setMousePressed(true);
			playing.getGame().getAudioPlayer().playEffect(AudioPlayer.BUTTON_CLICKED);
		} else if (bMenu.getBounds().contains(e.getX(), e.getY())) {
			bMenu.setMousePressed(true);
			playing.getGame().getAudioPlayer().playEffect(AudioPlayer.BUTTON_CLICKED);
		} else
			audioOptions.mousePressed(e);
	}
	
	public void mouseReleased(MouseEvent e) {
		if (bContinue.getBounds().contains(e.getX(), e.getY()) && bContinue.isMouseOver())
			playing.setPaused(false);
		else if (bRestart.getBounds().contains(e.getX(), e.getY()) && bRestart.isMouseOver()) {
			playing.loadLevel(playing.getLevelManager().getLvlIndex());
			playing.setPaused(false);
		} else if (bMenu.getBounds().contains(e.getX(), e.getY()) && bMenu.isMouseOver()) {
			playing.setGamestate(LEVEL_SELECT);
			playing.setPaused(false);
		} else
			audioOptions.mouseReleased(e);
		
		bContinue.setMouseOver(false);
		bRestart.setMouseOver(false);
		bMenu.setMouseOver(false);
		
		bContinue.setMousePressed(false);
		bRestart.setMousePressed(false);
		bMenu.setMousePressed(false);
	}
	
}
