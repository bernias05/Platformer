package ui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import helpz.LoadSave;
import static helpz.Constants.UI.SoundButton.*;

public class SoundButton extends Button {

	private BufferedImage[][] soundImgs;
	private int rowIndex, columnIndex;
	private boolean muted;
	
	public SoundButton(int x, int y, int width, int height) {
		super(x, y, width, height);
		loadSoundButtonImgs();
	}
	
	private void loadSoundButtonImgs() {
		BufferedImage atlas = LoadSave.GetSpriteAtlas(LoadSave.PAUSE_SOUND_BUTTON);
		soundImgs = new BufferedImage[2][3];
		
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 2; j++) {
				soundImgs[j][i] = atlas.getSubimage(i * B_SOUND_SIZE_DEFAULT, j * B_SOUND_SIZE_DEFAULT, B_SOUND_SIZE_DEFAULT, B_SOUND_SIZE_DEFAULT);
			}
		}
	}
	
	public void update() {
		if (muted)
			rowIndex = 1;
		else
			rowIndex = 0;
		
		columnIndex = 0;
		if (mouseOver)
			columnIndex = 1;
		if (mousePressed)
			columnIndex = 2;
	}
	
	public void draw(Graphics g) {
		g.drawImage(soundImgs[rowIndex][columnIndex], bounds.x, bounds.y, bounds.width, bounds.height, null);
	}

	public boolean isMuted() {
		return muted;
	}

	public void setMuted(boolean muted) {
		this.muted = muted;
	}

}
