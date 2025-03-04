package inputs;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import gameStates.GameState;
import main.GamePanel;

public class KeyboardInputs implements KeyListener {

	private GamePanel gamePanel;
	
	public KeyboardInputs(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (GameState.state) {
		case TITLE:
			gamePanel.getGame().getTitle().keyPressed(e);
			break;
		case LEVEL_SELECT:
			gamePanel.getGame().getLevelSelect().keyPressed(e);
			break;
		case EDITING:
			gamePanel.getGame().getEditing().keyPressed(e);
			break;
		case PLAYING:
			gamePanel.getGame().getPlaying().keyPressed(e);
		case OPTIONS:
			break;
		case QUIT:
			break;
		default:
			break;
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch (GameState.state) {
		case TITLE:
			gamePanel.getGame().getTitle().keyReleased(e);
			break;
		case LEVEL_SELECT:
			gamePanel.getGame().getLevelSelect().keyReleased(e);
			break;
		case EDITING:
			gamePanel.getGame().getEditing().keyReleased(e);
			break;
		case PLAYING:
			gamePanel.getGame().getPlaying().keyReleased(e);
		case OPTIONS:
			break;
		case QUIT:
			break;
		default:
			break;
		}
	}

}
