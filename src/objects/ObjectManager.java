package objects;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Float;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import audio.AudioPlayer;
import entities.Crabby;
import entities.Pinkstar;
import entities.Player;
import entities.Shark;
import gameStates.Playing;
import helpz.LoadSave;
import levels.Level;
import main.Game;

import static helpz.Constants.Enemy.Action.*;
import static helpz.Constants.Object.*;
import static helpz.Constants.Object.Size.*;
import static helpz.Constants.Player.PLAYER_SPEED;
import static helpz.HelpMethods.IsProjectileHittingWall;

public class ObjectManager {
	
	private Playing playing;
	
	private BufferedImage[][] potionImgs, containerImgs;
	private BufferedImage[] cannonImgs, treeImgs, curvedTreeImgs, boatImgs, grassImgs, coinImgs;
	private BufferedImage spikeImg, cannonballImg;
	
	private ArrayList<Potion> potions;
	private ArrayList<GameContainer> containers;
	private ArrayList<Spike> spikes;
	private ArrayList<Cannon> cannons;
	private ArrayList<Projectile> cannonballs = new ArrayList<>();
	private ArrayList<Tree> trees;
	private ArrayList<Boat> boats;
	private ArrayList<Grass> grass;
	private ArrayList<Coin> coins;
	
	private int spikeTick;
	private int maxSpikeTick = 40;
	
	private boolean active = true;
	
	public ObjectManager(Playing playing) {
		this.playing = playing;
		loadImgs();
	}
	
	private void loadImgs() {
		spikeImg 	  = LoadSave.GetSpriteAtlas(LoadSave.OBJECT_SPIKE);
		cannonballImg = LoadSave.GetSpriteAtlas(LoadSave.OBJECT_CANNONBALL);
		
		BufferedImage potionAtlas	  = LoadSave.GetSpriteAtlas(LoadSave.OBJECT_POTIONS);
		BufferedImage containerAtlas  = LoadSave.GetSpriteAtlas(LoadSave.OBJECT_BOX_BARREL);
		BufferedImage cannonAtlas 	  = LoadSave.GetSpriteAtlas(LoadSave.OBJECT_CANNON);
		BufferedImage treeAtlas 	  = LoadSave.GetSpriteAtlas(LoadSave.TREE_SPRITES);
		BufferedImage curvedTreeAtlas = LoadSave.GetSpriteAtlas(LoadSave.CURVED_TREE_SPRITES);
		BufferedImage boatAtlas   	  = LoadSave.GetSpriteAtlas(LoadSave.OBJECT_BOAT);
		BufferedImage grassAtlas 	  = LoadSave.GetSpriteAtlas(LoadSave.GRASS_SPRITES);
		BufferedImage coinAtlas 	  = LoadSave.GetSpriteAtlas(LoadSave.OBJECT_COIN);
		
		potionImgs = new BufferedImage[2][7];
		for (int i = 0; i < potionImgs.length; i++)
			for ( int j = 0; j < potionImgs[0].length; j++)
				potionImgs[i][j] = potionAtlas.getSubimage(j * POTION_WIDTH_DEFAULT, i * POTION_HEIGHT_DEFAULT, POTION_WIDTH_DEFAULT, POTION_HEIGHT_DEFAULT);
		
		containerImgs = new BufferedImage[2][8];
		for (int i = 0; i < containerImgs.length; i++)
			for (int j = 0; j < containerImgs[0].length; j++)
				containerImgs[i][j] = containerAtlas.getSubimage(j * CONTAINER_WIDTH_DEFAULT, i * CONTAINER_HEIGHT_DEFAULT, CONTAINER_WIDTH_DEFAULT, CONTAINER_HEIGHT_DEFAULT);
		
		cannonImgs = new BufferedImage[7];
		for (int i = 0; i < cannonImgs.length; i++)
			cannonImgs[i] = cannonAtlas.getSubimage(i * CANNON_WIDTH_DEFAULT, 0, CANNON_WIDTH_DEFAULT, CANNON_HEIGHT_DEFAULT);
		
		treeImgs	   = new BufferedImage[4];
		curvedTreeImgs = new BufferedImage[4];
		boatImgs 	   = new BufferedImage[4];
		for (int i = 0; i < treeImgs.length; i++) {
			treeImgs[i] 	  = treeAtlas	   .getSubimage(i * TREE_WIDTH_DEFAULT,	 	   0, TREE_WIDTH_DEFAULT, 		 TREE_HEIGHT_DEFAULT);
			curvedTreeImgs[i] = curvedTreeAtlas.getSubimage(i * CURVED_TREE_WIDTH_DEFAULT, 0, CURVED_TREE_WIDTH_DEFAULT, CURVED_TREE_HEIGHT_DEFAULT);
			boatImgs[i]  	  = boatAtlas	   .getSubimage(i * BOAT_WIDTH_DEFAULT,		   0, BOAT_WIDTH_DEFAULT, 		 BOAT_HEIGHT_DEFAULT);
		}
		
		grassImgs = new BufferedImage[3];
		for (int i = 0; i < grassImgs.length; i++)
			grassImgs[i] = grassAtlas.getSubimage(i * GRASS_WIDTH_DEFAULT, 0, GRASS_WIDTH_DEFAULT, GRASS_HEIGHT_DEFAULT);
		
		coinImgs = new BufferedImage[6];
		for (int i = 0; i < coinImgs.length; i++)
			coinImgs[i] = coinAtlas.getSubimage(i * COIN_WIDTH_DEFAULT, 0, COIN_WIDTH_DEFAULT, COIN_WIDTH_DEFAULT);
	}
	
	public void loadObjects(Level lvl) {
		potions = new ArrayList<>(lvl.getPotions());
		containers = new ArrayList<>(lvl.getContainers());
		spikes = lvl.getSpikes();
		cannons = lvl.getCannons();
		trees = lvl.getTrees();
		boats = lvl.getBoats();
		grass = lvl.getGrass();
		coins = lvl.getCoins();
		cannonballs.clear();
	}
	
	public void update(Player player, int[][] lvlData) {
		for (Potion p : potions)
			if (p.isActive())
				p.updateAnimationTick();
		
		for (GameContainer c : containers)
			if (c.isActive())
				c.update();
		for (Cannon c : cannons)
			c.update(this, player, lvlData);
		
		for (Projectile p : cannonballs)
			if (p.isActive())
				p.update();
		
		for (Tree t : trees)
			t.update();
		
		for (Boat b : boats)
			b.updateAnimationTick();
		
		for (Coin c : coins)
			c.updateAnimationTick();
		
		checkObjectHitboxes(playing.getPlayer(), lvlData);
	}
	
	public void checkObjectHitboxes(Player player, int[][] lvlData) {
		checkPotions(player.getHitbox());
		checkContainers(player.getAttackHitbox());
		checkSpikes(player);
		checkCannonballs(player, lvlData);
		checkCoins(player.getHitbox());
		checkBoats(player.getHitbox());
	}

	private void checkBoats(Float hitbox) {
		for (Boat b : boats) {
			if (b.getHitbox().intersects(hitbox)) {
				playing.levelCompleted();
			}
		}
	}

	private void checkCannonballs(Player player, int[][] lvlData) {
		for (Projectile p : cannonballs) {
			if (p.isActive()) {
				if (p.getHitbox().intersects(player.getHitbox())) {
					player.hurt(CANNONBALL_DMG);
					p.setActive(false);
				} else if (IsProjectileHittingWall(p, lvlData))
					p.setActive(false);
			}
		} 
	}
	
	private void checkPotions(Rectangle2D.Float hitbox) {
		for (Potion p : potions)
			if (p.isActive())
				if (p.getHitbox().intersects(hitbox)) {
					p.setActive(false);
					playing.getGame().getAudioPlayer().playEffect(AudioPlayer.POTION_BREAKS);
					if (p.getType() == RED_POTION)
						playing.getPlayer().heal(RED_POTION_VALUE);
					else
						playing.getPlayer().changePower(BLUE_POTION_VALUE);
				}
	}
	
	private void checkContainers(Rectangle2D.Float attackHitbox) {
		for (GameContainer c : containers) {
			if (c.isActive())
				if (c.getHitbox().intersects(attackHitbox))
					if (playing.getPlayer().isAttacking() && playing.getPlayer().getAniIndex() == 2 && playing.getPlayer().getAniTick() == 1 || playing.getPlayer().isPowerAttackActive()) {
						c.setNeedAnimation(true);
						playing.getGame().getAudioPlayer().playEffect(AudioPlayer.CONTAINER_BREAKS);
						return;
					}
			if (c.isDestroyed()) {
				potions.add(new Potion((int) (c.getHitbox().x - (Game.TILE_SIZE - c.getHitbox().width) / 2), (int) c.getHitbox().y / Game.TILE_SIZE * Game.TILE_SIZE, c.getType() - 2));
				c.setDestroyed(false);
				c.setActive(false);
				playing.getLevelManager().getLevelInfo().containerDestroyed();
			}
		}
	}
	
	private void checkSpikes(Player player) {
		ArrayList<Crabby> crabbys = playing.getEnemyManager().getCrabbys();
		ArrayList<Pinkstar> pinkstars = playing.getEnemyManager().getPinkstars();
		ArrayList<Shark> sharks = playing.getEnemyManager().getSharks();
		
		boolean in = false;
		
		if (!active) {
			spikeTick++;
			if (spikeTick > maxSpikeTick) {
				spikeTick = 0;
				active = true;
			}
		}
		
		for (Spike s : spikes) {
			if (s.getHitbox().intersects(player.getHitbox())) {
				in = true;
				player.setMoveSpeed(PLAYER_SPEED / 2);
//				player.setCanJump(false);
				if (active) {
					player.hurt(SPIKE_DAMAGE);
					active = false;
				}
			}
			
			for (Crabby c : crabbys)
				if (s.getHitbox().intersects(c.getHitbox()) && c.getEnemyAction() != DEAD && active)
					c.hurt(SPIKE_DAMAGE, null);
			
			for (Pinkstar p : pinkstars)
				if (s.getHitbox().intersects(p.getHitbox()) && p.getEnemyAction() != DEAD && active)
						p.hurt(SPIKE_DAMAGE, null);
			
			for (Shark sh : sharks)
				if (s.getHitbox().intersects(sh.getHitbox()) && sh.getEnemyAction() != DEAD && active)
						sh.hurt(SPIKE_DAMAGE, null);
		}
		
		if (!in) {
			player.setMoveSpeed(PLAYER_SPEED);
			player.setCanJump(true);
		}
	}
	
	private void checkCoins(Rectangle2D.Float hitbox) {
		for (Coin c : coins) {
			if (c.isActive())
				if (c.getHitbox().intersects(hitbox)) {
					c.setActive(false);
					playing.getGame().getAudioPlayer().playEffect(AudioPlayer.COIN_COLLECTED);
					playing.getLevelManager().getLevelInfo().coinCollected();
				}
		}
	}
	
	public void draw(Graphics g, int xLvlOffset, int yLvlOffset) {
		drawGrass(g, xLvlOffset, yLvlOffset);
		drawBoats(g, xLvlOffset, yLvlOffset);
		drawTrees(g, xLvlOffset, yLvlOffset);
		drawPotions(g, xLvlOffset, yLvlOffset);
		drawContainers(g, xLvlOffset, yLvlOffset);
		drawSpikes(g, xLvlOffset, yLvlOffset);
		drawCannons(g, xLvlOffset, yLvlOffset);
		drawProjectiles(g, xLvlOffset, yLvlOffset);
		drawCoins(g, xLvlOffset, yLvlOffset);
	}

	private void drawGrass(Graphics g, int xLvlOffset, int yLvlOffset) {
		for (Grass gr : grass) {
			g.drawImage(grassImgs[gr.getIndex()], 
					gr.getX() - xLvlOffset, 
					gr.getY() - GRASS_HEIGHT - yLvlOffset, 
					GRASS_WIDTH, GRASS_HEIGHT, null);
		}
		
	}

	private void drawBoats(Graphics g, int xLvlOffset, int yLvlOffset) {
		for (Boat b : boats) {
			g.drawImage(boatImgs[b.getAniIndex()], 
					(int) b.getHitbox().x - xLvlOffset, 
					(int) b.getHitbox().y - yLvlOffset - b.getyDrawOffset(), 
					BOAT_WIDTH, BOAT_HEIGHT, null);
//			b.drawHitbox(g, xLvlOffset, yLvlOffset);
		}
	}

	private void drawTrees(Graphics g, int xLvlOffset, int yLvlOffset) {
		for (Tree t : trees) {
			if (t.getType() == TREE) {
				g.drawImage(treeImgs[t.getAniIndex()], 
						t.getX() - xLvlOffset, 
						t.getY() - yLvlOffset,
						TREE_WIDTH, TREE_HEIGHT, null);
			} 
			else if (t.getType() == CURVED_TREE) {
				g.drawImage(curvedTreeImgs[t.getAniIndex()], 
						t.getX() - xLvlOffset, 
						t.getY() - yLvlOffset,
						CURVED_TREE_WIDTH, CURVED_TREE_HEIGHT, null);
			} 
			else if (t.getType() == CURVED_TO_LEFT_TREE) {
				g.drawImage(curvedTreeImgs[t.getAniIndex()], 
						t.getX() - xLvlOffset, 
						t.getY() - yLvlOffset,
						-CURVED_TREE_WIDTH, CURVED_TREE_HEIGHT, null);
			}
		}
	}

	private void drawPotions(Graphics g, int xLvlOffset, int yLvlOffset) {
		for (Potion p : potions)
			if (p.isActive()) {
				g.drawImage(potionImgs[p.getType()][p.getAniIndex()], 
						(int) (p.getHitbox().x - p.getxDrawOffset()) - xLvlOffset, 
						(int) (p.getHitbox().y - p.getyDrawOffset() - 2 * Game.SCALE) - yLvlOffset, 
						POTION_WIDTH, POTION_HEIGHT, null);
//				p.drawHitbox(g, xLvlOffset, yLvlOffset);
			}
	}
	
	private void drawContainers(Graphics g, int xLvlOffset, int yLvlOffset) {
		for (GameContainer c : containers)
			if (c.isActive()) {
				g.drawImage(containerImgs[c.getType() - 2][c.getAniIndex()], 
						(int) (c.getHitbox().x - c.getxDrawOffset()) - xLvlOffset, 
						(int) (c.getHitbox().y - c.getyDrawOffset()) - yLvlOffset, 
						CONTAINER_WIDTH, CONTAINER_HEIGHT, null);
//				c.drawHitbox(g, xLvlOffset, yLvlOffset);
			}
	}
	
	private void drawSpikes( Graphics g, int xLvlOffset, int yLvlOffset) {
		for (Spike s : spikes) {
			g.drawImage(spikeImg, 
					(int) s.getHitbox().x - xLvlOffset, 
					(int) s.getHitbox().y - s.getyDrawOffset() - yLvlOffset, 
					SPIKE_WIDTH, SPIKE_HEIGHT, null);
//			s.drawHitbox(g, xLvlOffset, yLvlOffset);
		}
	}
	
	private void drawCannons(Graphics g, int xLvlOffset, int yLvlOffset) {
		for (Cannon c : cannons) {
			if (c.getType() == CANNON_TO_RIGHT)
				g.drawImage(cannonImgs[c.getAniIndex()], 
						(int) (c.getHitbox().x - c.getxDrawOffset() + c.getHitbox().width) - xLvlOffset, 
						(int) (c.getHitbox().y - 1.5 * Game.SCALE) - yLvlOffset, 
						-CANNON_WIDTH, CANNON_HEIGHT, null);
			else
				g.drawImage(cannonImgs[c.getAniIndex()], 
						(int) (c.getHitbox().x + c.getxDrawOffset() - xLvlOffset), 
						(int) (c.getHitbox().y - 1.5 * Game.SCALE) - yLvlOffset, 
						CANNON_WIDTH, CANNON_HEIGHT, null);
//			c.drawHitbox(g, xLvlOffset, yLvlOffset);
		}
	}
	
	private void drawProjectiles(Graphics g, int xLvlOffset, int yLvlOffset) {
		for (Projectile p : cannonballs)
			if (p.isActive()) {
				g.drawImage(cannonballImg, 
						(int) p.getHitbox().x - p.getxDrawOffset() - xLvlOffset, 
						(int) p.getHitbox().y - p.getyDrawOffset() - yLvlOffset, 
						CANNONBALL_WIDTH, CANNONBALL_WIDTH, null);
//				p.drawHitbox(g, xLvlOffset, yLvlOffset);
			}
	}
	
	private void drawCoins(Graphics g, int xLvlOffset, int yLvlOffset) {
		for (Coin c : coins) {
			if (c.isActive())
				g.drawImage(coinImgs[c.getAniIndex()], 
						c.getX() - xLvlOffset - c.getxDrawOffset(),
						c.getY() - yLvlOffset - c.getyDrawOffset(), 
						COIN_WIDTH,
						COIN_WIDTH,
						null);
//			c.drawHitbox(g, xLvlOffset, yLvlOffset);
			}
	}
	
	public void reset() {
		loadObjects(playing.getLevelManager().getCurrentLevel());
		for (Potion p : potions)
			p.reset();
		for (GameContainer gc : containers)
			gc.reset();
		for (Cannon c : cannons)
			c.reset();
		for (Coin c : coins)
			c.reset();
	}
	
	public ArrayList<Projectile> getCannonballs() {
		return cannonballs;
	}
	
	public BufferedImage getPotionImg() {
		return potionImgs[1][0];
	}
	
	public BufferedImage getContainerImg() {
		return containerImgs[0][0];
	}
	
	public BufferedImage getSpikeImg() {
		return spikeImg;
	}
	
	public BufferedImage getTreeImg() {
		return curvedTreeImgs[0];
	}
	
	public BufferedImage getCannonImg() {
		return cannonImgs[0];
	}
	
	public BufferedImage getBoatImg() {
		return boatImgs[0];
	}
	
	public BufferedImage getCoinImg() {
		return coinImgs[0];
	}
	
}
