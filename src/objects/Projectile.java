package objects;

import static helpz.Constants.Object.CANNONBALL_SPEED;

import main.Game;

public class Projectile extends GameObject {

	private int dir;
	
	public Projectile(int x, int y, int dir) {
		super(x, y, 0);
		this.dir = dir;
		initHitbox(10, 10);
		yDrawOffset = xDrawOffset = (int) (2 * Game.SCALE);
	}
	
	public void update() {
		hitbox.x += dir * CANNONBALL_SPEED;
	}
	
}
