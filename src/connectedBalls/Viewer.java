package connectedBalls;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Viewer extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;
	
	private ArrayList<Bany> banys;
	private ArrayList<Mobil> mobils;

	public Viewer(ArrayList<Bany> banys, ArrayList<Mobil> mobils) {
		this.setBackground(Color.WHITE);
		this.banys = banys;
		this.mobils = mobils;
	}

	@Override
	public void run() {
		while (true) {
			try {
				repaint();
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void paint(Graphics g) {
		g.drawImage(ferBorrador(), 0, 0, this.getWidth(), this.getHeight(), null);
	}
	
	@Override
	public void update(Graphics g) {
		paint(g);
	}
	
	public boolean contePunt(int x, int y) {
		return (x >= 0 && x < this.getWidth() && y >= 0 && y < this.getHeight());
	}
	
	private BufferedImage ferBorrador() {
		BufferedImage borrador = (BufferedImage) createImage(this.getWidth(), this.getHeight());
		Graphics borradorGraphics = borrador.getGraphics();
				
		if (mobils != null) {
			for (int i = 0; i < mobils.size(); i++) { // per a evitar ConcurrentModificationException s'ha de fer així el for, no com el dels banys
				Mobil m = mobils.get(i);
				m.pintar(borradorGraphics);
			}
		}
		
		if (banys != null) {
			for (Bany bany: banys) {
				bany.pintar(borradorGraphics);
			}
		}
		
		return borrador;
	}
}
