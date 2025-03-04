package gameStates;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import audio.AudioPlayer;
import helpz.Constants;
import helpz.LoadSave;
import levels.Level;
import main.Game;
import ui.EditButton;
import ui.Toolbar;
import ui.UrmButton;

import static gameStates.GameState.*;
import static main.Game.TILE_SIZE;
import static helpz.Constants.LevelElementType.*;
import static helpz.Constants.UI.UrmButton.B_URM_SIZE;
import static helpz.LoadSave.SaveLevel;

public class Editing extends State implements StateMethods{

	private Level lvl;
	private Toolbar toolbar;
	private BufferedImage background;
	private UrmButton bSave;
	
	private boolean showToolbar = true;
	private boolean drawing = false;
	
	private int tileX, tileY;
	
	private int xLvlOffset;
	private int maxOffsetX;
	
	private int yLvlOffset;
	private int maxOffsetY;
	
	public Editing(Game game) {
		super(game);
		lvl = game.getPlaying().getLevelManager().getCurrentLevel();
		background = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BG);
		maxOffsetX = game.getPlaying().getLevelManager().getCurrentLevel().getMaxOffsetX();
		maxOffsetY = game.getPlaying().getLevelManager().getCurrentLevel().getMaxOffsetY();
		yLvlOffset = maxOffsetY;
		toolbar = new Toolbar(this);
		bSave = new UrmButton((int) (20 * Game.SCALE), (int) (20 * Game.SCALE), B_URM_SIZE, B_URM_SIZE, 4);
	}
	
	public void setLevel(Level lvl) {
		this.lvl = lvl;
		game.getPlaying().getObjectManager().loadObjects(lvl);
		game.getPlaying().getEnemyManager().loadEnemies(lvl);
	}
	
	@Override
	public void update() {
		toolbar.update();
		bSave.update();
	}
	
	@Override
	public void render(Graphics g) {
		g.drawImage(background, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
		
		game.getPlaying().getLevelManager().draw(g, xLvlOffset, yLvlOffset);
		game.getPlaying().getLevelManager().drawWater(g, xLvlOffset, yLvlOffset);
		game.getPlaying().getObjectManager().draw(g, xLvlOffset, yLvlOffset);
		game.getPlaying().getEnemyManager().draw(g, xLvlOffset, yLvlOffset);
		
		drawGrid(g);
		drawHighlight(g);
		drawSpawnPoint(g);
		if (showToolbar) {
			toolbar.draw(g);
			bSave.draw(g);
		}
		g.setFont(new Font("Kalam", Font.PLAIN, 30));
		g.drawString("Press Space to toggle toolbar", (int) (100 * Game.SCALE), (int) (35 * Game.SCALE));
	}
	
	private void drawGrid(Graphics g) {
		g.setColor(Color.LIGHT_GRAY);
		
		for (int i = 1; i <= 2 * maxOffsetX / TILE_SIZE; i++)
			g.drawLine(i * TILE_SIZE - xLvlOffset, 0, 
					   i * TILE_SIZE - xLvlOffset, maxOffsetY);
		
		for (int i = 1; i <= 2 * maxOffsetY / TILE_SIZE; i++)
			g.drawLine(0, 		   i * TILE_SIZE - yLvlOffset, 
					   maxOffsetX, i * TILE_SIZE - yLvlOffset);
	}
	
	private void drawHighlight(Graphics g) {
		g.setColor(Color.black);
		g.drawRect(tileX, tileY, TILE_SIZE, TILE_SIZE);
	}
	
	private void drawSpawnPoint(Graphics g) {
		int x = game.getPlaying().getLevelManager().getCurrentLevel().getSpawnPoint().x;
		int y = game.getPlaying().getLevelManager().getCurrentLevel().getSpawnPoint().y;
		
		g.drawImage(toolbar.getSpawnPointImg(), x - xLvlOffset, y - yLvlOffset, Game.TILE_SIZE, Game.TILE_SIZE, null);
	}
	
	private void changeData(MouseEvent e) {
		if (e.getX() >= Game.GAME_WIDTH || e.getX() < 0 || e.getY() >= Game.GAME_HEIGHT || e.getY() < 0)
			return;
		
		if (game.getPlaying().getLevelManager().getCurrentLevel().getCoins().size() == 3 && toolbar.getSelectedButtonIndex() == 10)
			return;
		
		// Typ wird bestimmt, xTile und yTile weitergegeben
		int xTile = (e.getX() + xLvlOffset) / TILE_SIZE;
		int yTile = (e.getY() + yLvlOffset) / TILE_SIZE;
		int channel = 0;
		int rgb = 0;
		
		EditButton temp = toolbar.getButtons()[toolbar.getSelectedButtonIndex()];
		switch (toolbar.getSelectedButtonIndex()) {
		case AIR: 		  channel = 3; rgb = 255; break;
		
		case GROUND: 	  channel = 0; rgb = 0;   break;
		case WATER: 	  channel = 0; rgb = 48;  break;
		case SPAWN_POINT: channel = 0; rgb = 100; break;
		
		case ENEMY:		  channel = 1; rgb = temp.getRotationIndex();     break;
		
		case SPIKE: 	  channel = 2; rgb = Constants.Object.SPIKE;      break;
		case BOAT: 		  channel = 2; rgb = Constants.Object.BOAT;	      break;
		case COIN:		  channel = 2; rgb = Constants.Object.COIN;		  break;
		case POTION: 	  channel = 2; rgb = 0 + temp.getRotationIndex(); break;
		case CONTAINER:   channel = 2; rgb = 2 + temp.getRotationIndex(); break;
		case TREE: 		  channel = 2; rgb = 7 + temp.getRotationIndex(); break;
		case CANNON:	  channel = 2; rgb = 5 + temp.getRotationIndex(); break;
		};
		
		game.getPlaying().getLevelManager().getCurrentLevel().changeData(xTile, yTile, rgb, channel);
		
		game.getPlaying().getObjectManager().loadObjects(lvl);
	}
	
	private int tileAlign(int value) {
		return value / TILE_SIZE * TILE_SIZE;
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		bSave.setMouseOver(false);
		tileX = tileAlign(e.getX());
		tileY = tileAlign(e.getY());
		if (showToolbar) {
			if (toolbar.getBounds().contains(e.getX(), e.getY()))
				toolbar.mouseMoved(e);
			if (bSave.getBounds().contains(e.getX(), e.getY()))
				bSave.setMouseOver(true);
		}
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		if (showToolbar && toolbar.getBounds().contains(e.getX(), e.getY()))
				toolbar.mousePressed(e);
		else if (showToolbar && bSave.getBounds().contains(e.getX(), e.getY())) {
				bSave.setMousePressed(true);
				game.getAudioPlayer().playEffect(AudioPlayer.BUTTON_CLICKED);
		} else {
			if (toolbar.isButtonSelected()) {
				drawing = true;
				changeData(e);
			}
		}
	}
	
	public void mouseDragged(MouseEvent e) {
		if (!(showToolbar && toolbar.getBounds().contains(e.getX(), e.getY()))) {
			if (drawing) {
				if (tileX != tileAlign(e.getX()) || tileY != tileAlign(e.getY())) {
					changeData(e);
					tileX = tileAlign(e.getX());
					tileY = tileAlign(e.getY());
				}
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		drawing = false;
		if (showToolbar && toolbar.getBounds().contains(e.getX(), e.getY()))
			toolbar.mouseReleased(e);
		if (showToolbar && bSave.getBounds().contains(e.getX(), e.getY()) && bSave.isMousePressed()) {
			SaveLevel(game.getPlaying().getLevelManager().getLvlIndex(), lvl.getImg());
		}
		
		bSave.setMousePressed(false);
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		toolbar.keyPressed(e);
		int offset = TILE_SIZE;
		if (e.getKeyCode() == KeyEvent.VK_W) {
			yLvlOffset -= offset;
			if (yLvlOffset < 0)
				yLvlOffset = 0;
		} else if (e.getKeyCode() == KeyEvent.VK_A) {
			xLvlOffset -= offset;
			if (xLvlOffset < 0)
				xLvlOffset = 0;
		}
			
		if (e.getKeyCode() == KeyEvent.VK_S) {
			yLvlOffset += offset;
			if (yLvlOffset > maxOffsetY)
				yLvlOffset = maxOffsetY;
		} else if (e.getKeyCode() == KeyEvent.VK_D) {
			xLvlOffset += offset;
			if (xLvlOffset > maxOffsetX)
				xLvlOffset = maxOffsetX;
		}
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			setGamestate(LEVEL_SELECT);
			game.getPlaying().getLevelManager().getLevelInfo().refreshData();
		}
		if (e.getKeyCode() == KeyEvent.VK_SPACE)
			showToolbar = !showToolbar;
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		
	}
	
	public Game getGame() {
		return game;
	}

}
