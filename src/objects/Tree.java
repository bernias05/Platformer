package objects;

import main.Game;
import java.util.Random;

import static helpz.Constants.Object.*;

public class Tree extends GameObject {

	private Random random = new Random();
	
	private int treeOffsetX = (int) (3 * Game.SCALE);
	private int treeOffsetY = (int) (70 * Game.SCALE);
	private int curvedTreeOffsetX = (int) (-13 * Game.SCALE);
	private int curvedToLeftTreeOffsetX = (int) (-20 * Game.SCALE);
	private int curvedTreeOffsetY = (int) (30 * Game.SCALE);
	
	public Tree(int x, int y, int type) {
		super(x, y, type);
		
		if (type == TREE) {
			this.x = x - treeOffsetX;
			this.y = y - treeOffsetY;
		} else if (type == CURVED_TREE) {
			this.x = x - curvedTreeOffsetX;
			this.y = y - curvedTreeOffsetY;
		} else {
			this.x = x - curvedToLeftTreeOffsetX;
			this.y = y - curvedTreeOffsetY;
		}
	}
	
	public void update() {
		aniTick++;
		if (aniTick > Game.ANI_SPEED * 1.5) {
			aniTick = 0;
			aniIndex++;
			if (aniIndex > 3)
				aniIndex = 0;
		}
	}

}
