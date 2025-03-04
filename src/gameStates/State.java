package gameStates;

import audio.AudioPlayer;
import main.Game;

public abstract class State {

	protected Game game;
	private boolean paused;
	
	public State(Game game) {
		this.game = game;
	}
	
	public Game getGame() {
		return game;
	}
	
	public void setGamestate(GameState state) {
		switch(state) {
		case TITLE: 
			game.getAudioPlayer().playSong(AudioPlayer.MENU);
			break;
		case PLAYING: 
			game.getAudioPlayer().setLvlSong(game.getPlaying().getLevelManager().getLvlIndex());
			break;
		case LEVEL_SELECT: 
			game.getAudioPlayer().playSong(AudioPlayer.MENU);
			break;
		case EDITING:
			game.getAudioPlayer().playSong(AudioPlayer.EDIT);
		case OPTIONS: break;
		case QUIT: break;
		default: break;
		}
		GameState.state = state;
	}
	
	public void setPaused(boolean paused) {
		this.paused = paused;
	}
	
}
