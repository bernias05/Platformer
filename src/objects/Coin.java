package objects;

import main.Game;

public class Coin extends GameObject {
	
	public Coin(int x, int y) {
		super(x, y, 11);
		initHitbox(16, 20);
		
		// offset to center the drawing in a tile
		xDrawOffset = (int) (-1 * Game.SCALE);
		yDrawOffset = (int) (-1 * Game.SCALE);
		
		// offset to center hitbox in a tile
		hitbox.x += (int) (8 * Game.SCALE);
		hitbox.y += (int) (6 * Game.SCALE);
	}

}
