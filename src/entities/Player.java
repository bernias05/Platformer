package entities;

import static helpz.Constants.Direction.LEFT;
import static helpz.Constants.Direction.RIGHT;
import static helpz.Constants.Player.*;
import static helpz.HelpMethods.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import audio.AudioPlayer;
import gameStates.Playing;
import helpz.LoadSave;
import main.Game;

import static main.Game.*;

public class Player extends Entity {
	
	private Playing playing;
	private BufferedImage[][] animations;
	private BufferedImage statusBarImg;
	
	private int[][] lvlData;
	
	private float xDrawOffset = 24 * SCALE;
	private float yDrawOffset = 10 * SCALE;
	private int flipX = 0;
	private int flipW = 1;
			
	private boolean left, right, up, down;
	private boolean jumping, landing, attacking;
	private boolean inWater, hurt;

	private boolean canJump = true;
	
	private int hurtTick;
	private int maxHurtTick = 60;
	
	private float jumpSpeed = -3.6f * SCALE;
	
	private int statusBarWidth = (int) (192 * SCALE);
	private int statusBarHeight = (int) (58 * SCALE);
	private int statusBarX = (int) (10 * SCALE);
	private int statusBarY = (int) (10 * SCALE);
	
	private int healthBarWidth = (int) (150 * SCALE);
	private int healthBarHeight = (int) (6 * SCALE);
	private int healthBarX = (int) (45 * SCALE);
	private int healthBarY = (int) (23 * SCALE);

	private int powerBarHeight = (int) (2 * SCALE);
	private int powerBarX = (int) (54 * SCALE);
	private int powerBarY = (int) (44 * SCALE);
	private int maxPower = 100;
	private int power = 100;
	private int powerAttackTick;
	private int powerGrowSpeed = 80;
	private int powerGrowTick;
	private int powerGrowRate = 5;
	private boolean powerAttackActive;
	private float powerBarWidth = 104f * SCALE;
	
	public Player(Playing playing, float x, float y) {
		super((int) x, (int) y);
		this.playing = playing;
		moveSpeed = PLAYER_SPEED;
		maxHP = 100;
		currentHP = maxHP;
		dmg = 1;
		
		loadImgs();
		initHitbox(16, 21);
		initAttackHitbox();
	}
	
	private void initAttackHitbox() {
		attackHitbox = new Rectangle2D.Float(hitbox.x + hitbox.width, hitbox.y, (int) 30 * SCALE, (int) 30 * SCALE);
	}
	
	private void loadImgs() {
		loadAnimationSprites();
		loadBarImg();
	}
	
	private void loadBarImg() {
		statusBarImg = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_HP_POWER_BARS);
	}
	
	private void loadAnimationSprites() {
		BufferedImage sprites = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_SPRITES);
		animations = new BufferedImage[10][8];
		
		for (int i = 0; i < animations.length; i++) {
			for (int j = 0; j < animations[i].length; j++)
				animations[i][j] = sprites.getSubimage(j * PLAYER_WIDTH_DEFAULT, i * PLAYER_HEIGHT_DEFAULT, PLAYER_WIDTH_DEFAULT, PLAYER_HEIGHT_DEFAULT);
		}
	}
	
	public void loadLvlData(int[][] lvlData) {
		this.lvlData = lvlData;
		if (!IsEntityOnFloor(hitbox, lvlData))
			inAir = true;
	}
	
	public void draw(Graphics g, int xLvlOffset, int yLvlOffset) {
		drawBars(g, xLvlOffset);
		g.drawImage(animations[action][aniIndex],
				(int) (hitbox.x - xDrawOffset) - xLvlOffset + flipX,
				(int) (hitbox.y - yDrawOffset) - yLvlOffset,
				PLAYER_WIDTH * flipW, PLAYER_HEIGHT, null);
//		drawHitbox(g, xLvlOffset, yLvlOffset);
//		drawAttackHitbox(g, xLvlOffset, yLvlOffset);
	}
	
	private void drawBars(Graphics g, int lvlOffset) {
		g.setColor(Color.red);
		g.fillRect(healthBarX, healthBarY, (int) (healthBarWidth / maxHP * currentHP), healthBarHeight);
		
		g.drawImage(statusBarImg, statusBarX, statusBarY, statusBarWidth, statusBarHeight, null);
		
		g.setColor(Color.cyan);
		g.fillRect(powerBarX, powerBarY, (int) (powerBarWidth / maxPower * power), powerBarHeight);
	}
	
	public void update() {
		if (currentHP == 0) {
			if (action != DIE) {
				action = DIE;
				aniTick = 0;
				aniIndex = 0;
				playing.setPlayerDying(true);
				playing.setPaused(false);
				playing.getGame().getAudioPlayer().playEffect(AudioPlayer.DIE);
			} else if (aniIndex == GetSpriteAmount(DIE) - 1 && aniTick == Game.ANI_SPEED - 1) {
				playing.gameOver();
				playing.setPlayerDying(false);
				playing.getGame().getAudioPlayer().stopSong();
				playing.getGame().getAudioPlayer().playEffect(AudioPlayer.GAMEOVER);
			} else
				updateAnimationTick();
			return;
		}
		if (hurt) {
			hurtTick++;
			if (hurtTick > maxHurtTick) {
				hurtTick = 0;
				hurt = false;
			}
		}
		inWater = isPlayerInWater();
		
		updatePower();
		updatePos();
		updateAttackHitbox();
		updateAnimationTick();
		setAction();
	}
	
	private boolean isPlayerInWater() {
		int playerTileX = (int) (hitbox.x / TILE_SIZE);
		int playerTileY = (int) (hitbox.y / TILE_SIZE);
		int playerTileXPlusWidth = (int) ((hitbox.x + hitbox.width) / TILE_SIZE);
		int playerTileYPlusHeight = (int) ((hitbox.y + hitbox.height) / TILE_SIZE);
		
		if (lvlData[playerTileX][playerTileY] < 48 || lvlData[playerTileX][playerTileY] == 100)
			if (lvlData[playerTileX][playerTileYPlusHeight] < 48 || lvlData[playerTileX][playerTileYPlusHeight] == 100)
				if (lvlData[playerTileXPlusWidth][playerTileY] < 48 || lvlData[playerTileXPlusWidth][playerTileY] == 100)
					if (lvlData[playerTileXPlusWidth][playerTileYPlusHeight] < 48 || lvlData[playerTileXPlusWidth][playerTileYPlusHeight] == 100) {
						return false;
					}
		return true;
	}
	
	private void updatePower() {
		powerGrowTick++;
		if (powerGrowTick >= powerGrowSpeed) {
			changePower(powerGrowRate);
			powerGrowTick = 0;
		}
		
		if (powerAttackActive) {
			powerAttackTick++;
			if (powerAttackTick >= 35) {
				powerAttackTick = 0;
				powerAttackActive = false;
			}
		}
	}

	private void updateAttackHitbox() {
		if (flipW == 1)
			attackHitbox.x = hitbox.x + hitbox.width;
		else
			attackHitbox.x = hitbox.x - attackHitbox.width;
		
		attackHitbox.y = hitbox.y;
	}
	
	private void updatePos() {
		float xSpeed = 0;
		
		if (powerAttackActive) {
			xSpeed = flipW == 1 ? moveSpeed : -moveSpeed;
			xSpeed *= 3;
			updatePosX(xSpeed);
		} else if (hit) {
			hit();
		} else if (inWater) {
			swim();
			inAir = false;
		} else {
			if (waterSpeedY != 0) {
				airSpeed = waterSpeedY;
				waterSpeedY = 0;
				waterSpeedX = 0;
				inAir = true;
			}
			if (isPlayerNotMoving())
				return;
			
			checkJumpStart();
			
			if (left && !right) {
				xSpeed -= moveSpeed;
				flipX = PLAYER_WIDTH;
				flipW = -1;
			} else if (right && !left) {
				xSpeed += moveSpeed;
				flipX = 0;
				flipW = 1;
			}
			updatePosX(xSpeed);
			
			if (!inAir)
				if(!IsEntityOnFloor(hitbox, lvlData))
					inAir = true;
			
			if (inAir && !powerAttackActive)
				updatePosY(airSpeed);
		}
	}

	private void hit() {
		if (CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
			hitbox.y += airSpeed;
			airSpeed += Game.GRAVITY;
		} else {
			hitbox.y = GetYPosAtTile(hitbox, airSpeed);
			hit = false;
			inAir = false;
			airSpeed = 0;
		}
		updatePosX(hitSpeedX);
	}
	
	private void swim() {
		// x-moves
		if (left && !right) {
			waterSpeedX = ApproximateSpeed(waterSpeedX, -MAX_WATER_SPEED, WATER_ACCELERATION);
			flipX = PLAYER_WIDTH;
			flipW = -1;
		} else if (right && !left) {
			waterSpeedX = ApproximateSpeed(waterSpeedX, MAX_WATER_SPEED, WATER_ACCELERATION);
			flipX = 0;
			flipW = 1;
		} else if (right && left || !right && !left) {
			waterSpeedX = ApproximateSpeed(waterSpeedX, 0, WATER_INERTIA);
		}
		updatePosX(waterSpeedX);
		
		// y-moves
		if (airSpeed != 0) {
			waterSpeedY = airSpeed;
			airSpeed = 0;
		}
		if (Math.abs(waterSpeedY) > MAX_WATER_SPEED) {
			if (waterSpeedY > MAX_WATER_SPEED)
				waterSpeedY = ApproximateSpeed(waterSpeedY, MAX_WATER_SPEED, WATER_SLOWING);
			else
				waterSpeedY = ApproximateSpeed(waterSpeedY, -MAX_WATER_SPEED, WATER_SLOWING);
		} else {
			if (up && !down)
				waterSpeedY = ApproximateSpeed(waterSpeedY, -MAX_WATER_SPEED, WATER_ACCELERATION);
			else if (down && !up)
				waterSpeedY = ApproximateSpeed(waterSpeedY, MAX_WATER_SPEED, WATER_ACCELERATION);
			else if (up && down || !up && !down)
				waterSpeedY = ApproximateSpeed(waterSpeedY, SINK_SPEED, WATER_INERTIA);
		}
		updatePosY(waterSpeedY);
	}
	
	private boolean isPlayerNotMoving() {
		if (!powerAttackActive)
			if (!jumping && !inAir)
				if (left && right || !left && !right)
					return true;
		return false;
	}
	
	private void checkJumpStart() {
		if (jumping && canJump) {
			if (!inAir) {
				inAir = true;
				airSpeed = jumpSpeed;
				playing.getGame().getAudioPlayer().playEffect(AudioPlayer.JUMP);
			}
		}
	}
	
	private void updatePosX(float xSpeed) {
		if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData))
			hitbox.x += xSpeed;
		else {
			hitbox.x = GetEntityXPosAtWall(hitbox, xSpeed);
			powerAttackActive = false;
			powerAttackTick = 0;
			waterSpeedX = 0;
		}
	}
	
	private void updatePosY(float ySpeed) {
		if (CanMoveHere(hitbox.x, hitbox.y + ySpeed, hitbox.width, hitbox.height, lvlData)) {
			hitbox.y += ySpeed;
			if (!inWater)
				airSpeed += GRAVITY;
		}
		else {
			hitbox.y = GetYPosAtTile(hitbox, ySpeed);
			if (inWater) {
				waterSpeedY = 0;
			} else {
				if (airSpeed > 0 ) {
					inAir = false;
					landing = true;
				}
				airSpeed = 0;
			}
		}
	}
	
	private void updateAnimationTick() {
		int aniSpeed = Game.ANI_SPEED;
		
		if (action == JUMP && aniIndex == 1 || action == POWER_ATTACK || action == SWIM)
			return;
		
		aniTick++;
		
		if (action == ATTACK_DOWN)
			aniSpeed *= 1.25;
		
		if (aniTick >= aniSpeed) {
			aniTick = 0;
			aniIndex++;
			
			if (aniIndex >= GetSpriteAmount(action)) {
				aniIndex = 0;
				landing = false;
				attacking = false;
				
				if (action == HURT) {
					action = IDLE;
					hitSpeedX = 0;
					hit = false;
				}
			}
		}
	}
	
	public void powerAttack() {
		if (powerAttackActive)
			return;
		
		if (power == maxPower) {
			playing.getGame().getAudioPlayer().playEffect(AudioPlayer.POWER_ATTACK);
			powerAttackActive = true;
			airSpeed = 0;
			inAir = true;
//			power = 0;
		}
	}
	
	private void setAction() {
		int startAni = action;
		
		if (action == HURT)
			return;
		if (left && right || !left && !right) {
			action = IDLE;
		} else
			action = RUN;
		
		if (inWater)
			action = SWIM;
		if (inAir) {
			if (airSpeed < 0)
				action = JUMP;
			else
				action = FALL;
		}
		
		if (landing && !jumping)
			action = LAND;
		
		if (attacking) {
			action = ATTACK_DOWN;
		}
		
		if (powerAttackActive) {
			aniIndex = 1;
			aniTick = 0;
			action = POWER_ATTACK;
		}
		
		if (startAni != action) {
			resetForNewAni();
			if (action == ATTACK_DOWN)
				playing.getGame().getAudioPlayer().playAttackSound();
		}
	}
	
	public void reset() {
		resetPosHPAni();
		resetDirBooleans();
		action = 0;
		flipX = 0; flipW = 1;
		airSpeed = 0;
		powerAttackActive = false;
//		power = 0;
		
		inAir = true;
		attacking = false; landing = false; jumping = false;
		hurt = false;
	}
	
	public void resetDirBooleans() {
		left = false;
		right = false;
	}
	
	private void resetForNewAni() {
		aniTick = 0;
		aniIndex = 0;
	}
	
	public void hurt(int dmg, Enemy e) {
		currentHP -= dmg;
		hurt = true;
		if (currentHP <= 0)
			currentHP = 0;
		else {
			playing.getGame().getAudioPlayer().playEffect(AudioPlayer.PLAYER_HURT);
			action = HURT;
			resetForNewAni();hit = true;
			inAir = true;
			airSpeed = -1.5f * Game.SCALE;
			if (hitbox.x > e.getHitbox().x) {
				hitDir = RIGHT;
				hitSpeedX = (int) (moveSpeed * 0.75);
			} else {
				hitDir = LEFT;
				hitSpeedX = (int) (-moveSpeed * 0.75);
			}
		}
	}
	
	public void hurt(int dmg) {
		currentHP -= dmg;
		hurt = true;
		if (currentHP <= 0)
			currentHP = 0;
		else
			playing.getGame().getAudioPlayer().playEffect(AudioPlayer.PLAYER_HURT);
	}
	
	public void heal(int heal) {
		currentHP += heal;
		if (currentHP >= maxHP)
			currentHP = maxHP;
	}
	
	public void changePower(int value) {
		power += value;
		if (power > maxPower)
			power = maxPower;
		else if (power < 0)
			power = 0;
	}
	
	public Playing getPlaying() {
		return playing;
	}
	
	public int getDmg() {
		return dmg;
	}
	
	public boolean isAttacking() {
		return attacking;
	}
	
	public void setAttacking(boolean attacking) {
		this.attacking = attacking;
	}
	
	public boolean isLeft() {
		return left;
	}
	
	public void setLeft(boolean left) {
		this.left = left;
	}
	
	public boolean isRight() {
		return right;
	}
	
	public void setRight(boolean right) {
		this.right = right;
	}
	
	public boolean isUp() {
		return up;
	}
	
	public void setUp(boolean up) {
		this.up = up;
	}
	
	public boolean isDown() {
		return down;
	}
	
	public void setDown(boolean down) {
		this.down = down;
	}
	
	public void setJumping(boolean jump) {
		this.jumping = jump;
	}
	
	public void setLanding(boolean landing) {
		this.landing = landing;
	}
	
	public void setCanJump(boolean canJump) {
		this.canJump = canJump;
	}
	
	public boolean isPowerAttackActive() {
		return powerAttackActive;
	}
	
	public boolean isHurt() {
		return hurt;
	}
	
	public boolean isInWater() {
		return inWater;
	}
	
}
