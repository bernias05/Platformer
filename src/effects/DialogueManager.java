package effects;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import gameStates.Playing;
import helpz.LoadSave;

import static helpz.Constants.Object.Size.*;

public class DialogueManager {
	
	private Playing playing;
	
	private ArrayList<Dialogue> dialogues = new ArrayList<>();
	
	private BufferedImage[] questionImgs, exclamationImgs;
	
	public DialogueManager(Playing playing) {
		this.playing = playing;
		loadImgs();
	}
	
	private void loadImgs() {
		questionImgs = new BufferedImage[5];
		exclamationImgs = new BufferedImage[5];
		
		BufferedImage questionAtlas = LoadSave.GetSpriteAtlas(LoadSave.QUESTIONMARK_SPRITES);
		BufferedImage exclamationAtlas = LoadSave.GetSpriteAtlas(LoadSave.EXCLAMATION_SPRITES);
		
		for (int i = 0; i < 5; i++) {
			questionImgs[i] = questionAtlas.getSubimage(i * PUNCTUATION_MARK_WIDTH_DEFAULT, 0, PUNCTUATION_MARK_WIDTH_DEFAULT, PUNCTUATION_MARK_HEIGHT_DEFAULT);
			exclamationImgs[i] = exclamationAtlas.getSubimage(i * PUNCTUATION_MARK_WIDTH_DEFAULT, 0, PUNCTUATION_MARK_WIDTH_DEFAULT, PUNCTUATION_MARK_HEIGHT_DEFAULT);
		}
	}
	
	public void update() {
		for (Dialogue d : dialogues)
			if (d.isActive())
				d.update();
	}
	
	public void draw(Graphics g, int xLvlOffset, int yLvlOffset) {
		for (Dialogue d : dialogues)
			if (d.isActive())
				d.draw(g, xLvlOffset, yLvlOffset);
	}
	
	public void addQuestionMark(int x, int y) {
		for (Dialogue d : dialogues)
			if (!d.isActive()) {
				d.reuse(questionImgs, x, y);
				return;
			}
		dialogues.add(new Dialogue(questionImgs, x, y));
	}
	
	public void addExclamationMark(int x, int y) {
		for (Dialogue d : dialogues)
			if (!d.isActive()) {
				d.reuse(exclamationImgs, x, y);
				return;
			}
		dialogues.add(new Dialogue(exclamationImgs, x, y));
	}

}
