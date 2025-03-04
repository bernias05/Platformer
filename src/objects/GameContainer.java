package objects;

import static helpz.Constants.Object.*;

import main.Game;

public class GameContainer extends GameObject{

	public GameContainer(int x, int y, int type) {
		super(x, y, type);
		init();
	}

	private void init() {
		if (type == BOX) {
			initHitbox(25, 18);
			xDrawOffset = (int) (7 * Game.SCALE);
			yDrawOffset = (int) (12 * Game.SCALE);
			hitbox.x += (Game.DEFAULT_TILE_SIZE - 25) / 2 * Game.SCALE;
		} else {
			initHitbox(23, 25);
			xDrawOffset = (int) (8 * Game.SCALE);
			yDrawOffset = (int) (5 * Game.SCALE);
			hitbox.x += (Game.DEFAULT_TILE_SIZE - 23) / 2 * Game.SCALE;
		}
		hitbox.y += yDrawOffset + 2 * Game.SCALE;
	}
	
	public void update() {
		if (needAnimation)
			updateAnimationTick();
	}
	
	public boolean isDestroyed() {
		return destroyed;
	}
	
	public void setDestroyed(boolean destroyed) {
		this.destroyed = destroyed;
	}
	
	public String toString() {
		String out = "";
		if (type == 2)
			out += "Box    | X-Tile: ";
		else
			out += "Barrel | X-Tile: ";
		out += x / Game.TILE_SIZE + "  | Y-Tile: " + y / Game.TILE_SIZE;
		return out;
	}

}