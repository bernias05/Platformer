package gameStates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import audio.AudioPlayer;
import helpz.LoadSave;
import main.Game;
import ui.MenuButton;

import static gameStates.GameState.*;
import static helpz.Constants.UI.Button.*;
import static main.Game.SCALE;

public class Title extends State implements StateMethods {
	
	private MenuButton bPlay, bOptions, bQuit;
	
	private BufferedImage buttonMenu;
	private BufferedImage background;
	private BufferedImage title;
	
	private int menuX, menuY, menuWidth, menuHeight;
	
	public Title(Game game) {
		super(game);

		loadImgs();
		loadMenu();
		initButtons();
	}
	
	private void loadImgs() {
		background = LoadSave.GetSpriteAtlas(LoadSave.TITLE_BG);
		title = LoadSave.GetSpriteAtlas(LoadSave.TITLE_TITLE);
	}

	private void loadMenu() {
		buttonMenu = LoadSave.GetSpriteAtlas(LoadSave.TITLE_MENU);
		
		menuWidth = (int) (buttonMenu.getWidth() * SCALE);
		menuHeight = (int) (buttonMenu.getHeight() * SCALE);
		menuX = Game.GAME_WIDTH / 2 - menuWidth / 2;
		menuY = (int) (title.getHeight() * Game.SCALE);;
	}
	
	private void initButtons() {
		bPlay = new MenuButton(this, Game.GAME_WIDTH / 2 - B_WIDTH / 2, menuY + B_HEIGHT / 2, 0, LEVEL_SELECT);
		bOptions = new MenuButton(this, Game.GAME_WIDTH / 2 - B_WIDTH / 2, menuY + (int) (1.75 * B_HEIGHT), 1, OPTIONS);
		bQuit = new MenuButton(this, Game.GAME_WIDTH / 2 - B_WIDTH / 2, menuY + (int) (3 * B_HEIGHT), 2, QUIT);
	}
	
	@Override
	public void update() {
		bPlay.update();
		bOptions.update();
		bQuit.update();
	}
	
	@Override
	public void render(Graphics g) {
		drawbackground(g);
		drawButtonMenu(g);	
	}
	
	private void drawButtonMenu(Graphics g) {
		int offset = (int) (5 * Game.SCALE);
		g.drawImage(buttonMenu, menuX, menuY, menuWidth, menuHeight, null);
		g.drawImage(title, offset, offset, Game.GAME_WIDTH - 2 * offset, title.getHeight() * Game.GAME_WIDTH / title.getWidth(), null);
		bPlay.draw(g);
		bOptions.draw(g);
		bQuit.draw(g);
	}
	
	private void drawbackground(Graphics g) {
		g.drawImage(background, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		bPlay.setMouseOver(false);
		bOptions.setMouseOver(false);
		bQuit.setMouseOver(false);
		
		if (bPlay.getBounds().contains(e.getX(), e.getY()))
			bPlay.setMouseOver(true);
		else if (bOptions.getBounds().contains(e.getX(), e.getY()))
			bOptions.setMouseOver(true);
		else if (bQuit.getBounds().contains(e.getX(), e.getY()))
			bQuit.setMouseOver(true);
	}
	
	@Override
	public void mousePressed (MouseEvent e) {
		if (bPlay.getBounds().contains(e.getX(), e.getY())) {
			bPlay.setMousePressed(true);
			game.getAudioPlayer().playEffect(AudioPlayer.BUTTON_CLICKED);
		} else if (bOptions.getBounds().contains(e.getX(), e.getY())) {
			bOptions.setMousePressed(true);
			game.getAudioPlayer().playEffect(AudioPlayer.BUTTON_CLICKED);
		} else if (bQuit.getBounds().contains(e.getX(), e.getY())) {
			bQuit.setMousePressed(true);
			game.getAudioPlayer().playEffect(AudioPlayer.BUTTON_CLICKED);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (bPlay.isMouseOver() && bPlay.getBounds().contains(e.getX(), e.getY()))
			bPlay.applyAction();
		else if (bOptions.isMouseOver() && bOptions.getBounds().contains(e.getX(), e.getY()))
			bOptions.applyAction();
		else if (bQuit.isMouseOver() && bQuit.getBounds().contains(e.getX(), e.getY())) {
			bQuit.applyAction();
		}
		
		bPlay.setMouseOver(false);
		bOptions.setMouseOver(false);
		
		bQuit.setMousePressed(false);
		bPlay.setMousePressed(false);
		bOptions.setMousePressed(false);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER)
			state = LEVEL_SELECT;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}

}
