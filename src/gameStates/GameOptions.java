package gameStates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import audio.AudioPlayer;
import helpz.LoadSave;
import main.Game;
import ui.AudioOptions;
import ui.UrmButton;

import static gameStates.GameState.*;
import static helpz.Constants.UI.UrmButton.*;

public class GameOptions extends State implements StateMethods {
	
	private AudioOptions audioOptions;
	private BufferedImage bg, menu;
	private UrmButton bMenu;
	private int menuX, menuY, menuW, menuH;

	public GameOptions(Game game, AudioOptions audioOptions) {
		super(game);
		this.audioOptions = audioOptions;
		loadImgs();
		initButton();
	}

	private void loadImgs() {
		bg = LoadSave.GetSpriteAtlas(LoadSave.TITLE_BG);
		menu = LoadSave.GetSpriteAtlas(LoadSave.OPTIONS_MENU);
		
		menuW = (int) (menu.getWidth() * Game.SCALE);
		menuH = (int) (menu.getHeight() * Game.SCALE);
		menuX = (Game.GAME_WIDTH - menuW) / 2;
		menuY = (int) (38 * Game.SCALE);
	}
	
	private void initButton() {
		int x = (int) (387 * Game.SCALE);
		int y = (int) (335 * Game.SCALE);
		
		bMenu = new UrmButton(x, y, B_URM_SIZE, B_URM_SIZE, 2);
	}

	@Override
	public void update() {
		audioOptions.update();
		bMenu.update();
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(bg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
		g.drawImage(menu, menuX, menuY, menuW, menuH, null);

		audioOptions.draw(g);
		bMenu.draw(g);
	}
	
	public void mouseDragged(MouseEvent e) {
		audioOptions.mouseDragged(e);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		bMenu.setMouseOver(false);
		
		if (bMenu.getBounds().contains(e.getX(), e.getY()))
			bMenu.setMouseOver(true);
		else
			audioOptions.mouseMoved(e);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (bMenu.getBounds().contains(e.getX(), e.getY())) {
			bMenu.setMousePressed(true);
			game.getAudioPlayer().playEffect(AudioPlayer.BUTTON_CLICKED);
		} else
			audioOptions.mousePressed(e);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (bMenu.getBounds().contains(e.getX(), e.getY()) && bMenu.isMouseOver())
			GameState.state = TITLE;
		else
			audioOptions.mouseReleased(e);
		
		bMenu.setMouseOver(false);		
		bMenu.setMousePressed(false);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}

}
