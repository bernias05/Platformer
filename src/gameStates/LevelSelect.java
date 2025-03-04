package gameStates;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import audio.AudioPlayer;
import helpz.LoadSave;
import main.Game;
import ui.LevelButton;
import ui.OverlayLevelSelected;
import ui.UrmButton;

import static helpz.Constants.UI.UrmButton.*;
import static main.Game.SCALE;

public class LevelSelect extends State implements StateMethods{

	private OverlayLevelSelected ols;
	private BufferedImage background;
	private UrmButton bHome;
	private LevelButton[] levelButtons;
	private int lvlsUnlocked = 8;
	
	private boolean levelSelected;
	
	public LevelSelect(Game game) {
		super(game);
		loadBg();
		init();
	}
	
	private void init() {
		ols = new OverlayLevelSelected(this);
		bHome = new UrmButton((int) (10 * Game.SCALE), (int) (10 * Game.SCALE), B_URM_SIZE, B_URM_SIZE, 2);
		levelButtons = new LevelButton[9];
		
		int size = (int) (40 * SCALE);
		
		levelButtons[0] = new LevelButton(0, (int) (55  * SCALE), (int) (200 * SCALE), size, size, true);
		levelButtons[1] = new LevelButton(1, (int) (130 * SCALE), (int) (130 * SCALE), size, size, true);
		levelButtons[2] = new LevelButton(2, (int) (220 * SCALE), (int) (50  * SCALE), size, size, true);
		levelButtons[3] = new LevelButton(3, (int) (310 * SCALE), (int) (150 * SCALE), size, size, true);
		levelButtons[4] = new LevelButton(4, (int) (370 * SCALE), (int) (290 * SCALE), size, size, true);
		levelButtons[5] = new LevelButton(5, (int) (495 * SCALE), (int) (365 * SCALE), size, size, true);
		levelButtons[6] = new LevelButton(6, (int) (600 * SCALE), (int) (240 * SCALE), size, size, true);
		levelButtons[7] = new LevelButton(7, (int) (620 * SCALE), (int) (120 * SCALE), size, size, true);
		levelButtons[8] = new LevelButton(8, (int) (750 * SCALE), (int) (90  * SCALE), size, size, true);
	}
	
	private void loadBg() {
		background = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_MENU_BG);
	}
	
	public void unlockNextLevel() {
		levelButtons[lvlsUnlocked].unlock();
		lvlsUnlocked++;
	}
	
	@Override
	public void update() {
		if (levelSelected)
			ols.update();
		bHome.update();
		for (LevelButton b : levelButtons)
			b.update();
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(background, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
		bHome.draw(g);
		for (LevelButton b : levelButtons)
			b.draw(g);
		if (levelSelected) {
			g.setColor(new Color(0, 0, 0, 100));
			g.fillRect(0,  0,  Game.GAME_WIDTH, Game.GAME_HEIGHT);
			ols.draw(g);
		}
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		if (levelSelected)
			ols.mouseMoved(e);
		else {
			bHome.setMouseOver(false);
			if (bHome.getBounds().contains(e.getX(), e.getY()))
				bHome.setMouseOver(true);

			for (LevelButton b : levelButtons) {
				if (b.isUnlocked()) {
					b.setMouseOver(false);
					if (b.getBounds().contains(e.getX(), e.getY()))
						b.setMouseOver(true);
				}
			}
		}
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		if (levelSelected)
			ols.mousePressed(e);
		else {
			if (bHome.getBounds().contains(e.getX(), e.getY())) {
				bHome.setMousePressed(true);
				game.getAudioPlayer().playEffect(AudioPlayer.BUTTON_CLICKED);
			}
			for (LevelButton b : levelButtons) {
				if (b.isUnlocked())
					if (b.getBounds().contains(e.getX(), e.getY())) {
						b.setMousePressed(true);
						game.getAudioPlayer().playEffect(AudioPlayer.BUTTON_CLICKED);
					}
			}
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		if (levelSelected)
			ols.mouseReleased(e);
		else {
			if (bHome.getBounds().contains(e.getX(), e.getY()))
				setGamestate(GameState.TITLE);
			bHome.setMousePressed(false);
			bHome.setMouseOver(false);
			
			for (LevelButton b : levelButtons) {
				if (b.isUnlocked())
					if (b.getBounds().contains(e.getX(), e.getY()) && b.isMouseOver()) {
						ols.setLevel(b.getLvlIndex());
						levelSelected = true;
					}
				b.setMousePressed(false);
				b.setMouseOver(false);
			}
		}
	}
	
	public void mouseDragged(MouseEvent e) {
		
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
	}
	
	public OverlayLevelSelected getOls() {
		return ols;
	}
	
	public void setLevelSelected(boolean levelSelected) {
		this.levelSelected = levelSelected;
	}
	
	public int getLvlsUnlocked() {
		return lvlsUnlocked;
	}

}
