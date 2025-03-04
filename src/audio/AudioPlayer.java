package audio;

import java.io.IOException;
import java.net.URL;
import java.util.Random;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.BooleanControl;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import main.Game;

public class AudioPlayer {
	
//	private Game game;
	
	public static int MENU = 0;
	public static int LEVEL_1 = 1;
	public static int LEVEL_2 = 2;
	public static int EDIT = 3;

	public static int DIE = 0;
	public static int JUMP = 1;
	public static int GAMEOVER = 2;
	public static int LVL_COMPLETED = 3;
	public static int ATTACK_1 = 4;
	public static int ATTACK_2 = 5;
	public static int ATTACK_3 = 6;
	public static int POTION_BREAKS = 7;
	public static int POWER_ATTACK = 8;
	public static int CONTAINER_BREAKS = 9;
	public static int PLAYER_HURT = 10;
	public static int BUTTON_CLICKED = 11;
	public static int COIN_COLLECTED = 12;
	
	private Clip[] songs, sfx;
	
	private int currentSongId;
	private float volume = 0.8f;
	private boolean songMute, sfxMute;
	private Random random = new Random();
	
	public AudioPlayer(Game game) {
//		this.game = game;
		loadSongs();
		loadSfx();
		playSong(MENU);
	}
	
	private void loadSongs() {
		String[] names = {"song_menu", "song_level1", "song_level2", "song_edit"};
		songs = new Clip[names.length];
		
		for (int i = 0; i < songs.length; i++)
			songs[i] = getClip(names[i]);
	}
	
	private void loadSfx() {
		String[] names = {"die", "jump", "gameover", "lvlcompleted", "attack1", "attack2", "attack3", 
				"potionbreaks", "powerattack", "containerbreaks", "playerhurt", "buttonclick", "coincollected"};
		sfx = new Clip[names.length];
		
		for (int i = 0; i < sfx.length; i++)
			sfx[i] = getClip(names[i]);
		
		updateSfxVolume();
	}
	
	public Clip getClip(String name) {
		URL url = getClass().getResource("/audio/" + name + ".wav");
		AudioInputStream ais;
		
		try {
			ais = AudioSystem.getAudioInputStream(url);
			Clip c = AudioSystem.getClip();
			c.open(ais);
			return c;
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void setVolume(float volume) {
		this.volume = volume;
		updateSongVolume();
		updateSfxVolume();
	}
	
	public void updateSongVolume() {
		FloatControl gainControl = (FloatControl) songs[currentSongId].getControl(FloatControl.Type.MASTER_GAIN);
		float range = gainControl.getMaximum() - gainControl.getMinimum();
		float gain = (range * volume) + gainControl.getMinimum();
		gainControl.setValue(gain);
	}
	
	private void updateSfxVolume() {
		for (Clip c : sfx) {
			FloatControl gainControl = (FloatControl) c.getControl(FloatControl.Type.MASTER_GAIN);
			float range = gainControl.getMaximum() - gainControl.getMinimum();
			float gain = (range * volume) + gainControl.getMinimum();
			gainControl.setValue(gain);
		}
	}
	
	public void stopSong() {
		if (songs[currentSongId].isActive())
			songs[currentSongId].stop();
	}
	
	public void playSong(int songId) {
		stopSong();
		currentSongId = songId;
		updateSongVolume();
		songs[songId].setMicrosecondPosition(0);
		songs[songId].loop(Clip.LOOP_CONTINUOUSLY);
	}
	
	public void playEffect(int effectId) {
		if (sfx[effectId].getMicrosecondPosition() > 0)
			sfx[effectId].setMicrosecondPosition(0);
		sfx[effectId].start();
	}
	
	public void setLvlSong(int lvlIndex) {
		if (lvlIndex % 2 == 0)
			playSong(LEVEL_1);
		else
			playSong(LEVEL_2);
	}
	
	public void lvlCompleted() {
		stopSong();
		playEffect(LVL_COMPLETED);
	}
	
	public void playAttackSound() {
		int start = 4;
		start += random.nextInt(3);
		playEffect(start);
	}
	
	public void toggleSongMute() {
		songMute = !songMute;
		for (Clip c : songs) {
			BooleanControl booleanControl = (BooleanControl) c.getControl(BooleanControl.Type.MUTE);
			booleanControl.setValue(songMute);
		}
	}
	
	public void toggleSfxMute() {
		sfxMute = !sfxMute;
		for (Clip c : sfx) {
			BooleanControl booleanControl = (BooleanControl) c.getControl(BooleanControl.Type.MUTE);
			booleanControl.setValue(sfxMute);
		}
		if (!sfxMute)
			playEffect(JUMP);
	}

}
