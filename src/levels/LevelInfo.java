package levels;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import helpz.LoadSave;
import main.Game;

import static helpz.Constants.Object.Size.COIN_BORDER_WIDTH;

public class LevelInfo {
	
	private LevelManager levelManager;
	private BufferedImage coinBorder, coinImg;
	private BufferedImage missionInfo, checkbox;
	
	private int levelNum;
	private int currCoinsCollected = 0;
	
	// keeps track of number of collected coins and enemys/containers in all levels 
	// and whether they were once all killed or destroyed
	private int[] containerCount, enemyCount;
	private int containersDestroyed = 0, enemiesKilled = 0;
	
	private int[] coinsCollected;
	private boolean[] containerComplete, enemyComplete;

	public LevelInfo(LevelManager levelManager) {
		this.levelManager = levelManager;
		init();
	}
	
	private void init() {
		levelNum = levelManager.getLevels().size();
		coinsCollected = new int[levelNum];
		containerCount = new int[levelNum];
		enemyCount = new int[levelNum];
		containerComplete = new boolean[levelNum];
		enemyComplete = new boolean[levelNum];
		
		coinBorder = LoadSave.GetSpriteAtlas(LoadSave.OBJECT_COIN_BORDER);
		coinImg = LoadSave.GetSpriteAtlas(LoadSave.OBJECT_COIN_BORDER_FILLED);
		missionInfo = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_MISSION_INFO_BG);
		checkbox = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_MISSION_CHECKBOX);

		refreshData();
	}
	
	public void draw(Graphics g) {
		for (int i = 0; i < 3; i++)
			g.drawImage(coinBorder, (int) (Game.GAME_WIDTH / 2 - 2 * COIN_BORDER_WIDTH + i * COIN_BORDER_WIDTH * 1.5), 
					(int) (20 * Game.SCALE), COIN_BORDER_WIDTH, COIN_BORDER_WIDTH, null);
		
		for (int i = 0; i < currCoinsCollected; i++)
			g.drawImage(coinImg, (int) (Game.GAME_WIDTH / 2 - 2 * COIN_BORDER_WIDTH + i * COIN_BORDER_WIDTH * 1.5), 
					(int) (20 * Game.SCALE), COIN_BORDER_WIDTH, COIN_BORDER_WIDTH, null);
			
		for (int i = 0; i < 2; i++) {
			g.drawImage(missionInfo, 
					Game.GAME_WIDTH - (int) (180 * Game.SCALE), i * (int) (30 * Game.SCALE), 
					(int) (180 * Game.SCALE), (int) (30 * Game.SCALE), null);
			g.drawImage(checkbox, 
					Game.GAME_WIDTH - (int) (210 * Game.SCALE), i * (int) (30 * Game.SCALE), 
					(int) (30 * Game.SCALE), (int) (30 * Game.SCALE), null);
		}
		
		g.setColor(Color.BLACK);
		g.setFont(new Font("Kalam", Font.PLAIN, 28));
		g.drawString(enemiesKilled + "/" + enemyCount[levelManager.getLvlIndex()] + "   Kill enemies" , 
				Game.GAME_WIDTH - (int) (173 * Game.SCALE), (int) (20 * Game.SCALE));
		g.drawString(containersDestroyed + "/" + containerCount[levelManager.getLvlIndex()] + "   Destroy containers" , 
				Game.GAME_WIDTH - (int) (173 * Game.SCALE), (int) (50 * Game.SCALE));
		
		
		g.setColor(Color.green);
		if (enemiesKilled >= enemyCount[levelManager.getLvlIndex()])
			g.fillRect(Game.GAME_WIDTH - (int) (204 * Game.SCALE), (int) (6 * Game.SCALE), 
					(int) (18 * Game.SCALE), (int) (18 * Game.SCALE));
		if (containersDestroyed >= containerCount[levelManager.getLvlIndex()])
			g.fillRect(Game.GAME_WIDTH - (int) (204 * Game.SCALE), (int) (36 * Game.SCALE), 
					(int) (18 * Game.SCALE), (int) (18 * Game.SCALE));
	}
	
	public void refreshData() {
		for (int i = 0; i < levelNum; i++) {
			containerCount[i] = levelManager.getLevels().get(i).getContainers().size();
			enemyCount[i] = levelManager.getLevels().get(i).getCrabbys().size()
					+ levelManager.getLevels().get(i).getPinkstars().size()
					+ levelManager.getLevels().get(i).getSharks().size();
		}
	}
	
	public void reset() {
		coinsCollected[levelManager.getLvlIndex()] = currCoinsCollected;
		currCoinsCollected = 0;
		enemiesKilled = 0;
		containersDestroyed = 0;
	}
	
	public void coinCollected() {
		if (currCoinsCollected < 3)
			currCoinsCollected++;
	}
	
	public void enemyKilled() {
		enemiesKilled++;
		if (enemiesKilled >= enemyCount[levelManager.getLvlIndex()])
			enemyComplete[levelManager.getLvlIndex()] = true;
	}
	
	public void containerDestroyed() {
		containersDestroyed++;
		if (containersDestroyed >= containerCount[levelManager.getLvlIndex()])
			containerComplete[levelManager.getLvlIndex()] = true;
	}

	public int[] getCoinsCollected() {
		return coinsCollected;
	}

	public void setCoinsCollected(int[] coinsCollected) {
		this.coinsCollected = coinsCollected;
	}

	public int[] getContainerCount() {
		return containerCount;
	}

	public void setContainerCount(int[] containerCount) {
		this.containerCount = containerCount;
	}

	public int[] getEnemyCount() {
		return enemyCount;
	}

	public void setEnemyCount(int[] enemyCount) {
		this.enemyCount = enemyCount;
	}

	public boolean[] getContainerComplete() {
		return containerComplete;
	}

	public void setContainerComplete(boolean[] containerComplete) {
		this.containerComplete = containerComplete;
	}

	public boolean[] getEnemyComplete() {
		return enemyComplete;
	}

	public void setEnemyComplete(boolean[] enemyComplete) {
		this.enemyComplete = enemyComplete;
	}
	
}
