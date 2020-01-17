package connectedBalls;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Bany extends BufferedImage {
	
	private int x;
	private int y;
	private boolean buit;

	public Bany(int x, int y, int ample, int alt) {
		super(ample, alt, BufferedImage.TYPE_3BYTE_BGR);
		this.x = x;
		this.y = y;
		this.buit = true;
	}
	
	public synchronized void entrar() {
		while (!buit) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		buit = false;
		notifyAll();
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public void pintar(Graphics g) {
		if (buit) g.setColor(Color.GREEN);
		else g.setColor(Color.RED);
		g.fillRect(x, y, this.getWidth(), this.getHeight());
		
		g.setColor(Color.WHITE);
		g.drawString("WC", x, y + this.getHeight() / 2);
	}
	
	public synchronized void sortir() {
		buit = true;
		notifyAll();
	}

}
