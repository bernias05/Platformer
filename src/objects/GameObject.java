package objects;

import static helpz.Constants.Object.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import main.Game;

public abstract class GameObject {

	protected int x, y, type;
	protected int aniTick, aniIndex;
	protected int framesForAniUpdate = 15;
	protected int xDrawOffset, yDrawOffset;
	
	protected Rectangle2D.Float hitbox;
	protected boolean needAnimation, ready, active = true;
	protected boolean destroyed = false;
	
	public GameObject(int x, int y, int type) {
		this.x = x;
		this.y = y;
		this.type = type;
		needAnimation = NeedAnimation(type);
	}
	
	protected void updateAnimationTick() {
		aniTick++;
		if (aniTick >= framesForAniUpdate) {
			aniIndex++;
			aniTick = 0;
			
			if (aniIndex == 4 && (type == 5 || type == 6))
				ready = true;
			
			if (aniIndex >= GetSpriteAmount(type)) {
				if (type == BARREL || type == BOX) {
					needAnimation = false;
					destroyed = true;
					active = false;
				} else if (type == CANNON_TO_LEFT || type == CANNON_TO_RIGHT) {
					needAnimation = false;
					aniIndex = 0;
				} else
					aniIndex = 0;
			}
		}
	}
	
	public void reset() {
		aniIndex = 0;
		aniTick = 0;
		active = true;
		needAnimation = NeedAnimation(type);
	}
	
	protected void initHitbox(int width, int height) {
		hitbox = new Rectangle2D.Float(x, y, (int) (width * Game.SCALE), (int) (height * Game.SCALE));
	}
	
	protected void drawHitbox(Graphics g, int xLvlOffset, int yLvlOffset) {
		g.setColor(Color.pink);
		g.drawRect((int) hitbox.x - xLvlOffset, (int) hitbox.y - yLvlOffset, (int) hitbox.width, (int) hitbox.height);
	}
	
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public int getType() {
		return type;
	}
	
	public int getAniIndex() {
		return aniIndex;
	}
	
	public int getxDrawOffset() {
		return xDrawOffset;
	}
	
	public int getyDrawOffset() {
		return yDrawOffset;
	}
	
	public boolean isActive() {
		return active;
	}
	
	public void setActive(boolean active) {  
		this.active = active;
	}
	
	public void setNeedAnimation(boolean needAnimation) {
		this.needAnimation = needAnimation;
	}
	
	public Rectangle2D.Float getHitbox() {
		return hitbox;
	}
	
}
