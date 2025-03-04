package levels;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import entities.Crabby;
import entities.Pinkstar;
import entities.Shark;
import main.Game;
import objects.Boat;
import objects.Cannon;
import objects.Coin;
import objects.GameContainer;
import objects.Grass;
import objects.Potion;
import objects.Spike;
import objects.Tree;

import static helpz.Constants.Enemy.Type.*;
import static helpz.Constants.Object.*;
import static helpz.HelpMethods.ToRgbValue;

public class Level {
	
	private BufferedImage img;
	private Point spawnPoint;
	private Random random = new Random();
	
	private ArrayList<Crabby> crabbys = new ArrayList<>();
	private ArrayList<Pinkstar> pinkstars = new ArrayList<>();
	private ArrayList<Shark> sharks = new ArrayList<>();
	
	private ArrayList<Potion> potions = new ArrayList<>();
	private ArrayList<GameContainer> containers = new ArrayList<>();
	private ArrayList<Spike> spikes = new ArrayList<>();
	private ArrayList<Cannon> cannons = new ArrayList<>();
	private ArrayList<Tree> trees = new ArrayList<>();
	private ArrayList<Boat> boats = new ArrayList<>();
	private ArrayList<Grass> grass = new ArrayList<>();
	private ArrayList<Coin> coins = new ArrayList<>();
	
	private int[][] lvlData;
	private int maxPixelOffsetX;
	private int maxPixelOffsetY;
	private int lvlWidth;
	private int lvlHeight;
	
	private boolean completed;
	
	public Level(BufferedImage img) {
		this.img = img;
		lvlData = new int[img.getWidth()][img.getHeight()];
		lvlWidth = img.getWidth() * Game.TILE_SIZE;
		lvlHeight = img.getHeight() * Game.TILE_SIZE;
		loadAllLevelData();
		calcLvlOffset();
	}
	
	private void loadAllLevelData() {
		for (int x = 0; x < img.getWidth(); x++)
			for (int y = 0; y < img.getHeight(); y++) {
				Color color = new Color(img.getRGB(x, y));
				int red = color.getRed();
				int green = color.getGreen();
				int blue = color.getBlue();
				
				loadLvlData(red, x, y);
				
				if (green <= 2)
					loadEnemis(green, x * Game.TILE_SIZE, y * Game.TILE_SIZE);
				
				if (blue <= 11)
					loadObjects(blue, x * Game.TILE_SIZE, y * Game.TILE_SIZE);
			}
	}
	
	private void calcLvlOffset() {
		maxPixelOffsetX = (img.getWidth() - Game.TILES_IN_WIDTH) * Game.TILE_SIZE;
		maxPixelOffsetY = (img.getHeight() - Game.TILES_IN_HEIGHT) * Game.TILE_SIZE;
	}

	private void loadLvlData(int red, int x, int y) {
		if (red == 100)
			spawnPoint = new Point(x * Game.TILE_SIZE, y * Game.TILE_SIZE);
		else if (red >= 50)
			red = 11;
		
		lvlData[x][y] = red;
		
		if (canPlaceGrass(red)) {
			grass.add(new Grass(x * Game.TILE_SIZE, y * Game.TILE_SIZE, random.nextInt(3)));
		}
	}
	
	private boolean canPlaceGrass(int spriteIndex) {
		switch (spriteIndex) {
		case 0, 1, 2, 3, 30, 31, 33, 34, 35, 36, 37, 38, 39:
			return true;
		default:
			return false;
		}
	}
	
	private void loadEnemis(int green, int x, int y) {
		switch(green) {
		case CRABBY:
			crabbys.add(new Crabby(x, y));
			break;
		case PINKSTAR:
			pinkstars.add(new Pinkstar(x, y));
			break;
		case SHARK:
			sharks.add(new Shark(x, y));
			break;
		}
	}
	
	private void loadObjects(int blue, int x, int y) {
		switch(blue) {
		case BLUE_POTION:
			potions.add(new Potion(x, y, 0));
			break;
		case RED_POTION:
			potions.add(new Potion(x, y, blue));
			break;
		case BOX, BARREL:
			containers.add(new GameContainer(x, y, blue));
			break;
		case SPIKE:
			spikes.add(new Spike(x, y, blue));
			break;
		case CANNON_TO_LEFT, CANNON_TO_RIGHT:
			cannons.add(new Cannon(x, y, blue));
			break;
		case TREE, CURVED_TREE, CURVED_TO_LEFT_TREE:
			trees.add(new Tree(x, y, blue));
			break;
		case BOAT:
			boats.add(new Boat(x, y));
			break;
		case COIN:
			coins.add(new Coin(x, y));
		}
	}
	
	
	// edit section
	
	public void changeData(int xTile, int yTile, int value, int rgbChannel) {
		// x and y are already formatted as tilex/tileY
		// value is just the new value for the specified channel in format < 256
		// rgbChannel: 0 = red, 1 = green, 2 = blue, 3 = r & g & b channels
		
		switch (rgbChannel) {
		case 0 -> changeLvlData(xTile, yTile, value);
		case 1 -> changeEnemyData(xTile, yTile, value);
		case 2 -> changeObjectData(xTile, yTile, value);
		case 3 -> {	
				img.setRGB(xTile, yTile, ToRgbValue(11, 255,  255));
				changeLvlData(xTile, yTile, value);
			}
		}
		resetEnemiesAndObjects();
		loadEnemiesAndObjects();
	}
	
	private void changeEnemyData(int xTile, int yTile, int rgb) {
		if (!isGround(lvlData[xTile][yTile]))
			img.setRGB(xTile, yTile, ToRgbValue(11, rgb, 255));
	}

	private void changeObjectData(int xTile, int yTile, int rgb) {
		Color temp = new Color(img.getRGB(xTile, yTile));
		
		// special case: tree can only be placed on ground, all other objects can not
		if (rgb == CURVED_TREE || rgb == CURVED_TO_LEFT_TREE || rgb == TREE) {
			if (isGround(lvlData[xTile][yTile]) && lvlData[xTile][yTile] != 13) {
				img.setRGB(xTile, yTile, ToRgbValue(temp.getRed(), temp.getGreen(), rgb));
			}
			return;
		}
		
		if (isGround(lvlData[xTile][yTile]))
			return;
		
		// tests if an object can not be placed, then it returns, else it skips the switch and changes data
		switch (rgb) {
		case BOX, BARREL, SPIKE, CANNON_TO_LEFT, CANNON_TO_RIGHT:
			if (!(yTile == lvlData[0].length - 1) && !(isGround(lvlData[xTile][yTile + 1])))
				return;
			break;
		case BOAT:
			if (!(yTile == lvlData[0].length - 1) && !(isWaterSurface(lvlData[xTile][yTile + 1])))			// must be on water
				return;
			if (xTile == lvlData.length - 1 || new Color(img.getRGB(xTile + 1, yTile)).getBlue() == BOAT)	// bunch of checks, cause the boat is thicc     
				return;
			if (xTile > 0 && new Color(img.getRGB(xTile - 1, yTile)).getBlue() == BOAT)
				return;
			if (xTile < lvlData.length - 1 && isGround(lvlData[xTile + 1][yTile]))
				return;
			if (xTile < lvlData.length - 1 && yTile < lvlData[0].length - 1 && isGround(lvlData[xTile + 1][yTile + 1]))
				return;
			if (yTile == 0 || xTile < lvlData.length - 1 && new Color(img.getRGB(xTile + 1, yTile - 1)).getBlue() == BOAT
					|| xTile < lvlData.length - 1 && isGround(lvlData[xTile + 1][yTile - 1]))
				return;
		}
		img.setRGB(xTile, yTile, ToRgbValue(11, temp.getGreen(), rgb));
	}
	
	private boolean isGround(int value) {
		return value != 11 && value >= 0 && value <= 47;
	}
	
	private boolean isWaterSurface(int x) {
		return x == 49;
	}
	
	private void changeLvlData(int xTile, int yTile, int rgb) {
		// rgb: 0 -> ground; 48 -> water; 50 -> barrier; 100 -> spawnPoint; 255 -> void
		if (rgb != 100 && lvlData[xTile][yTile] == 100)
			return;
		// grass updates
		if (rgb == 0) {
			if (yTile < lvlData[0].length)
				changeGrass(xTile, yTile + 1, false);
			if (yTile > 0 && !isGround(lvlData[xTile][yTile - 1]))
				changeGrass(xTile, yTile, true);
		} else if (rgb == 255) {
			changeGrass(xTile, yTile, false);
			if (yTile < lvlData[0].length - 1 && isGround(lvlData[xTile][yTile + 1]))
				changeGrass(xTile, yTile + 1, true);
		} else if (rgb != 100) {
			changeGrass(xTile, yTile, false);
			if (yTile < lvlData[0].length)
				changeGrass(xTile, yTile, false);
		}
		
		// initial center tile gets updated
		if (rgb == 100) {
			// old tile gets reset
			lvlData[(int) (spawnPoint.getX() / Game.TILE_SIZE)][(int) (spawnPoint.getY() / Game.TILE_SIZE)] = 11;
			img.setRGB((int) (spawnPoint.getX() / Game.TILE_SIZE), (int) (spawnPoint.getY() / Game.TILE_SIZE), ToRgbValue(11, 255, 255));
			
			spawnPoint = new Point(xTile * Game.TILE_SIZE, yTile * Game.TILE_SIZE);
			// new spawn point set
			img.setRGB(xTile, yTile, ToRgbValue(100, 255, 255));
			lvlData[xTile][yTile] = 100;
			return;
		}
		
		if (rgb == 255) {
			img.setRGB(xTile, yTile, ToRgbValue(11, 255, 255));
			lvlData[xTile][yTile] = 11;
		} else {
			img.setRGB(xTile, yTile, ToRgbValue(findCorrectTile(xTile, yTile, rgb), 255, 255));
			lvlData[xTile][yTile] = new Color(img.getRGB(xTile, yTile)).getRed();
		}
		
		// now the 8 other tiles around the center get updated
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				int x = xTile - i;
				int y = yTile - j;
				if (x < 0 || y < 0 || x >= lvlData.length || y >= lvlData[0].length)
					continue;
				if (lvlData[x][y] == 11 || lvlData[x][y] > 49 || i == 0 && j == 0) {
					continue;
				}
				Color temp = new Color(img.getRGB(x, y));
				// fancy expression, but when block is water, water is übergeben, wenn Block ist Boden, Boden is overgiven
				img.setRGB(x, y, ToRgbValue(findCorrectTile(x, y, lvlData[x][y] >= 48 ? 48 : 0), temp.getGreen(), temp.getBlue()));
				lvlData[x][y] = new Color(img.getRGB(x, y)).getRed();
			}
		}
	}
	
	private void changeGrass(int xTile, int yTile, boolean place) {
		xTile *= Game.TILE_SIZE;
		yTile *= Game.TILE_SIZE;
		if (place) {
			for (Grass g : grass) {
				if (g.getX() == xTile && g.getY() == yTile) {
					return;
				}
			}
			grass.add(new Grass(xTile, yTile, random.nextInt(3)));
		} else {
			for (Grass g : grass) {
				if (g.getX() == xTile && g.getY() == yTile) {
					grass.remove(g);
					break;
				}
			}
		}
	}
	
	private int findCorrectTile(int x, int y, int rgb) {
		// rgb: 0 -> ground ; 48 -> water
		
		// water
		if (rgb == 48) {
			// when no water is above, it should be surface water
			if (y == 0 || lvlData[x][y - 1] <= 47) {
				rgb = 49;
			}
			// when water is placed above surface water, the surface water should be updated to normal water
			if (y < lvlData[0].length - 1 && lvlData[x][y + 1] == 49) {
				img.setRGB(x, y + 1, ToRgbValue(48, 255, 255));
				lvlData[x][y + 1] = 48;
			}
			return rgb;
		}
		
		// ground
		// if another groundBlock is around the block or it is on screen edge, the corresponding variable is true
		boolean leftUp   = true, up   = true, rightUp   = true, 
				left     = true,              right     = true, 
				leftDown = true, down = true, rightDown = true;
		
		if (x > 0) {
			left = lvlData[x - 1][y] != 11 && lvlData[x - 1][y] < 48;
			if (y > 0)
				leftUp = lvlData[x - 1][y - 1] != 11 && lvlData[x - 1][y - 1] < 48;
			if (y < lvlData[0].length - 1)
				leftDown = lvlData[x - 1][y + 1] != 11 && lvlData[x - 1][y + 1] < 48;
		}
		if (x < lvlData.length - 1) {
			right = lvlData[x + 1][y] != 11 && lvlData[x + 1][y] < 48;
			if (y > 0)
				rightUp = lvlData[x + 1][y - 1] != 11 && lvlData[x + 1][y - 1] < 48;
			if (y < lvlData[0].length - 1)
				rightDown = lvlData[x + 1][y + 1] != 11 && lvlData[x + 1][y + 1] < 48;
		}
		if (y > 0)
			up = lvlData[x][y - 1] != 11 && lvlData[x][y - 1] < 48;
		if (y < lvlData[0].length - 1)
			down = lvlData[x][y + 1] != 11 && lvlData[x][y + 1] < 48;
		
		// now the fun part, checking for the right tile
		
		// nothing around
		if (!left && !right && !up && !down)
			return 39;
		
		// nothing on left and right
		if (!left && !right &&  up && !down)
			return 27;
		if (!left && !right && !up &&  down)
			return 3;
		if (!left && !right &&  up &&  down)
			return 15;
		
		// nothing above and below
		if (!up && !down && !left &&  right)
			return 36;
		if (!up && !down &&  left && !right)
			return 38;
		if (!up && !down &&  left &&  right)
			return 37;
		
		// corner blocks
		if (!left && !up && right && down)
			return rightDown ? 0 : 34;
		if (!up && !right && down && left)
			return leftDown ? 2 : 35;
		if (!right && !down && left && up)
			return leftUp ? 26 : 47;
		if (!down && !left && up && right)
			return rightUp ? 24 : 46;
		
		// nothing above
		if (!up && right && down && left) {
			if (!leftDown && !rightDown)
				return 33;
			if (!leftDown &&  rightDown)
				return 31;
			if ( leftDown && !rightDown)
				return 30;
			return 1;
		}
		
		// nothing on right
		if (!right && down && left && up) {
			if (!leftUp && !leftDown)
				return 45;
			if (!leftUp &&  leftDown)
				return 41;
			if ( leftUp && !leftDown)
				return 29;
			return 14;
		}
		
		// nothing below
		if (!down && left && up && right) {
			if (!rightUp && !leftUp)
				return 44;
			if (!rightUp &&  leftUp)
				return 42;
			if ( rightUp && !leftUp)
				return 43;
			return 25;
		}
		
		// nothing on left
		if (!left && up && right && down) {
			if (!rightDown && !rightUp)
				return 32;
			if (!rightDown &&  rightUp)
				return 28;
			if ( rightDown && !rightUp)
				return 40;
			return 12;
		}
		
		// up, right, down, left are always true in the code below
		
		if (!leftUp && !rightUp && !rightDown)
			return leftDown ? 9 : 10;
		if (!leftUp && !rightUp &&  rightDown)
			return leftDown ? 6 : 8;
		
		if (!leftUp &&  rightUp && !rightDown)
			return leftDown ? 22 : 20;
		if (!leftUp &&  rightUp &&  rightDown)
			return leftDown ? 17 : 18;
		
		if ( leftUp && !rightUp && !rightDown)
			return leftDown ? 7 : 21;
		if ( leftUp && !rightUp &&  rightDown)
			return leftDown ? 16 : 23;
		
		if ( leftUp &&  rightUp && !rightDown)
			return leftDown ? 4 : 19;
			
		return leftDown ? 13 : 5;
	}

	private void resetEnemiesAndObjects() {
		crabbys.clear();
		pinkstars.clear();
		sharks.clear();
		potions.clear();
		containers.clear();
		spikes.clear();
		cannons.clear();
		trees.clear();
		boats.clear();
		coins.clear();
	}

	private void loadEnemiesAndObjects() {
		for (int x = 0; x < img.getWidth(); x++)
			for (int y = 0; y < img.getHeight(); y++) {
				Color color = new Color(img.getRGB(x, y));
				int green = color.getGreen();
				int blue = color.getBlue();
				
				if (green <= 2)
					loadEnemis(green, x * Game.TILE_SIZE, y * Game.TILE_SIZE);
				if (blue <= 11)
					loadObjects(blue, x * Game.TILE_SIZE, y * Game.TILE_SIZE);
		}
	}
	
	public int getTileIndexValue(int x, int y) {
		return lvlData[x][y];
	}
	
	public int[][] getLvlData() {
		return lvlData;
	}
	
	public int getMaxOffsetX() {
		return maxPixelOffsetX;
	}
	
	public int getMaxOffsetY() {
		return maxPixelOffsetY;
	}
	
	public int getLvlWidth() {
		return lvlWidth;
	}
	
	public int getLvlHeight() {
		return lvlHeight;
	}
	
	public BufferedImage getImg() {
		return img;
	}
	
	public void setImg(BufferedImage img) {
		this.img = img;
	}
	
	public Point getSpawnPoint() {
		return spawnPoint;
	}
	
	public ArrayList<Crabby> getCrabbys() {
		return crabbys;
	}
	
	public ArrayList<Pinkstar> getPinkstars() {
		return pinkstars;
	}
	
	public ArrayList<Shark> getSharks() {
		return sharks;
	}
	
	public ArrayList<Potion> getPotions() {
		return potions;
	}
	
	public ArrayList<GameContainer> getContainers() {
		return containers;
	}
	
	public ArrayList<Spike> getSpikes() {
		return spikes;
	}
	
	public ArrayList<Cannon> getCannons() {
		return cannons;
	}
	
	public ArrayList<Tree> getTrees() {
		return trees;
	}
	
	public ArrayList<Boat> getBoats() {
		return boats;
	}
	
	public ArrayList<Grass> getGrass() {
		return grass;
	}
	
	public ArrayList<Coin> getCoins() {
		return coins;
	}
	
}
