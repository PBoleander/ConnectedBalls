package connectedBalls;

import java.awt.Color;
import java.io.Serializable;

public class Message implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int x;
	private int y;
	private int velocitat;
	private int direccio;
	private Color color;

	public Message(int x, int y, int d, int v, Color c) {
		this.x = x;
		this.y = y;
		this.direccio = d;
		this.velocitat = v;
		this.color = c;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getVelocitat() {
		return velocitat;
	}

	public int getDireccio() {
		return direccio;
	}

	public Color getColor() {
		return color;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}
}
