package main;

import java.awt.Graphics;

import audio.AudioPlayer;
import gameStates.Editing;
import gameStates.GameOptions;
import gameStates.GameState;
import gameStates.LevelSelect;
import gameStates.Title;
import ui.AudioOptions;
import gameStates.Playing;

public class Game implements Runnable{
	
	private GameWindow gameWindow;
	private GamePanel gamePanel;
	private Thread gameThread;
	
	private Title title;
	private LevelSelect levelSelect;
	private Editing editing;
	private Playing playing;
	private GameOptions options;
	private AudioOptions audioOptions;
	private AudioPlayer audioPlayer;
	
	private final int FPS_SET = 120;
	private final int UPS_SET = 120;

	public static final float SCALE = 2f;
	public static final int DEFAULT_TILE_SIZE = 32;
	public static final int TILES_IN_WIDTH = 26;
	public static final int TILES_IN_HEIGHT = 14;
	public static final int TILE_SIZE = (int) (DEFAULT_TILE_SIZE * SCALE);
	public static final int GAME_WIDTH = TILE_SIZE * TILES_IN_WIDTH;
	public static final int GAME_HEIGHT = TILE_SIZE * TILES_IN_HEIGHT;
	
	public static final float GRAVITY = 0.05f * SCALE;

	public static final float WATER_SLOWING = 0.1f * Game.SCALE;
	public static final float WATER_INERTIA = 0.01f * Game.SCALE;
	public static final float WATER_ACCELERATION = 0.015f * Game.SCALE;
	public static final float MAX_WATER_SPEED = 0.8f * Game.SCALE;
	public static final float SINK_SPEED = 0.1f * Game.SCALE;
	
	public static final int ANI_SPEED = 15;
	
	public Game() {
		initClasses();
		startGameLoop();
	}
	
	private void initClasses() {
		audioOptions = new AudioOptions(this);
		title = new Title(this);
		levelSelect = new LevelSelect(this);
		playing = new Playing(this, audioOptions);
		options = new GameOptions(this, audioOptions);
		audioPlayer = new AudioPlayer(this);
		editing =  new Editing(this);
		
		gamePanel = new GamePanel(this);
		gameWindow = new GameWindow(gamePanel);
		gamePanel.setFocusable(true);
		gamePanel.requestFocus();
	}
	
	private void startGameLoop() {
		gameThread = new Thread(this);
		gameThread.start();
	}
	
	public void update() {
		switch (GameState.state) {
		case TITLE:
			title.update();
			break;
		case LEVEL_SELECT:
			levelSelect.update();
			break;
		case EDITING:
			editing.update();
			break;
		case PLAYING:
			playing.update();
			break;
		case OPTIONS:
			options.update();
			break;
		case QUIT:
			System.exit(0);
			break;
		default:
			break;
		}
	}
	
	public void render(Graphics g) {
		switch (GameState.state) {
		case TITLE:
			title.render(g);
			break;
		case LEVEL_SELECT:
			levelSelect.render(g);
			break;
		case EDITING:
			editing.render(g);
			break;
		case PLAYING:
			playing.render(g);
			break;
		case OPTIONS:
			options.render(g);
			break;
		default:
			break;
		}
	}

	public void windowFocusLost() {
		if (GameState.state == GameState.PLAYING)
			playing.windowFocusLost();
	}

	@Override
	public void run() {
		double timePerFrame = 1000000000.0 / FPS_SET;
		double timePerUpdate = 1000000000.0 / UPS_SET;
		
		int frames = 0;
		int updates = 0;
		
		double deltaUpdate = 0;
		double deltaFrame = 0;

		long lastCheck = System.currentTimeMillis();
		long previousTime = System.nanoTime();
		
		while(true) {
			long currentTime = System.nanoTime();
			
			deltaUpdate += (currentTime - previousTime) / timePerUpdate;
			deltaFrame += (currentTime - previousTime) / timePerFrame;
			previousTime = currentTime;
			
			if (deltaUpdate >= 1) {
				update();
				updates++;
				deltaUpdate--;
			}
			
			if (deltaFrame >= 1) {
				gamePanel.repaint();
				frames++;
				deltaFrame--;
			}
			
			if (System.currentTimeMillis() - lastCheck >= 1000) {
				lastCheck = System.currentTimeMillis();
//				System.out.println("FPS: " + frames + " | UPS: " + updates);
				frames = 0;
				updates = 0;
			}
		}
	}
	
	public Title getTitle() {
		return title;
	}
	
	public LevelSelect getLevelSelect() {
		return levelSelect;
	}
	
	public Editing getEditing() {
		return editing;
	}
	
	public Playing getPlaying() {
		return playing;
	}
	
	public GameOptions getOptions() {
		return options;
	}
	
	public AudioPlayer getAudioPlayer() {
		return audioPlayer;
	}
	
}
