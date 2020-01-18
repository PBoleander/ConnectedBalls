package connectedBalls;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Mobil extends BufferedImage implements Runnable {
		
	private int direccio;
	private boolean viu;
	private int x;
	private int y;
	private int velocitat;
	private Bany meuBany;
	private Color color;
	private ControlBanys controlBanys;
	
	public Mobil(int x, int y, int direccio, int v, Color c) {
		super(10, 10, BufferedImage.TYPE_3BYTE_BGR);
		this.x = x;
		this.y = y;
		this.direccio = direccio;
		this.velocitat = v;
		this.viu = true;
		this.color = c;
		
		new Thread(this).start();
	}
	
	@Override
	public void run() {
		while (viu) {
			try {
				moure();
				if (controlBanys != null) { // per si es connecta i rep mòbils abans de crear controlBanys o no hi ha banys al programa
					mirarSiIntersectaBany();
				}
				if (meuBany != null) {
					Thread.sleep((int) (1000 * Math.random()));
				} else {
					Thread.sleep(10);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
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
	
	public void setControlBanys(ControlBanys cb) {
		this.controlBanys = cb;
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
	
	public void setViu(boolean viu) {
		this.viu = viu;
	}
	
	private void mirarSiIntersectaBany() {
		Bany bany = controlBanys.intersectaAmbBany(x, y, this.getWidth(), this.getHeight());
		if (bany != null) {
			if (meuBany == null) {
				bany.entrar();
				meuBany = bany;
			}
		} else {
			if (meuBany != null) {
				meuBany.sortir();
				meuBany = null;
			}
		}
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
