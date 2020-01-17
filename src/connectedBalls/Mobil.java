package connectedBalls;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;

public class Mobil extends BufferedImage implements Runnable {
		
	private int direccio;
	private boolean viu;
	private int x;
	private int y;
	private int velocitat;
	private boolean dinsBany;
	private Color color;
	
	public Mobil(int x, int y, int direccio, int v, Color c) {
		super(10, 10, BufferedImage.TYPE_3BYTE_BGR);
		this.x = x;
		this.y = y;
		this.direccio = direccio;
		this.velocitat = v;
		this.viu = true;
		this.dinsBany = false;
		this.color = c;
		
		new Thread(this).start();
	}
	
	public Color getColor() {
		return color;
	}
	
	public int getDireccio() {
		return direccio;
	}
	
	public synchronized int getX() {
		return x;
	}
	
	public synchronized int getY() {
		return y;
	}
	
	public int getVelocitat() {
		return velocitat;
	}
	
	public synchronized void setDinsBany(boolean dinsBany) {
		this.dinsBany = dinsBany;
	}
	
	public synchronized void setX(int x) {
		this.x = x;
	}
	
	public synchronized void setY(int y) {
		this.y = y;
	}
	
	public void pintar(Graphics g) {
		g.setColor(color);
		g.fillOval(x, y, this.getWidth(), this.getHeight());
	}

	@Override
	public void run() {
		while (viu) {
			try {
				moure();
				if (dinsBany) {
					Thread.sleep((int) (1000 * Math.random()));
				} else {
					Thread.sleep(10);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void setViu(boolean viu) {
		this.viu = viu;
	}
	
	private synchronized void moure() {
		switch (direccio) {
		case 0:
			x += velocitat;
			break;
		case 1:
			y += velocitat;
			break;
		case 2:
			x -= velocitat;
			break;
		case 3:
			y -= velocitat;
			break;
		}
	}
}
