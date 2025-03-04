package objects;

import entities.Player;
import main.Game;

import static helpz.Constants.Object.*;
import static helpz.HelpMethods.IsSightClear;

public class Cannon extends GameObject {
	
	private int tileY;
	private int range = 10 * Game.TILE_SIZE;

	public Cannon(int x, int y, int type) {
		super(x, y, type);
		tileY = y / Game.TILE_SIZE;
		framesForAniUpdate = 18;
		
		xDrawOffset = (int) (-5 * Game.SCALE);
		yDrawOffset = (int) (8 * Game.SCALE);
		
		initHitbox(30, 24);
		
		hitbox.x += 1 * Game.SCALE;
		hitbox.y += yDrawOffset;
	}
	
	public void update(ObjectManager objectManager, Player player, int[][] lvlData) {
		if (needAnimation) {
			updateAnimationTick();
			if (ready) {
				shoot(objectManager);
				ready = false;
			}
		} else {
			if (tileY == (int) (player.getHitbox().y / Game.TILE_SIZE))
				if (isPlayerInRange(player))
					if (isPlayerInSight(player))
						if (IsSightClear(tileY, hitbox, player.getHitbox(), lvlData))
							needAnimation = true;
		}
	}
	
	private boolean isPlayerInRange(Player player) {
		float playerLeftEdge = player.getHitbox().x;
		float playerRightEdge = player.getHitbox().x + player.getHitbox().width;
		
		switch (type) {
		case 5:
			if (Math.abs(x - playerRightEdge) <= range || Math.abs(x - playerLeftEdge) <= range)
				return true;
		case 6:
			if (Math.abs(playerLeftEdge - (x + hitbox.width)) <= range || Math.abs(playerRightEdge - (x + hitbox.width)) <= range)
				return true;
		}
		return false;
	}
	
	private boolean isPlayerInSight(Player player) {
		if (type == CANNON_TO_LEFT && player.getHitbox().x < x)
			return true;
		if (type == CANNON_TO_RIGHT && player.getHitbox().x > x)
			return true;
		
		return false;
	}
	
	private void shoot(ObjectManager objectManager) {
		needAnimation = true;

		int x = (int) (hitbox.x - 5 * Game.SCALE);
		int y = (int) (hitbox.y + 5 * Game.SCALE);
		int dir = -1;
		
		if (type == 6) {
			x = (int) (hitbox.x + hitbox.width / 5 * 4);
			dir = 1;
		}
		objectManager.getCannonballs().add(new Projectile(x, y, dir));
	}
	
	public int getTileY() {
		return tileY;
	}

}
