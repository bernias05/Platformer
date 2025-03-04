package objects;

import main.Game;

public class Potion extends GameObject {
	
	public Potion(int x, int y, int type) {
		super(x, y, type);
		initHitbox(7, 14);
		xDrawOffset = (int) (3 * Game.SCALE);
		yDrawOffset = (int) (2 * Game.SCALE);
		hitbox.y += 2;
		hitbox.x += (Game.DEFAULT_TILE_SIZE - 7) / 2 * Game.SCALE;
	}

}