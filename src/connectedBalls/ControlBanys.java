package connectedBalls;

import java.awt.Rectangle;
import java.util.ArrayList;

public class ControlBanys {
	
	private ArrayList<Bany> llistaBanys;
	
	public ControlBanys(ArrayList<Bany> banys) {
		this.llistaBanys = banys;
	}
	
	public Bany intersectaAmbBany(int x, int y, int ample, int alt) {
		for (int i = 0; i < llistaBanys.size(); i++) {
			Bany b = llistaBanys.get(i);
			Rectangle bany = new Rectangle(b.getX(), b.getY(), b.getWidth(), b.getHeight());
			Rectangle self = new Rectangle(x, y, ample, alt);
			if (self.intersects(bany)) return b;
		}
		return null;
	}

}
