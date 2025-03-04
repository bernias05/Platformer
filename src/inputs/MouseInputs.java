package inputs;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import gameStates.GameState;
import main.GamePanel;

public class MouseInputs implements MouseListener, MouseMotionListener, MouseWheelListener {

	private GamePanel gamePanel;
	
	public MouseInputs(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
	}
	
	public void mouseDragged(MouseEvent e) {
		switch (GameState.state) {
		case PLAYING:
			gamePanel.getGame().getPlaying().mouseDragged(e);
			break;
		case LEVEL_SELECT:
			gamePanel.getGame().getLevelSelect().mouseDragged(e);
			break;
		case OPTIONS:
			gamePanel.getGame().getOptions().mouseDragged(e);
			break;
		case EDITING:
			gamePanel.getGame().getEditing().mouseDragged(e);
			break;
		default:
			break;
		}
	}
	
	public void mouseMoved(MouseEvent e) {
		switch (GameState.state) {
		case TITLE:
			gamePanel.getGame().getTitle().mouseMoved(e);
			break;
		case LEVEL_SELECT:
			gamePanel.getGame().getLevelSelect().mouseMoved(e);
			break;
		case PLAYING:
			gamePanel.getGame().getPlaying().mouseMoved(e);
			break;
		case OPTIONS:
			gamePanel.getGame().getOptions().mouseMoved(e);
			break;
		case EDITING:
			gamePanel.getGame().getEditing().mouseMoved(e);
			break;
		default:
			break;
		}
	}
	
	public void mousePressed(MouseEvent e) {
		switch (GameState.state) {
		case TITLE:
			gamePanel.getGame().getTitle().mousePressed(e);
			break;
		case LEVEL_SELECT:
			gamePanel.getGame().getLevelSelect().mousePressed(e);
			break;
		case PLAYING:
			gamePanel.getGame().getPlaying().mousePressed(e);
			break;
		case OPTIONS:
			gamePanel.getGame().getOptions().mousePressed(e);
			break;
		case EDITING:
			gamePanel.getGame().getEditing().mousePressed(e);
			break;
		default:
			break;
		}
	}

	
	public void mouseReleased(MouseEvent e) {
		switch (GameState.state) {
		case TITLE:
			gamePanel.getGame().getTitle().mouseReleased(e);
			break;
		case LEVEL_SELECT:
			gamePanel.getGame().getLevelSelect().mouseReleased(e);
			break;
		case PLAYING:
			gamePanel.getGame().getPlaying().mouseReleased(e);
			break;
		case OPTIONS:
			gamePanel.getGame().getOptions().mouseReleased(e);
			break;
		case EDITING:
			gamePanel.getGame().getEditing().mouseReleased(e);
			break;
		default:
			break;
		}
	}
	
	public void mouseClicked(MouseEvent e) {
		
	}
	public void mouseEntered(MouseEvent e) {
		
	}

	public void mouseExited(MouseEvent e) {
		
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		
	}

}
