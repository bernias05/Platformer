package entities;

import static helpz.Constants.Enemy.Type.SHARK;

import java.awt.geom.Rectangle2D;

import main.Game;

import static helpz.Constants.Direction.RIGHT;
import static helpz.Constants.Enemy.Action.*;
import static helpz.Constants.Enemy.Size.*;

public class Shark extends Enemy {

	private float attackHitboxOffsetX = -4 * Game.SCALE;
	private float attackHitboxOffsetY = 3 * Game.SCALE;
	private float attackHitboxWidth = 25 * Game.SCALE;
	
	public Shark(int x, int y) {
		super(SHARK, x, y);
		initHitbox(18, 20);
		initAttackHitbox();
	}
	
	private void initAttackHitbox() {
		attackHitbox = new Rectangle2D.Float(hitbox.x - attackHitboxOffsetX, hitbox.y - attackHitboxOffsetY, attackHitboxWidth, 16 * Game.SCALE);
	}

	public void update(Player player, int[][] lvlData) {
		updateBehaviour(player, lvlData);
		updateAnimationTick();
		updateAttackHitbox();
	}
	
	private void updateAttackHitbox() {
		if (flipW() == -1)
			attackHitbox.x = hitbox.x - attackHitboxOffsetX;
		else
			attackHitbox.x = hitbox.x + attackHitboxOffsetX - (attackHitboxWidth - hitbox.width);
		
		attackHitbox.y = hitbox.y - attackHitboxOffsetY;
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
					turnTowardsPlayer(player);
					if (!seeing)
						player.getPlaying().getDialogueManager().addExclamationMark((int) hitbox.x, (int) (hitbox.y - hitbox.height));
					seeing = true;
					
					if (isPlayerInAttackRange(player))
						changeActionTo(ATTACK);
				} else
					seeing = false;
				run(lvlData);
			} else if (action == ATTACK && aniIndex >= 3 && aniIndex < 5)
				bite(player, lvlData);
		}
	}
	
	private void bite(Player player, int[][] lvlData) {
		float xSpeed = 5 * moveSpeed;
		if (flipW() == 1)
			xSpeed *= -1;
		
		if (attackHitbox.intersects(player.getHitbox())) {
			player.hurt(dmg, this);;
			action = RUNNING;
			aniTick = 0;
			aniIndex = 0;
		}
		
		if (isWalkable(lvlData, xSpeed))
			hitbox.x += xSpeed;
	}

	public int flipX() {
		if (walkDir == RIGHT)
			return SHARK_WIDTH;
		else
			return 0;
	}

}
