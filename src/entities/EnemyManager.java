package entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import gameStates.Playing;
import helpz.Constants;
import helpz.LoadSave;
import levels.Level;

import static helpz.Constants.Enemy.Size.*;
import static helpz.HelpMethods.*;

public class EnemyManager {
	
	private Playing playing;
	
	private BufferedImage[][] crabbyImgs;
	private BufferedImage[][] pinkstarImgs;
	private BufferedImage[][] sharkImgs;
	
	private ArrayList<Crabby> crabbys = new ArrayList<Crabby>();
	private ArrayList<Pinkstar> pinkstars = new ArrayList<Pinkstar>();
	private ArrayList<Shark> sharks = new ArrayList<Shark>();
	
	public EnemyManager(Playing playing) {
		this.playing = playing;
		loadEnemyImgs();
	}
	
	private void loadEnemyImgs() {
		loadCrabbyImgs();
		loadPinkstarImgs();
		loadSharkImgs();
	}
	
	private void loadCrabbyImgs() {
		crabbyImgs = new BufferedImage[5][9];
		BufferedImage atlas = LoadSave.GetSpriteAtlas(LoadSave.CRABBY_SPRITES);
		
		for (int i = 0; i < crabbyImgs.length; i++) {
			for (int j = 0; j < crabbyImgs[0].length; j++) {
				crabbyImgs[i][j] = atlas.getSubimage(
						j * CRABBY_WIDTH_DEFAULT, 
						i * CRABBY_HEIGHT_DEFAULT,
						CRABBY_WIDTH_DEFAULT,
						CRABBY_HEIGHT_DEFAULT);
			}
		}
	}

	private void loadPinkstarImgs() {
		pinkstarImgs = new BufferedImage[5][8];
		BufferedImage atlas = LoadSave.GetSpriteAtlas(LoadSave.PINKSTAR_SPRITES);
		
		for (int i = 0; i < pinkstarImgs.length; i++) {
			for (int j = 0; j < pinkstarImgs[0].length; j++) {
				pinkstarImgs[i][j] = atlas.getSubimage(
						j * PINKSTAR_WIDTH_DEFAULT, 
						i * PINKSTAR_HEIGHT_DEFAULT, 
						PINKSTAR_WIDTH_DEFAULT, 
						PINKSTAR_HEIGHT_DEFAULT);
			}
		}
	}

	private void loadSharkImgs() {
		sharkImgs = new BufferedImage[5][8];
		BufferedImage atlas = LoadSave.GetSpriteAtlas(LoadSave.SHARK_SPRITES);
		
		for (int i = 0; i < sharkImgs.length; i++) {
			for (int j = 0; j < sharkImgs[0].length; j++) {
				sharkImgs[i][j] = atlas.getSubimage(
						j * SHARK_WIDTH_DEFAULT,
						i * SHARK_HEIGHT_DEFAULT,
						SHARK_WIDTH_DEFAULT,
						SHARK_HEIGHT_DEFAULT);
			}
		}
	}
	
	public void loadEnemies(Level lvl) {
		crabbys = lvl.getCrabbys();
		pinkstars = lvl.getPinkstars();
		sharks = lvl.getSharks();
	}
	
	public void update(Player player, int[][] lvlData) {
		for (Crabby c : crabbys) {
			if (c.isAlive()) {
				c.update(player, lvlData);
				checkHitbox(c);
				checkPlayerHitbox(c);
			}
		}
		
		for (Pinkstar p : pinkstars) {
			if (p.isAlive()) {
				p.update(player, lvlData);
				checkHitbox(p);
			}
		}
		
		for (Shark s : sharks) {
			if (s.isAlive()) {
				s.update(player, lvlData);
				checkHitbox(s);
			}
		}
	}
	
	private void checkHitbox(Enemy e) {
		Player player = playing.getPlayer();
		
		if (IsHitting(e.getHitbox(), player.getAttackHitbox()) && e.getCurrentHP() > 0) {
			if (player.isAttacking() && player.getAniIndex() == 1 && player.getAniTick() == 1 && !player.isHit()) {
				e.hurt(playing.getPlayer().getDmg(), playing.getPlayer());
				if (e.getCurrentHP() == 0)
					playing.getLevelManager().getLevelInfo().enemyKilled();
			} else if (player.isPowerAttackActive() && !player.isHit()) {
				e.hurt(e.getMaxHP(), playing.getPlayer());
				playing.getLevelManager().getLevelInfo().enemyKilled();
			}
		}
	}
	
	private void checkPlayerHitbox(Crabby c) {
		if (!(c.getEnemyAction() == Constants.Enemy.Action.HIT))
			if (IsHitting(playing.getPlayer().getHitbox(), c.getAttackHitbox()))
				if (c.getEnemyAction() == Constants.Enemy.Action.ATTACK && c.getAniIndex() == 3 && c.getAniTick() == 0) {
					playing.getPlayer().hurt(c.getDmg(), c);
					playing.getPlayer().setHit(true);
				}
	}
	
	public void draw(Graphics g, int xLvlOffset, int yLvlOffset) {
		drawCrabbys(g, xLvlOffset, yLvlOffset);
		drawPinkstars(g, xLvlOffset, yLvlOffset);
		drawSharks(g, xLvlOffset, yLvlOffset);
	}

	private void drawCrabbys(Graphics g, int xLvlOffset, int yLvlOffset) {
		for (Crabby c : crabbys) {
			if (c.isAlive()) {
				g.drawImage(crabbyImgs[c.getEnemyAction()][c.getAniIndex()],
						(int) c.getHitbox().x - CRABBY_DRAWOFFSET_X - xLvlOffset + c.flipX(),
						(int) c.getHitbox().y - CRABBY_DRAWOFFSET_Y - yLvlOffset,
						CRABBY_WIDTH * c.flipW(), 
						CRABBY_HEIGHT, 
						null);
//				c.drawHitbox(g, xLvlOffset, yLvlOffset);
//				c.drawAttackHitbox(g, xLvlOffset, yLvlOffset);
				c.drawHpBar(g, xLvlOffset, yLvlOffset);
			}
		}
	}
	
	private void drawPinkstars(Graphics g, int xLvlOffset, int yLvlOffset) {
		for (Pinkstar p : pinkstars) {
			if (p.isAlive()) {
				g.drawImage(pinkstarImgs[p.getEnemyAction()][p.getAniIndex()], 
						(int) p.getHitbox().x - PINKSTAR_DRAWOFFSET_X - xLvlOffset + p.flipX(), 
						(int) p.getHitbox().y - PINKSTAR_DRAWOFFSET_Y - yLvlOffset, 
						PINKSTAR_WIDTH * p.flipW(), 
						PINKSTAR_HEIGHT, 
						null);
//				p.drawHitbox(g, xLvlOffset, yLvlOffset);
//				p.drawAttackHitbox(g, xLvlOffset, yLvlOffset);
				p.drawHpBar(g, xLvlOffset, yLvlOffset);
			}
		}
	}
	
	private void drawSharks(Graphics g, int xLvlOffset, int yLvlOffset) {
		for (Shark s : sharks) {
			if (s.isAlive()) {
				g.drawImage(sharkImgs[s.getEnemyAction()][s.getAniIndex()],
						(int) s.getHitbox().x - SHARK_DRAWOFFSET_X - xLvlOffset + s.flipX(),
						(int) s.getHitbox().y - SHARK_DRAWOFFSET_Y - yLvlOffset,
						SHARK_WIDTH * s.flipW(),
						SHARK_HEIGHT,
						null);
//				s.drawHitbox(g, xLvlOffset, yLvlOffset);
//				s.drawAttackHitbox(g, xLvlOffset, yLvlOffset);
				s.drawHpBar(g, xLvlOffset, yLvlOffset);
			}
		}
	}
	
	public void reset() {
		for (Crabby c : crabbys)
			c.reset();
		for (Pinkstar p : pinkstars)
			p.reset();
		for (Shark s : sharks)
			s.reset();
	}
	
	public ArrayList<Crabby> getCrabbys(){
		return crabbys;
	}
	
	public ArrayList<Pinkstar> getPinkstars(){
		return pinkstars;
	}

	public ArrayList<Shark> getSharks() {
		return sharks;
	}
	
	public BufferedImage getEnemyImg() {
		return pinkstarImgs[0][0];
	}
	
}
