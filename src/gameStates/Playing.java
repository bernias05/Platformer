package gameStates;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import java.util.Random;

import effects.DialogueManager;
import effects.RainManager;
import entities.EnemyManager;
import entities.Player;

import helpz.LoadSave;
import levels.Level;
import levels.LevelManager;

import main.Game;
import objects.ObjectManager;
import ui.AudioOptions;
import ui.OverlayGameOver;
import ui.OverlayLevelComplete;
import ui.OverlayPause;

import static helpz.Constants.Environment.*;

public class Playing extends State implements StateMethods{

	private Random random;
	private Player player;
	
	private LevelManager levelManager;
	private EnemyManager enemyManager;
	private ObjectManager objectManager;
	private RainManager rainManager;
	private DialogueManager dialogueManager;
	
	private OverlayPause overlayPause;
	private OverlayGameOver overlayGameOver;
	private OverlayLevelComplete overlayLevelComplete;
	
	private AudioOptions audioOptions;

	private BufferedImage background, bigCloud, smallCloud;
	
	private boolean paused;
	private boolean levelComplete;
	private boolean playerDying, gameOver;
	
	private int xLvlOffset;
	private int leftBorder 	= (int) (0.3 * Game.GAME_WIDTH);
	private int rightBorder = (int) (0.7 * Game.GAME_WIDTH);
	private int maxPixelOffsetX;
	
	private int yLvlOffset;
	private int upBorder =   (int) (0.4 * Game.GAME_HEIGHT);
	private int downBorder = (int) (0.6 * Game.GAME_HEIGHT);
	private int maxPixelOffsetY;
	
	private int[] smallCloudPosX;
	private int[] smallCloudPosY;
	
	public Playing(Game game, AudioOptions audioOptions) {
		super(game);
		this.audioOptions = audioOptions;
		
		initClasses();
		loadBackgroundImgs();
		
		xLvlOffset = (int) player.getHitbox().x - Game.GAME_WIDTH / 2;
		yLvlOffset = (int) player.getHitbox().y - Game.GAME_HEIGHT / 2;
		
		maxPixelOffsetX = levelManager.getCurrentLevel().getMaxOffsetX();
		maxPixelOffsetY = levelManager.getCurrentLevel().getMaxOffsetY();
		
		enemyManager .loadEnemies(levelManager.getCurrentLevel());
		objectManager.loadObjects(levelManager.getCurrentLevel());
	}
	
	private void initClasses() {
		random  = new Random();
		objectManager   = new ObjectManager(this);
		levelManager    = new LevelManager(this);
		enemyManager    = new EnemyManager(this);
		rainManager     = new RainManager(this);
		dialogueManager = new DialogueManager(this);
		
		Point spawn = levelManager.getCurrentLevel().getSpawnPoint();
		player = new Player(this, (float) spawn.getX(), (float) spawn.getY());
		player.loadLvlData(levelManager.getCurrentLevel().getLvlData());
		
		overlayPause = new OverlayPause(this, audioOptions);
		overlayGameOver = new OverlayGameOver(this);
		overlayLevelComplete = new OverlayLevelComplete(this);
	}
	
	private void loadBackgroundImgs() {
		background = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BG);
		bigCloud = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BIG_CLOUD);
		smallCloud = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_SMALL_CLOUD);
		
		smallCloudPosX = new int[13];
		for (int i = 0; i < smallCloudPosX.length; i++)
			smallCloudPosX[i] = (int) (50 * Game.SCALE) + random.nextInt((int) (50 * Game.SCALE));
		
		smallCloudPosY = new int[13];
		for (int i = 0; i < smallCloudPosY.length; i++)
			smallCloudPosY[i] = (int) (100 * Game.SCALE) + random.nextInt((int) (100 * Game.SCALE));
	}
	
	public void loadLevel(int lvlIndex) {
		reset();
		levelManager.setLevel(lvlIndex);
		Level level = levelManager.getCurrentLevel();
		enemyManager.loadEnemies(level);
		objectManager.loadObjects(level);
		
		player.loadLvlData(level.getLvlData());
		player.getHitbox().x = (float) level.getSpawnPoint().getX();
		player.getHitbox().y = (float) level.getSpawnPoint().getY();
		
		maxPixelOffsetX = level.getMaxOffsetX();
		game.getAudioPlayer().setLvlSong(levelManager.getLvlIndex());
	}
	
	@Override
	public void update() {
		if (gameOver)
			overlayGameOver.update();
		else if (playerDying)
			player.update();
		else if (paused)
			overlayPause.update();
		else if (levelComplete)
			overlayLevelComplete.update();
		else {
			rainManager.update();
			levelManager.update();
			enemyManager.update(player, levelManager.getCurrentLevel().getLvlData());
			objectManager.update(player, levelManager.getCurrentLevel().getLvlData());
			player.update();
			dialogueManager.update();
			checkIfCloseToXBorder();
			checkIfCloseToYBorder();
		}
	}

	private void checkIfCloseToXBorder() {
		int playerX = (int) player.getHitbox().x;
		int xDiffFromLeftScreenEdge = playerX - xLvlOffset;
		
		if (xDiffFromLeftScreenEdge > rightBorder)
			xLvlOffset += xDiffFromLeftScreenEdge - rightBorder;
		else if (xDiffFromLeftScreenEdge < leftBorder)
			xLvlOffset += xDiffFromLeftScreenEdge - leftBorder;
		
		if (xLvlOffset > maxPixelOffsetX)
			xLvlOffset = maxPixelOffsetX;
		else if (xLvlOffset < 0)
			xLvlOffset = 0;
	}
	
	private void checkIfCloseToYBorder() {
		int playerY = (int) player.getHitbox().y;
		int yDiffFromUpperScreenEdge = playerY - yLvlOffset;
		
		if (yDiffFromUpperScreenEdge > downBorder)
			yLvlOffset += yDiffFromUpperScreenEdge - downBorder;
		else if (yDiffFromUpperScreenEdge < upBorder)
			yLvlOffset += yDiffFromUpperScreenEdge - upBorder;
		
		if (yLvlOffset > maxPixelOffsetY)
			yLvlOffset = maxPixelOffsetY;
		else if (yLvlOffset < 0)
			yLvlOffset = 0;
	}
	
	@Override
	public void render(Graphics g) {
		g.drawImage(background, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
		drawClouds(g);
		
		rainManager.draw(g, xLvlOffset, yLvlOffset);
		levelManager.draw(g, xLvlOffset, yLvlOffset);
		objectManager.draw(g, xLvlOffset, yLvlOffset);
		levelManager.drawWater(g, xLvlOffset, yLvlOffset);
		enemyManager.draw(g, xLvlOffset, yLvlOffset);
		dialogueManager.draw(g, xLvlOffset, yLvlOffset);
		levelManager.drawLevelInfo(g);
		
		playerDyingCheck(g);
		player.draw(g, xLvlOffset, yLvlOffset);
		
		hurtCheck(g);
		pausedCheck(g);
		
		if (levelComplete)
			overlayLevelComplete.draw(g);
		else if (gameOver)
			overlayGameOver.draw(g);
	}

	private void drawClouds(Graphics g) {
		for (int i = 0; i < 7; i++)
			g.drawImage(bigCloud, i * BIG_CLOUD_WIDTH - xLvlOffset / 4, (int) (200 * Game.SCALE), BIG_CLOUD_WIDTH, BIG_CLOUD_HEIGHT, null);
		
		for (int i = 0; i < smallCloudPosY.length; i++)
			g.drawImage(smallCloud, smallCloudPosX[i] + i * SMALL_CLOUD_WIDTH * 3 - xLvlOffset / 2, smallCloudPosY[i] - yLvlOffset / 2, SMALL_CLOUD_WIDTH, SMALL_CLOUD_HEIGHT, null);
	}
	
	private void reset() {
		gameOver = false;
		paused = false;
		levelComplete = false;
		
		player.reset();
		enemyManager.reset();
		objectManager.reset();
		rainManager.reset();
		levelManager.getLevelInfo().reset();
		
		xLvlOffset = (int) player.getHitbox().x - Game.GAME_WIDTH / 2;
		yLvlOffset = (int) player.getHitbox().y - Game.GAME_HEIGHT / 2;
	}
	
	private void playerDyingCheck(Graphics g) {
		if (playerDying) {
			g.setColor(new Color(0, 0, 0, 150));
			g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
		}
	}
	
	private void hurtCheck(Graphics g) {
		if (player.isHurt()) {
			g.setColor(new Color(50, 0, 0, 50));
			g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
		}
	}

	private void pausedCheck(Graphics g) {
		if (paused) {
			g.setColor(new Color(0, 0, 0, 150));
			g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
			overlayPause.draw(g);
		}
	}
	
	public void gameOver() {
		gameOver = true;
		paused = false;
	}
	
	public void levelCompleted() {
		levelComplete = true;
		game.getAudioPlayer().lvlCompleted();
		
		// checks if the completed level is the highest unlocked level
		if (levelManager.getLvlIndex() == game.getLevelSelect().getLvlsUnlocked() - 1)
			game.getLevelSelect().unlockNextLevel();
	}

	public void windowFocusLost() {
		player.resetDirBooleans();
	}

	public void mouseDragged(MouseEvent e) {
		if (paused)
			audioOptions.mouseDragged(e);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (gameOver)
			overlayGameOver.mouseMoved(e);
		else if (paused)
			overlayPause.mouseMoved(e);
		else if (levelComplete)
			overlayLevelComplete.mouseMoved(e);
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		if (gameOver)
			overlayGameOver.mousePressed(e);
		else if (paused)
			overlayPause.mousePressed(e);
		else if (levelComplete)
			overlayLevelComplete.mousePressed(e);
		else if (e.getButton() == MouseEvent.BUTTON1 && !player.isPowerAttackActive()
//				&& player.isCdOver()
				) 
				player.setAttacking(true);
		else if (e.getButton() == MouseEvent.BUTTON3) 
				player.powerAttack();
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		if (gameOver)
			overlayGameOver.mouseReleased(e);
		else if (paused)
			overlayPause.mouseReleased(e);
		else if (levelComplete)
			overlayLevelComplete.mouseReleased(e);
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		if (gameOver || playerDying)
			return;
		else {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_A:
				if (!player.isPowerAttackActive())
					player.setLeft(true);
				break;
			case KeyEvent.VK_D:
				if (!player.isPowerAttackActive())
					player.setRight(true);
				break;
			case KeyEvent.VK_SPACE, KeyEvent.VK_W:
				player.setUp(true);
				player.setJumping(true);
				player.setLanding(true);
				break;
			case KeyEvent.VK_S:
				player.setDown(true);
				break;
				
			case KeyEvent.VK_ESCAPE:
				paused = !paused;
				break;
			}
		}
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		if (!gameOver) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_A:
				player.setLeft(false);
				break;
			case KeyEvent.VK_D:
				player.setRight(false);
				break;
			case KeyEvent.VK_SPACE, KeyEvent.VK_W:
				player.setUp(false);
				player.setJumping(false);
				player.setLanding(false);
				break;
			case KeyEvent.VK_S:
				player.setDown(false);
				break;
			}
		}
	}
	
	public void setPaused(boolean paused) {
		this.paused = paused;
	}
	
	public void setPlayerDying(boolean playerDying) {
		this.playerDying = playerDying;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public EnemyManager getEnemyManager() {
		return enemyManager;
	}
	
	public LevelManager getLevelManager() {
		return levelManager;
	}
	
	public ObjectManager getObjectManager() {
		return objectManager;
	}
	
	public DialogueManager getDialogueManager() {
		return dialogueManager;
	}

}

