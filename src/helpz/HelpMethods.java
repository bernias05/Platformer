package helpz;

import java.awt.geom.Rectangle2D;

import main.Game;
import objects.Projectile;
import static helpz.Constants.Direction.*;

public class HelpMethods {

	public static float GetEntityXPosAtWall(Rectangle2D.Float hitbox, float xSpeed) {
		int currentTile = (int) (hitbox.x / Game.TILE_SIZE);
		
		if (xSpeed > 0) {
			// Right
			int tilePosX = currentTile * Game.TILE_SIZE;
			int xOffset = (int)(Game.TILE_SIZE - hitbox.width);
			return tilePosX + xOffset - 1;
		} else 
			// Left
			return currentTile * Game.TILE_SIZE ;
	}
	
	public static float GetYPosAtTile(Rectangle2D.Float hitbox, float airSpeed) {
		int currentTileY;
		
		if (airSpeed > 0) {
			// Falling
			currentTileY = (int) ((hitbox.y + hitbox.height) / Game.TILE_SIZE);
			int pixelPosY = currentTileY * Game.TILE_SIZE;
			int yOffset = (int)(Game.TILE_SIZE - hitbox.height);
			return pixelPosY + yOffset - 1;
		} else {
			// Jumping
			currentTileY = (int) (hitbox.y / Game.TILE_SIZE);
			return currentTileY * Game.TILE_SIZE ;
		}
	}
	
	public static boolean CanMoveHere(float x, float y, float width, float height, int[][] lvlData) {
		if (!IsSolid(x, y, lvlData))
			if (!IsSolid(x + width, y + height, lvlData))
				if (!IsSolid(x, y + height, lvlData))
					if (!IsSolid(x + width, y, lvlData))
						return true;
		return false;
	}
	
	private static boolean IsSolid(float x, float y, int[][] lvlData) {
		if (x < 0 || x >= lvlData.length * Game.TILE_SIZE)
			return true;
		if (y < 0 || y >= lvlData[0].length * Game.TILE_SIZE)
			return true;
		
		float xIndex = x / Game.TILE_SIZE;
		float yIndex = y / Game.TILE_SIZE;
		int value = lvlData[(int) xIndex][(int) yIndex];
		
		if (value < 48 && value >= 0 && value != 11)
			return true;
		
		return false;
	}
	
	public static boolean IsEntityOnFloor(Rectangle2D.Float hitbox, int[][] lvlData) {
		if (!IsSolid(hitbox.x, hitbox.y + hitbox.height + 1, lvlData))
			if (!IsSolid(hitbox.x + hitbox.width, hitbox.y + hitbox.height + 1, lvlData))
				return false;
		return true;
	}
	
	public static boolean IsFloor(int dir, Rectangle2D.Float hitbox, float xSpeed, int[][] lvlData) {
		if (dir == LEFT)
			return IsSolid(hitbox.x + xSpeed, hitbox.y + hitbox.height + 1, lvlData);
		else
			return IsSolid(hitbox.x + hitbox.width + xSpeed, hitbox.y + hitbox.height + 1, lvlData);	
	}
	
	public static boolean IsSightClear(int tileY, Rectangle2D.Float hitbox, Rectangle2D.Float playerHitbox, int[][] lvlData) {
		int smallerX;
		
		int enemyTileX = (int) (hitbox.x / Game.TILE_SIZE);
		int playerTileX = (int) (playerHitbox.x / Game.TILE_SIZE);
		
		if (enemyTileX == playerTileX)
			return true;
		
		else if (enemyTileX > playerTileX)
			smallerX = playerTileX;
		else
			smallerX = enemyTileX;
		
		for (int i = 0; i < Math.abs(playerTileX - enemyTileX); i++) {
			if (IsSolid(Game.TILE_SIZE * (smallerX + i), Game.TILE_SIZE * tileY, lvlData))
				return false;
		}
		return true;
	}
	
	public static boolean IsPathClear(int tileY, Rectangle2D.Float hitbox, Rectangle2D.Float playerHitbox, int[][] lvlData) {
		int smallerX;
		
		int enemyTileX = (int) (hitbox.x / Game.TILE_SIZE);
		int playerTileX = (int) (playerHitbox.x / Game.TILE_SIZE);
		
		if (enemyTileX == playerTileX)
			return true;  
		else if (enemyTileX > playerTileX) {
			smallerX = playerTileX;
			smallerX = (int) ((playerHitbox.x + playerHitbox.width) / Game.TILE_SIZE);
		}
		else
			smallerX = enemyTileX;
		
		for (int i = 0; i < Math.abs(playerTileX - enemyTileX); i++) {
			if (IsSolid(Game.TILE_SIZE * (smallerX + i), Game.TILE_SIZE * tileY, lvlData))
				return false;
			else if (!IsSolid(Game.TILE_SIZE * (smallerX + i), Game.TILE_SIZE * (tileY + 1), lvlData))
				return false;
		}
		return true;
	}
	
	public static boolean IsHitting(Rectangle2D.Float hitbox, Rectangle2D.Float attackHitbox) {
		return attackHitbox.intersects(hitbox);
	}
	
	public static boolean IsProjectileHittingWall(Projectile p, int[][] lvlData) {
		return IsSolid(p.getHitbox().x + p.getHitbox().width / 2, p.getHitbox().y + p.getHitbox().height / 2, lvlData);
	}
	
	public static int ToRgbValue(int r, int g, int b) {
		return r * 256 * 256 + g * 256 + b;
	}
	
	public static float ApproximateSpeed(float speed, float maxSpeed, float acceleration) {
		// accerleration should always be a positive parameter
		if (speed == maxSpeed)
			return maxSpeed;
		else if (speed < maxSpeed) {
			speed += acceleration;
			return speed < maxSpeed ? speed : maxSpeed;
		} else {
			speed -= acceleration;
			return speed > maxSpeed ? speed : maxSpeed;
		}
	}
	
}
