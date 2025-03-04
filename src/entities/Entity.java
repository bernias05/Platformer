package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import main.Game;

public abstract class Entity {

	protected Rectangle2D.Float hitbox;
	protected Rectangle2D.Float attackHitbox;
	
	protected int x, y;
	protected int action = 0;
	protected int aniTick, aniIndex;
	protected int maxHP, currentHP;
	protected int dmg;
	
	protected float moveSpeed;
	protected float airSpeed = 0;
	protected float waterSpeedX = 0, waterSpeedY = 0;
	
	protected boolean inAir;
	protected boolean hit;
	
	protected int hitDir;
	
	protected float hitSpeedX;
	
	public Entity(int x, int y) {
		this.x = x;
		this.y = y;
	}

	protected void drawHitbox(Graphics g, int xLvlOffset, int yLvlOffset) {
		g.setColor(Color.pink);
		g.drawRect((int) hitbox.x - xLvlOffset, (int) hitbox.y - yLvlOffset, (int) hitbox.width, (int) hitbox.height);
	}
	
	public void drawAttackHitbox(Graphics g, int lvlOffset, int yLvlOffset) {
		g.setColor(Color.red);
		g.drawRect((int) attackHitbox.x - lvlOffset, (int) attackHitbox.y - yLvlOffset, (int) attackHitbox.width, (int) attackHitbox.height);
	}
	
	protected void initHitbox(int width, int height) {
		hitbox = new Rectangle2D.Float(x, y, width * Game.SCALE, height * Game.SCALE);
	}
	
	protected void resetPosHPAni() {
		hitbox.x = x;
		hitbox.y = y;
		currentHP = maxHP;
		aniIndex = 0;
		aniTick = 0;
		hit = false;
	}
	
	public Rectangle2D.Float getHitbox() {
		return hitbox;
	}
	
	public Rectangle2D.Float getAttackHitbox() {
		return attackHitbox;
	}
	
	public int getAniIndex() {
		return aniIndex;
	}
	
	public int getAniTick() {
		return aniTick;
	}
	
	public int getCurrentHP() {
		return currentHP;
	}
	
	public int getMaxHP() {
		return maxHP;
	}
	
	public boolean isHit() {
		return hit;
	}
	
	public void setHit(boolean hit) {
		this.hit = hit;
	}
	
	public void setMoveSpeed(float moveSpeed) {
		this.moveSpeed = moveSpeed;
	}
	
	public int getAction() {
		return action;
	}
	
}
