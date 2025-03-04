package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import audio.AudioPlayer;
import gameStates.Playing;
import helpz.LoadSave;
import main.Game;

import static gameStates.GameState.TITLE;
import static helpz.Constants.UI.UrmButton.*;

public class OverlayGameOver {
	
	private Playing playing;
	private BufferedImage deathScreen;
	private UrmButton bMenu, bRestart;
	private int imgX, imgY, imgW, imgH;
	
	public OverlayGameOver(Playing playing) {
		this.playing = playing;
		createImg();
		
		int y = (int) (195 * Game.SCALE);
		bMenu = new UrmButton((int) (335 * Game.SCALE), y, B_URM_SIZE, B_URM_SIZE, 2);
		bRestart = new UrmButton((int) (440 * Game.SCALE), y, B_URM_SIZE, B_URM_SIZE, 1);
	}
	
	private void createImg() {
		deathScreen = LoadSave.GetSpriteAtlas(LoadSave.DEATH_SCREEN_MENU);
		imgW = (int) (deathScreen.getWidth() * Game.SCALE);
		imgH = (int) (deathScreen.getHeight() * Game.SCALE);
		imgX = Game.GAME_WIDTH / 2 - imgW / 2;
		imgY = (int) (100 * Game.SCALE);
	}

	public void draw(Graphics g) {
		g.setColor(new Color(0, 0, 0, 150));
		g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
		
		g.drawImage(deathScreen, imgX, imgY, imgW, imgH, null);
		
		bMenu.draw(g);
		bRestart.draw(g);
	}
	
	public void update() {
		bMenu.update();
		bRestart.update();
	}
	
	public void mouseMoved(MouseEvent e) {
		bMenu.setMouseOver(false);
		bRestart.setMouseOver(false);
		
		if (bMenu.getBounds().contains(e.getX(), e.getY()))
			bMenu.setMouseOver(true);
		else if (bRestart.getBounds().contains(e.getX(), e.getY()))
			bRestart.setMouseOver(true);
	}
	
	public void mousePressed(MouseEvent e) {
		if (bMenu.getBounds().contains(e.getX(), e.getY())) {
			bMenu.setMousePressed(true);
			playing.getGame().getAudioPlayer().playEffect(AudioPlayer.BUTTON_CLICKED);
		} else if (bRestart.getBounds().contains(e.getX(), e.getY())) {
			bRestart.setMousePressed(true);
			playing.getGame().getAudioPlayer().playEffect(AudioPlayer.BUTTON_CLICKED);
		}
	}
	
	public void mouseReleased(MouseEvent e) {
		if (bMenu.getBounds().contains(e.getX(), e.getY()) && bMenu.isMouseOver()) {
			playing.setGamestate(TITLE);
			bMenu.setMouseOver(false);
		} else if (bRestart.getBounds().contains(e.getX(), e.getY()) && bRestart.isMouseOver()) {
			playing.loadLevel(playing.getLevelManager().getLvlIndex());
			playing.getGame().getAudioPlayer().setLvlSong(playing.getLevelManager().getLvlIndex());
		}
		
		bMenu.setMousePressed(false);
		bRestart.setMousePressed(false);
	}
	
}
