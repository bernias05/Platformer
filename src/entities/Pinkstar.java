package entities;

import java.awt.geom.Rectangle2D;

import main.Game;

import static helpz.Constants.Direction.*;

import static helpz.Constants.Enemy.*;
import static helpz.Constants.Enemy.Type.PINKSTAR;
import static helpz.Constants.Enemy.Size.*;
import static helpz.Constants.Enemy.Action.*;

public class Pinkstar extends Enemy {
	
	private int attackHitboxOffset = (int) (3 * Game.SCALE);
	
	private int rollAttackTick;

	public Pinkstar(int x, int y) {
		super(PINKSTAR, x, y);
		initHitbox(19, 21);
		initAttackHitbox();
	}
	
	private void initAttackHitbox() {
		attackHitbox = new Rectangle2D.Float(hitbox.x - attackHitboxOffset, hitbox.y, 25 * Game.SCALE, 21 * Game.SCALE);
	} 
	
	public void update(Player player, int[][] lvlData) {
		updateBehaviour(player, lvlData);
		updateAnimationTick();
		updateAttackHitbox();
	}
	
	private void updateBehaviour(Player player, int[][] lvlData) {
		if (firstUpdate)
			firstUpdateCheck(lvlData);
		
		if (hit) {
			hit(lvlData);
			return;
		}
		
		if (inAir)
			fall(lvlData);
		else {
			if (action == RUNNING) {
				if (canSeePlayer(player, lvlData)) {
					if (!seeing)
						player.getPlaying().getDialogueManager().addExclamationMark((int) hitbox.x, (int) (hitbox.y - hitbox.height));
					seeing = true;
					turnTowardsPlayer(player);
					
					if (isPlayerInAttackRange(player)) {
						changeActionTo(ATTACK);
					}
				} else {
					seeing = false;
				}
				run(lvlData);
			} else if (action == ATTACK) {
				if (aniIndex >= 3)
					roll(player, lvlData);
			}
		}
	}
	
	private void updateAttackHitbox() {
		attackHitbox.x = hitbox.x - attackHitboxOffset;
		attackHitbox.y = hitbox.y;		
	}
	
	private void roll(Player player, int[][] lvlData) {
		rollAttackTick++;
		float xSpeed = moveSpeed * 5;
		if (flipW() == 1)
			xSpeed *= -1;
		
		if (attackHitbox.intersects(player.getHitbox())) {
			player.hurt(GetDmg(enemyType), this);
			stopRollAttack(player, false);
		}
			
		if (isWalkable(lvlData, xSpeed))
			hitbox.x += xSpeed;
		else
			stopRollAttack(player, true);
		
		if (rollAttackTick >= 3 * 24)
			stopRollAttack(player, true);
	}
	
	private void stopRollAttack(Player player, boolean confused) {
		action = IDLE;
		rollAttackTick = 0;
		aniTick = 0;
		aniIndex = 0;
		
		if (confused)
			player.getPlaying().getDialogueManager().addQuestionMark((int) hitbox.x, (int) (hitbox.y - hitbox.height));
	}
	
	public int flipX() {
		if (walkDir == RIGHT)
			return PINKSTAR_WIDTH;
		else
			return 0;
	}

}
