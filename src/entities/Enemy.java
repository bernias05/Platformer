package entities;

import main.Game;

import static helpz.Constants.Enemy.*;
import static helpz.HelpMethods.*;

import java.awt.Color;
import java.awt.Graphics;

import static helpz.Constants.Direction.*;
import static helpz.Constants.Enemy.Action.*;
import static helpz.Constants.Enemy.Type.*;

public abstract class Enemy extends Entity {
		
	protected int enemyType;
	protected int walkDir = RIGHT;
	protected int tileY;
	
	protected boolean firstUpdate = true;
	protected boolean alive = true;
	protected boolean seeing;
	protected float attackRange;
	
	public Enemy(int enemyType, int x, int y) {
		super(x, y);
		this.enemyType = enemyType;
		moveSpeed = GetMoveSpeed(enemyType);
		dmg = GetDmg(enemyType);
		attackRange = GetAttackRange(enemyType);
		maxHP = GetMaxHP(enemyType);
		currentHP = maxHP;
		action = RUNNING;
	}
	
	protected void firstUpdateCheck(int[][] lvlData) {
		if (!IsEntityOnFloor(hitbox, lvlData))
			inAir = true;
		firstUpdate = false;
	}
	
	protected void fall(int[][] lvlData) {
		if (CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
			hitbox.y += airSpeed;
			airSpeed += Game.GRAVITY;
		} else {
			hitbox.y = GetYPosAtTile(hitbox, airSpeed);
			tileY = (int) (hitbox.y / Game.TILE_SIZE);
			inAir = false;
			airSpeed = 0;
		}
	}
	
	protected void run(int[][] lvlData) {
		float xSpeed = 0;
		
		if (walkDir == LEFT)
			xSpeed = -moveSpeed;
		else if (walkDir == RIGHT)
			xSpeed = moveSpeed;
		
		if (isWalkable(lvlData, xSpeed)) {
			hitbox.x += xSpeed;
			return;
		}
		reverseWalkDir();
	}
	
	protected void hit(int[][] lvlData) {
		if (CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
			hitbox.y += airSpeed;
			airSpeed += Game.GRAVITY;
		} else {
			hitbox.y = GetYPosAtTile(hitbox, airSpeed);
			tileY = (int) (hitbox.y / Game.TILE_SIZE);
			hit = false;
			inAir = false;
			airSpeed = 0;
		}
		if (CanMoveHere(hitbox.x + hitSpeedX, hitbox.y, hitbox.width, hitbox.height, lvlData))
			hitbox.x += hitSpeedX;
		else {
			hitbox.x = GetEntityXPosAtWall(hitbox, hitSpeedX);
		}
	}
	
	protected boolean isWalkable(int[][] lvlData, float moveSpeed) {
		if (CanMoveHere(hitbox.x + moveSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData))
			if (IsFloor(walkDir, hitbox, moveSpeed, lvlData))
				return true;
		return false;
	}
	
	protected boolean canSeePlayer(Player player, int[][] lvlData) {
		int playerTileY = (int) (player.getHitbox().y / Game.TILE_SIZE);
		
		if (playerTileY == tileY)
			if (isPlayerInSightRange(player))
				if (IsPathClear(tileY, hitbox, player.hitbox, lvlData))
					return true;
		
		return false;
	}
	
	private boolean isPlayerInSightRange(Player player) {
		float sightRange = 4 * Game.TILE_SIZE;
		float dist = Math.abs(player.getHitbox().x - hitbox.x);
		
		return dist <= sightRange;
	}
	
	protected boolean isPlayerInAttackRange(Player player) {
		float dist = Math.abs(player.getHitbox().x - hitbox.x);
		
		return dist <= attackRange;
	}
	
	protected void turnTowardsPlayer(Player player) {
		if (player.getHitbox().x > hitbox.x)
			walkDir = RIGHT;
		else
			walkDir = LEFT;
	}
	
	protected void updateAnimationTick() {
		aniTick++;
		
		if (aniTick >= Game.ANI_SPEED) {
			aniIndex++;
			aniTick = 0;
			
			if (aniIndex >= GetSpriteAmount(enemyType, action)) {
				hit = false;
				if (action == DEAD)
					alive = false;
				
				switch (enemyType) {
					case CRABBY:
						aniIndex = 0;
						switch (action) {
							case ATTACK, HIT -> action = RUNNING;
						}
						break;
						
					case PINKSTAR:
						if (action == ATTACK)
							aniIndex = 5;
						else
							aniIndex = 0;
						
						switch (action) {
							case HIT, IDLE -> action = RUNNING;
						}
						break;
						
					case SHARK:
						aniIndex = 0;
						switch (action) {
							case ATTACK, HIT -> action = RUNNING;
						}
					}
			}
		}
	}
	
	public void drawHpBar(Graphics g, int xLvlOffset, int yLvlOffset) {
		int barX = (int) (hitbox.x - 4 * Game.SCALE) - xLvlOffset;
		int barY = (int) (hitbox.y - 10 * Game.SCALE) - yLvlOffset;
		int barW = (int) (hitbox.width + 8 * Game.SCALE);
		int barH = (int) (3 * Game.SCALE);
		
		g.setColor(Color.white);
		g.fillRect(barX, barY, barW, barH);

		g.setColor(Color.red);
		if (currentHP == maxHP)
			g.fillRect(barX, barY, barW, barH);
		else
			g.fillRect(barX, barY, barW / maxHP * currentHP, barH);
		
		g.setColor(Color.black);
		g.drawRect(barX, barY, barW, barH);
	}
	
	// !!!!! this method is for player attacks, the one below is public and for spike damage
	
	public void hurt(int dmg, Player player) {
		currentHP -= dmg;
		if (currentHP <= 0) {
			changeActionTo(DEAD);
			currentHP = 0;
		} else {
			changeActionTo(HIT);
			if (player != null) {
				hit = true;
				inAir = true;
				airSpeed = -1.5f * Game.SCALE;
				if (hitbox.x > player.getHitbox().x) {
					hitDir = RIGHT;
					hitSpeedX = moveSpeed;
				} else {
					hitDir = LEFT;
					hitSpeedX = -moveSpeed;
				}
			}
		}
	}
	
	protected void changeActionTo(int newAction) {
		action = newAction;
		aniTick = 0;
		aniIndex = 0;
	}
	
	private void reverseWalkDir() {
		if (walkDir == LEFT)
			walkDir = RIGHT;
		else
			walkDir = LEFT;
	}
	
	protected int flipW() {
		if (walkDir == RIGHT)
			return -1;
		else
			return 1;
	}
	
	public void setAlive(boolean alive) {
		this.alive = alive;
	}
	
	public int getEnemyAction() {
		return action;
	}
	
	public boolean isAlive() {
		return alive;
	}
	
	public int getDmg() {
		return dmg;
	}
	
	protected void reset() {
		resetPosHPAni();
		inAir = true;
		alive = true;
		action = RUNNING;
		airSpeed = 0;
	}

}
