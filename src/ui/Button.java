package ui;

import java.awt.Rectangle;

public abstract class Button {

	protected Rectangle bounds;
	protected boolean mouseOver, mousePressed;
	
	public Button(int x, int y, int width, int height) {
		bounds = new Rectangle(x, y, width, height);
	}

	public int getX() {
		return bounds.x;
	}
	
	public void setX(int x) {
		bounds.x = x;
	}
	
	public int getY() {
		return bounds.y;
	}

	public void setY(int y) {
		bounds.y = y;
	}

	public int getWidth() {
		return bounds.width;
	}

	public void setWidth(int width) {
		bounds.width = width;
	}

	public int getHeight() {
		return bounds.height;
	}

	public void setHeight(int height) {
		bounds.height = height;
	}

	public Rectangle getBounds() {
		return bounds;
	}

	public void setBounds(Rectangle bounds) {
		this.bounds = bounds;
	}
	
	public boolean isMouseOver() {
		return mouseOver;
	}

	public void setMouseOver(boolean mouseOver) {
		this.mouseOver = mouseOver;
	}

	public boolean isMousePressed() {
		return mousePressed;
	}

	public void setMousePressed(boolean mousePressed) {
		this.mousePressed = mousePressed;
	}
	
}
