package helpz;

import main.Game;

public class Constants {
	
	public static class LevelElementType {
		public static final int AIR = 0;
		public static final int GROUND = 1;
		public static final int WATER = 2;
		public static final int SPIKE = 3;
		public static final int BOAT = 4;
		public static final int CONTAINER = 5;
		public static final int POTION = 6;
		public static final int CANNON = 7;
		public static final int ENEMY = 8;
		public static final int TREE = 9;
		public static final int COIN = 10;
		public static final int SPAWN_POINT = 11;
		public static final int BARRIER = 12;
	}
	
	public static class Object {
		public static final int BLUE_POTION = 0;
		public static final int RED_POTION = 1;
		public static final int BOX = 2;
		public static final int BARREL = 3;
		public static final int SPIKE = 4;
		public static final int CANNON_TO_LEFT = 5;
		public static final int CANNON_TO_RIGHT = 6;
		public static final int CURVED_TREE = 7;
		public static final int CURVED_TO_LEFT_TREE = 8;
		public static final int TREE = 9;
		public static final int BOAT = 10;
		public static final int COIN = 11;
		
		public static final int RED_POTION_VALUE = 30;
		public static final int BLUE_POTION_VALUE = 30;
		public static final int SPIKE_DAMAGE = 10;
		public static final int CANNONBALL_DMG = 20;
		public static final float CANNONBALL_SPEED = 3f;
		
		public static class Size {
			public static final int CONTAINER_WIDTH_DEFAULT = 40;
			public static final int CONTAINER_HEIGHT_DEFAULT = 30;
			public static final int CONTAINER_WIDTH = (int) (CONTAINER_WIDTH_DEFAULT * Game.SCALE);
			public static final int CONTAINER_HEIGHT = (int) (CONTAINER_HEIGHT_DEFAULT * Game.SCALE);
			
			public static final int POTION_WIDTH_DEFAULT = 12;
			public static final int POTION_HEIGHT_DEFAULT = 22;
			public static final int POTION_WIDTH = (int) (POTION_WIDTH_DEFAULT * Game.SCALE);
			public static final int	POTION_HEIGHT = (int) (POTION_HEIGHT_DEFAULT * Game.SCALE);
			
			public static final int SPIKE_WIDTH_DEFAULT = 32;
			public static final int SPIKE_HEIGHT_DEFAULT = 32;
			public static final int SPIKE_WIDTH = (int) (SPIKE_WIDTH_DEFAULT * Game.SCALE);
			public static final int	SPIKE_HEIGHT = (int) (SPIKE_HEIGHT_DEFAULT * Game.SCALE);
			
			public static final int CANNON_WIDTH_DEFAULT = 40;
			public static final int CANNON_HEIGHT_DEFAULT = 26;
			public static final int CANNON_WIDTH = (int) (CANNON_WIDTH_DEFAULT * Game.SCALE);
			public static final int	CANNON_HEIGHT = (int) (CANNON_HEIGHT_DEFAULT * Game.SCALE);
			
			public static final int CANNONBALL_WIDTH_DEFAULT = 15;
			public static final int CANNONBALL_WIDTH = (int) (CANNONBALL_WIDTH_DEFAULT * Game.SCALE);
			
			public static final int TREE_WIDTH_DEFAULT = 39;
			public static final int TREE_HEIGHT_DEFAULT = 92;
			public static final int TREE_WIDTH = (int) (TREE_WIDTH_DEFAULT * Game.SCALE);
			public static final int	TREE_HEIGHT = (int) (TREE_HEIGHT_DEFAULT * Game.SCALE);
			
			public static final int CURVED_TREE_WIDTH_DEFAULT = 62;
			public static final int CURVED_TREE_HEIGHT_DEFAULT = 54;
			public static final int CURVED_TREE_WIDTH = (int) (CURVED_TREE_WIDTH_DEFAULT * Game.SCALE);
			public static final int	CURVED_TREE_HEIGHT = (int) (CURVED_TREE_HEIGHT_DEFAULT * Game.SCALE);
			
			public static final int BOAT_WIDTH_DEFAULT = 78;
			public static final int BOAT_HEIGHT_DEFAULT = 72;
			public static final int BOAT_WIDTH = (int) (BOAT_WIDTH_DEFAULT * Game.SCALE);
			public static final int	BOAT_HEIGHT = (int) (BOAT_HEIGHT_DEFAULT * Game.SCALE);	
			
			public static final int GRASS_WIDTH_DEFAULT = 32;
			public static final int GRASS_HEIGHT_DEFAULT = 32;
			public static final int GRASS_WIDTH = (int) (GRASS_WIDTH_DEFAULT * Game.SCALE);
			public static final int	GRASS_HEIGHT = (int) (GRASS_HEIGHT_DEFAULT * Game.SCALE);
			
			public static final int PUNCTUATION_MARK_WIDTH_DEFAULT = 14;
			public static final int PUNCTUATION_MARK_HEIGHT_DEFAULT = 12;
			public static final int PUNCTUATION_MARK_WIDTH = (int) (PUNCTUATION_MARK_WIDTH_DEFAULT * Game.SCALE);
			public static final int	PUNCTUATION_MARK_HEIGHT = (int) (PUNCTUATION_MARK_HEIGHT_DEFAULT * Game.SCALE);
			
			public static final int COIN_WIDTH_DEFAULT = 15;
			public static final int COIN_WIDTH = (int) (2 * COIN_WIDTH_DEFAULT * Game.SCALE);
			public static final int COIN_BORDER_WIDTH = (int) (2 * (COIN_WIDTH_DEFAULT + 2) * Game.SCALE);
			
		}
		
		public static int GetSpriteAmount(int objType) {
			switch (objType) {
			case BARREL, BOX:
				return 8;
			case BLUE_POTION, RED_POTION, CANNON_TO_LEFT, CANNON_TO_RIGHT:
				return 7;
			case COIN:
				return 6;
			case TREE, CURVED_TREE, CURVED_TO_LEFT_TREE, BOAT:
				return 4;
			}
			return 0;
		}
		
		public static boolean NeedAnimation(int objType) {
			switch (objType) {
			case BLUE_POTION, RED_POTION:
				return true;
			case BARREL, BOX, CANNON_TO_LEFT, CANNON_TO_RIGHT:
				return false;
			}
			return false;
		}
	}
	
	public static class Enemy {
		public static int GetSpriteAmount(int enemyType, int action) {
			switch (enemyType) {
			
			case Type.CRABBY:
				switch (action) {
				case Action.IDLE:
					return 9;
				case Action.RUNNING:
					return 6;
				case Action.ATTACK:
					return 7;
				case Action.HIT:
					return 4;
				case Action.DEAD:
					return 5;
				}
				
			case Type.PINKSTAR:
				switch (action) {
				case Action.IDLE:
					return 8;
				case Action.RUNNING:
					return 6;
				case Action.ATTACK:
					return 7;
				case Action.HIT:
					return 4;
				case Action.DEAD:
					return 5;
				}
				
			case Type.SHARK:
				switch (action) {
				case Action.IDLE:
					return 8;
				case Action.RUNNING:
					return 6;
				case Action.ATTACK:
					return 8;
				case Action.HIT:
					return 4;
				case Action.DEAD:
					return 5;
				}
				
			default: 
				return 0;
			}
		}
		
		public static float GetMoveSpeed(int enemyType) {
			switch (enemyType) {
			case Type.CRABBY:
				return 0.3f * Game.SCALE;
			case Type.PINKSTAR:
				return 0.5f * Game.SCALE;
			case Type.SHARK:
				return 0.4f * Game.SCALE;
				
			default:
				return 0;
			}
		}
		
		public static int GetMaxHP(int enemyType) {
			switch (enemyType) {
			case Type.CRABBY:
				return 5;
			case Type.PINKSTAR:
				return 4;
			case Type.SHARK:
				return 2;
				
			default:
				return 0;
			}
		}
		
		public static int GetDmg(int enemyType) {
			switch (enemyType) {
			case Type.CRABBY:
				return 10;
			case Type.PINKSTAR:
				return 10;
			case Type.SHARK:
				return 20;
				
			default:
				return 0;
			}
		}
		
		public static float GetAttackRange(int enemyType) {
			switch (enemyType) {
			case Type.CRABBY:
				return 1.3f * Game.TILE_SIZE;
			case Type.PINKSTAR:
				return 2.5f * Game.TILE_SIZE;
			case Type.SHARK:
				return 2 * Game.TILE_SIZE;
				
			default:
				return 0;
			}
		}
		
		public static class Type {
			public static final int CRABBY = 0;
			public static final int PINKSTAR = 1;
			public static final int SHARK = 2;
		}
		
		public static class Action {
			public static final int IDLE = 0;
			public static final int RUNNING = 1;
			public static final int ATTACK = 2;
			public static final int HIT = 3;
			public static final int DEAD = 4;
		}
		
		public static class Size {
			public static final int CRABBY_WIDTH_DEFAULT = 72;
			public static final int CRABBY_HEIGHT_DEFAULT = 32;
			public static final int CRABBY_WIDTH = (int) (CRABBY_WIDTH_DEFAULT * Game.SCALE);
			public static final int CRABBY_HEIGHT = (int) (CRABBY_HEIGHT_DEFAULT * Game.SCALE);
			public static final int CRABBY_DRAWOFFSET_X = (int) (26 * Game.SCALE);
			public static final int CRABBY_DRAWOFFSET_Y = (int) (9 * Game.SCALE);
			
			public static final int PINKSTAR_WIDTH_DEFAULT = 34;
			public static final int PINKSTAR_HEIGHT_DEFAULT = 30;
			public static final int PINKSTAR_WIDTH = (int) (PINKSTAR_WIDTH_DEFAULT * Game.SCALE);
			public static final int PINKSTAR_HEIGHT = (int) (PINKSTAR_HEIGHT_DEFAULT * Game.SCALE);
			public static final int PINKSTAR_DRAWOFFSET_X = (int) (8 * Game.SCALE);
			public static final int PINKSTAR_DRAWOFFSET_Y = (int) (7 * Game.SCALE);
			
			public static final int SHARK_WIDTH_DEFAULT = 34;
			public static final int SHARK_HEIGHT_DEFAULT = 30;
			public static final int SHARK_WIDTH = (int) (SHARK_WIDTH_DEFAULT * Game.SCALE);
			public static final int SHARK_HEIGHT = (int) (SHARK_HEIGHT_DEFAULT * Game.SCALE);
			public static final int SHARK_DRAWOFFSET_X = (int) (8 * Game.SCALE);
			public static final int SHARK_DRAWOFFSET_Y = (int) (6 * Game.SCALE);
		}
		
	}
	
	public static class Environment {
		public static final int BIG_CLOUD_WIDTH_DEFAULT = 448;
		public static final int BIG_CLOUD_HEIGHT_DEFAULT = 101;
		public static final int BIG_CLOUD_WIDTH = (int) (BIG_CLOUD_WIDTH_DEFAULT * Game.SCALE);
		public static final int BIG_CLOUD_HEIGHT = (int) (BIG_CLOUD_HEIGHT_DEFAULT * Game.SCALE);

		public static final int SMALL_CLOUD_WIDTH_DEFAULT = 74;
		public static final int SMALL_CLOUD_HEIGHT_DEFAULT = 24;
		public static final int SMALL_CLOUD_WIDTH = (int) (SMALL_CLOUD_WIDTH_DEFAULT * Game.SCALE);
		public static final int SMALL_CLOUD_HEIGHT = (int) (SMALL_CLOUD_HEIGHT_DEFAULT * Game.SCALE);
	}
	 
	public static class UI {
		public static class Button {
			public static final int B_WIDTH_DEFAULT = 140;
			public static final int B_HEIGHT_DEFAULT = 56;
			public static final int B_WIDTH = (int) (B_WIDTH_DEFAULT * Game.SCALE);
			public static final int B_HEIGHT = (int) (B_HEIGHT_DEFAULT * Game.SCALE);
		}
		
		public static class SoundButton {
			public static final int B_SOUND_SIZE_DEFAULT = 42;
			public static final int B_SOUND_SIZE = (int) (B_SOUND_SIZE_DEFAULT * Game.SCALE);
		}
		
		public static class UrmButton {
			public static final int B_URM_SIZE_DEFAULT = 56;
			public static final int B_URM_SIZE = (int) (B_URM_SIZE_DEFAULT * Game.SCALE);
		}
		
		public static class VolumeButton {
			public static final int HANDLE_VOLUME_WIDTH_DEFAULT = 28;
			public static final int HANDLE_VOLUME_WIDTH = (int) (HANDLE_VOLUME_WIDTH_DEFAULT * Game.SCALE);
			public static final int VOLUME_HEIGHT_DEFAULT = 44;
			public static final int VOLUME_HEIGHT = (int) (VOLUME_HEIGHT_DEFAULT * Game.SCALE);
			public static final int SLIDER_VOLUME_WIDTH_DEFAULT = 215;
			public static final int SLIDER_VOLUME_WIDTH = (int) (SLIDER_VOLUME_WIDTH_DEFAULT * Game.SCALE);
		}
		
		public static class LevelButton {
			public static final int B_LEVEL_SIZE_DEFAULT = 42;
			public static final int B_LEVEL_SIZE = (int) (B_LEVEL_SIZE_DEFAULT * Game.SCALE);
		}
	}
	
	public static class Direction {
		public static final int LEFT = 0;
		public static final int RIGHT = 1;
	}

	public static class Player {
		public static final int PLAYER_WIDTH_DEFAULT = 64;
		public static final int PLAYER_HEIGHT_DEFAULT = 40;
		public static final int PLAYER_WIDTH = (int) (PLAYER_WIDTH_DEFAULT * Game.SCALE);
		public static final int PLAYER_HEIGHT = (int) (PLAYER_HEIGHT_DEFAULT * Game.SCALE);
		
		public static final float PLAYER_SPEED = 3.3f * Game.SCALE;
		
		public static final int IDLE = 0;
		public static final int RUN = 1;
		public static final int JUMP = 2;
		public static final int FALL = 3;
		public static final int LAND = 4;
		public static final int HURT = 5;
		public static final int POWER_ATTACK = 6;
		public static final int ATTACK_DOWN = 7;
		public static final int DIE = 8;
		public static final int SWIM = 9;
		
		public static int GetSpriteAmount(int playerAction) {
			switch (playerAction) {
			case IDLE:
				return 5;
			case RUN:
				return 6;
			case JUMP:
				return 2;
			case FALL, SWIM:
				return 1;
			case LAND:
				return 2;
			case HURT, ATTACK_DOWN:
				return 4;
			case DIE:
				return 8;
			case POWER_ATTACK:
				return 3;
			default:
				return 0;
			}
		}
	}
	
}
