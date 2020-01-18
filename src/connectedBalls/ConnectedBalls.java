package connectedBalls;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JFrame;

public class ConnectedBalls extends JFrame implements Runnable {

	private static final long serialVersionUID = 1L;
	
	private ArrayList<Bany> llistaBanys;
	private ArrayList<Mobil> llistaMobils;
	private ControlPanel controlPanel;
	private ControlBanys controlBanys;
	private RemoteBall remote;
	private Viewer viewer;
	
	private int distanciaEntreBanys;
	private int frequencia;
	private int numMaxMobils;
	private int numMinBanys;
	private int numBanys;
	private int numMaxBanys;
	private int ampleBanys;
	private int altBanys;
	private int velocitatMinMobils;
	private int velocitatMaxMobils;
	private boolean reinici;
	private boolean ferBanys;
	private boolean ferMobils;
	
	public ConnectedBalls() {
		super();
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setMinimumSize(new Dimension(600, 400));
		this.setLayout(new GridBagLayout());
		
		remote = new RemoteBall(this);
		ServerConnection server = new ServerConnection(remote);
		new ClientConnection(remote, server);
		
		this.llistaBanys = new ArrayList<>();
		this.llistaMobils = new ArrayList<>();
		this.distanciaEntreBanys = 20;
		this.frequencia = 100;
		this.numMaxMobils = 5;
		this.numMinBanys = 1;
		this.numMaxBanys = calculMaxBanys();
		this.numBanys = (this.numMaxBanys > 4 ? 5 : (this.numMaxBanys + this.numMinBanys) / 2);
		this.ampleBanys = 20;
		this.altBanys = 20;
		this.velocitatMinMobils = 1;
		this.velocitatMaxMobils = 3;
		this.ferBanys = false;
		this.ferMobils = false;
		
		this.viewer = new Viewer(llistaBanys, llistaMobils);
		this.controlPanel = new ControlPanel(this);
		GridBagConstraints c = new GridBagConstraints();
		
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 0.0;
		c.weighty = 1.0;
		
		this.add(controlPanel, c);
		
		c.weightx = 1.0;
		this.add(viewer, c);
		
		this.pack();
						
		new Thread(viewer).start();
		new Thread(this).start();
	}
	
	@Override
	public void run() {
		while (true) {
			try {
				while (true) {
					if (ferBanys) {
						crearBanys();
						controlBanys = new ControlBanys(llistaBanys);
					}
					reinici = false;
					while (!reinici) {
						if (ferMobils &&
							llistaMobils.size() < numMaxMobils &&
							(int) (frequencia * Math.random()) == 0) {
							Mobil m = crearMobil();
							if (ferBanys) m.setControlBanys(controlBanys);
							llistaMobils.add(m);
						}
						
						enviarMobilsForaViewer();
						eliminarBanysForaViewer(); // per si es redimensiona finestra
						repaint();
						
						Thread.sleep(10);
					}
					resetejar();
					repaint();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException e) { // si falla enviar
				try {
					if (remote.getSocket() != null) {
						remote.getSocket().close();
						remote.setSocket(null, null, null);
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
	
	public synchronized ArrayList<Bany> getLlistaBanys() {
		return llistaBanys;
	}

	public synchronized void setLlistaBanys(ArrayList<Bany> llistaBanys) {
		this.llistaBanys = llistaBanys;
	}

	public synchronized ArrayList<Mobil> getLlistaMobils() {
		return llistaMobils;
	}

	public synchronized void setLlistaMobils(ArrayList<Mobil> llistaMobils) {
		this.llistaMobils = llistaMobils;
	}

	public synchronized RemoteBall getRemote() {
		return remote;
	}

	public synchronized void setRemote(RemoteBall remote) {
		this.remote = remote;
	}

	public synchronized int getFrequencia() {
		return frequencia;
	}

	public synchronized void setFrequencia(int frequencia) {
		this.frequencia = frequencia;
	}

	public synchronized int getNumMaxMobils() {
		return numMaxMobils;
	}

	public synchronized void setNumMaxMobils(int numMaxMobils) {
		this.numMaxMobils = numMaxMobils;
	}

	public synchronized int getNumMinBanys() {
		return numMinBanys;
	}

	public synchronized void setNumMinBanys(int numMinBanys) {
		this.numMinBanys = numMinBanys;
	}

	public synchronized int getNumMaxBanys() {
		return numMaxBanys;
	}

	public int getNumBanys() {
		return numBanys;
	}

	public synchronized void setNumBanys(int numBanys) {
		this.numBanys = numBanys;
	}

	public synchronized void setNumMaxBanys(int numMaxBanys) {
		this.numMaxBanys = numMaxBanys;
	}

	public synchronized int getAmpleBanys() {
		return ampleBanys;
	}

	public synchronized void setAmpleBanys(int ampleBanys) {
		this.ampleBanys = ampleBanys;
	}

	public synchronized int getAltBanys() {
		return altBanys;
	}

	public synchronized void setAltBanys(int altBanys) {
		this.altBanys = altBanys;
	}

	public synchronized int getVelocitatMinMobils() {
		return velocitatMinMobils;
	}

	public synchronized void setVelocitatMinMobils(int velocitatMinMobils) {
		this.velocitatMinMobils = velocitatMinMobils;
	}

	public synchronized int getVelocitatMaxMobils() {
		return velocitatMaxMobils;
	}

	public synchronized void setVelocitatMaxMobils(int velocitatMaxMobils) {
		this.velocitatMaxMobils = velocitatMaxMobils;
	}

	public synchronized boolean isReinici() {
		return reinici;
	}

	public synchronized void setReinici(boolean reinici) {
		this.reinici = reinici;
	}

	public synchronized boolean isFerBanys() {
		return ferBanys;
	}

	public synchronized void setFerBanys(boolean ferBanys) {
		this.ferBanys = ferBanys;
	}

	public synchronized boolean isFerMobils() {
		return ferMobils;
	}

	public synchronized void setFerMobils(boolean ferMobils) {
		this.ferMobils = ferMobils;
	}

	public void afegirMobil(Mobil m) {
		switch (m.getDireccio()) {
		case 0:
			m.setX(0);
			break;
		case 1:
			m.setY(0);
			break;
		case 2:
			m.setX(viewer.getWidth() - 1);
			break;
		case 3:
			m.setY(viewer.getHeight() - 1);
		}
		if (ferBanys) m.setControlBanys(controlBanys);
		llistaMobils.add(m);
	}
	
	private int calculMaxBanys() {
		// empram el tamany del frame i no del viewer perquè encara no sabem el del viewer
		int numMaxHoritzontal = this.getWidth() / (this.ampleBanys + this.distanciaEntreBanys);
		int numMaxVertical = this.getHeight() / (this.altBanys + this.distanciaEntreBanys);
		
		return numMaxHoritzontal * numMaxVertical / 20; // dividim per 20 perquè provant és el número que millor va, si fos major no aniria malament
	}
	
	private void crearBanys() {
		int x, y;
		for (int i = 0; i < numBanys; i++) {
			do {
				x = (int) (this.distanciaEntreBanys + (viewer.getWidth() - 2 * this.distanciaEntreBanys + 1 - this.ampleBanys) * Math.random()); // x entre distanciaEntreBanys i width - distanciaEB
				y = (int) (this.distanciaEntreBanys + (viewer.getHeight() - 2 * this.distanciaEntreBanys + 1 - this.altBanys) * Math.random()); // y entre distanciaEB i height - distanciaEB
			} while (intersectaAmbBany(x, y, ampleBanys, altBanys, this.distanciaEntreBanys));
		
			llistaBanys.add(new Bany(x, y, ampleBanys, altBanys));
		}
	}
	
	private Mobil crearMobil() {
		int x, y;
		int velocitat = (int) (velocitatMinMobils + (velocitatMaxMobils - velocitatMinMobils + 1) * Math.random());
		int direccio = (int) (4 * Math.random());
		Color color = new Color((int) (256 * Math.random()),
								(int) (256 * Math.random()),
								(int) (256 * Math.random()));
		switch (direccio) {
		case 0: // cap a la dreta
			x = 0;
			y = (int) (viewer.getHeight() * Math.random());
			break;
		case 1: // cap abaix
			x = (int) (viewer.getWidth() * Math.random());
			y = 0;
			break;
		case 2: // cap a l'esquerra
			x = viewer.getWidth() - 1;
			y = (int) (viewer.getHeight() * Math.random());
			break;
		default: // case 3: cap amunt
			x = (int) (viewer.getWidth() * Math.random());
			y = viewer.getHeight() - 1;
		}
		
		return new Mobil(x, y, direccio, velocitat, color);
	}
	
	private void eliminarTotsMobils() {
		for (int i = 0; i < llistaMobils.size(); i++) {
			Mobil m = llistaMobils.get(i);
			eliminarMobil(m);
		}
	}
	
	private void eliminarBanysForaViewer() {
		for (int i = 0; i < llistaBanys.size(); i++) {
			Bany b = llistaBanys.get(i);
			if (!viewer.contePunt(b.getX(), b.getY())) {
				llistaBanys.remove(b);
			}
		}
	}
	
	private void eliminarMobil(Mobil m) {
		m.setViu(false);
		llistaMobils.remove(m);
	}
	
	private void enviarMobilsForaViewer() throws IOException {
		for (int i = 0; i < llistaMobils.size(); i++) {
			Mobil m = llistaMobils.get(i);
			if (!viewer.contePunt(m.getX(), m.getY())) {
				if (remote.getSocket() != null) remote.enviarMobil(m);
				eliminarMobil(m);
			}
		}
	}
	
	private boolean intersectaAmbBany(int x, int y, int ample, int alt, int distanciaEntreBanys) {
		for (Bany b: llistaBanys) {
			Rectangle bany = new Rectangle(b.getX(), b.getY(), b.getWidth(), b.getHeight());
			Rectangle self = new Rectangle(x - distanciaEntreBanys, y - distanciaEntreBanys, ample + 2 * distanciaEntreBanys, alt + 2 * distanciaEntreBanys);
			if (self.intersects(bany)) return true;
		}
		return false;
	}
	
	private void resetejar() {
		eliminarTotsMobils();
		for (Bany b: llistaBanys) b.sortir(); // per si qualque bany té cua quan s'elimina, els mòbils que esperen no moririen mai
		llistaBanys.clear();
	}

	public static void main(String[] args) {
		new ConnectedBalls();
	}
}
