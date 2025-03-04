package ui;

import static helpz.Constants.UI.SoundButton.B_SOUND_SIZE;
import static helpz.Constants.UI.VolumeButton.HANDLE_VOLUME_WIDTH;
import static helpz.Constants.UI.VolumeButton.SLIDER_VOLUME_WIDTH;
import static helpz.Constants.UI.VolumeButton.VOLUME_HEIGHT;

import java.awt.Graphics;
import java.awt.event.MouseEvent;

import audio.AudioPlayer;
import main.Game;

public class AudioOptions {
	
	private Game game;
	private SoundButton bMusic, bSfx;
	private VolumeSlider bVolume;
	
	public AudioOptions(Game game) {
		this.game = game;
		initSoundButtons();
		initVolumeSlider();
	}

	private void initSoundButtons() {
		int bSoundX = (int) (450 * Game.SCALE);
		int bMusicY = (int) (145 * Game.SCALE);
		int bSfxY = (int) (191 * Game.SCALE);
		
		bMusic = new SoundButton(bSoundX, bMusicY, B_SOUND_SIZE, B_SOUND_SIZE);
		bSfx = new SoundButton(bSoundX, bSfxY, B_SOUND_SIZE, B_SOUND_SIZE);
	}

	private void initVolumeSlider() {
		int vX = (int) (309 * Game.SCALE);
		int vY = (int) (283 * Game.SCALE);
		
		bVolume = new VolumeSlider(vX, vY, SLIDER_VOLUME_WIDTH, VOLUME_HEIGHT);		
	}
	
	public void update() {
		bMusic.update();
		bSfx.update();
		bVolume.update();
	}
	
	public void draw(Graphics g) {
		bMusic.draw(g);
		bSfx.draw(g);
		bVolume.draw(g);
	}
	
	public void mouseDragged(MouseEvent e) {
		if (bVolume.isMousePressed()) {
			float valueBefore = bVolume.getFloatValue();
			bVolume.changeX(e.getX() - HANDLE_VOLUME_WIDTH / 2);
			float valueAfter = bVolume.getFloatValue();
			
			if (valueBefore != valueAfter)
				game.getAudioPlayer().setVolume(valueAfter);
		}
	}
	
	public void mouseMoved(MouseEvent e) {
		bMusic.setMouseOver(false);
		bSfx.setMouseOver(false);
		bVolume.setMouseOver(false);
		
		if (bMusic.getBounds().contains(e.getX(), e.getY()))
			bMusic.setMouseOver(true);
		else if (bSfx.getBounds().contains(e.getX(), e.getY()))
			bSfx.setMouseOver(true);
		else if (bVolume.getHandleBounds().contains(e.getX(), e.getY()))
			bVolume.setMouseOver(true);
	}
	
	public void mousePressed(MouseEvent e) {
		if (bMusic.getBounds().contains(e.getX(), e.getY())) {
			bMusic.setMousePressed(true);
			game.getAudioPlayer().playEffect(AudioPlayer.BUTTON_CLICKED);
		} else if (bSfx.getBounds().contains(e.getX(), e.getY())) {
			bSfx.setMousePressed(true);
			game.getAudioPlayer().playEffect(AudioPlayer.BUTTON_CLICKED);
		} else if (bVolume.getBounds().contains(e.getX(), e.getY())) {
			bVolume.setMousePressed(true);
			game.getAudioPlayer().playEffect(AudioPlayer.BUTTON_CLICKED);
		}
	}
	
	public void mouseReleased(MouseEvent e) {
		if (bMusic.getBounds().contains(e.getX(), e.getY()) && bMusic.isMouseOver()) {
			bMusic.setMuted(!bMusic.isMuted());
			game.getAudioPlayer().toggleSongMute();
		} else if (bSfx.getBounds().contains(e.getX(), e.getY()) && bSfx.isMouseOver()) {
			bSfx.setMuted(!bSfx.isMuted());
			game.getAudioPlayer().toggleSfxMute();
		}
		bMusic.setMousePressed(false);
		bSfx.setMousePressed(false);
		bVolume.setMousePressed(false);
	}
	
}
