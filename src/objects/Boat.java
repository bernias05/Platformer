package objects;

import static helpz.Constants.Object.*;

import main.Game;

public class Boat extends GameObject {
	
	public Boat(int x, int y) {
		super(x, y, BOAT);
		initHitbox(78, 15);
		yDrawOffset = (int) (50 * Game.SCALE);
		hitbox.y += 38;
	}

}
