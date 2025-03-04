package entities;

import main.Game;


import java.awt.geom.Rectangle2D;

import static helpz.Constants.Enemy.*;
import static helpz.Constants.Enemy.Action.*;
import static helpz.Constants.Enemy.Size.*;
import static helpz.Constants.Enemy.Type.*;

import static helpz.Constants.Direction.*;

public class Crabby extends Enemy {

	private int attackHitboxOffset = (int) (30 * Game.SCALE);
	
	public Crabby(float x, float y) {
		super(CRABBY, (int) x, (int) y);
		initHitbox(22, 19);
		initAttackHitbox();
	}
	
	private void initAttackHitbox() {
		attackHitbox = new Rectangle2D.Float(hitbox.x - attackHitboxOffset, hitbox.y, 82 * Game.SCALE, (19 * Game.SCALE));
	}
	
	public void update(Player player, int[][] lvlData) {
		updateBehavior(player, lvlData);
		updateAnimationTick();
		updateAttackHitbox();
	}
	
	private void updateBehavior(Player player, int[][] lvlData) {
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
					moveSpeed = 2 * GetMoveSpeed(enemyType);
					
					if (isPlayerInAttackRange(player))
						changeActionTo(ATTACK);
				} else {
					seeing = false;
					moveSpeed = GetMoveSpeed(enemyType);
				}
				run(lvlData);
			}
		}
	}
	
	private void updateAttackHitbox() {
		attackHitbox.x = hitbox.x - attackHitboxOffset;
		attackHitbox.y = hitbox.y;		
	}
	
	public int flipX() {
		if (walkDir == RIGHT)
			return CRABBY_WIDTH;
		else
			return 0;
	}

}
