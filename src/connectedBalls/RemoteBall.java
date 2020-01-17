package connectedBalls;

import java.awt.Color;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class RemoteBall implements Runnable {
	
	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private ConnectedBalls cb;
	private HealthSurveillor hs;

	public RemoteBall(ConnectedBalls cb) {
		this.cb = cb;
		hs = new HealthSurveillor(this);
		new Thread(this).start();
	}
	
	public synchronized Socket getSocket() {
		return socket;
	}
	
	@Override
	public void run() {
		while (true) {
			try {
				if (socket != null) {
					rebre();
				} else {
					Thread.sleep(10);
				}
			} catch (InterruptedException | ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) { // si falla rebre
				try {
					if (socket != null)
						socket.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				socket = null;
			}
		}
	}
	
	public synchronized void setSocket(Socket s, ObjectOutputStream o, ObjectInputStream i) {
		socket = s;
		out = o;
		in = i;
	}

	public synchronized void enviarMobil(Mobil mobil) throws IOException {
		Message m = new Message(mobil.getX(), mobil.getY(), mobil.getDireccio(), mobil.getVelocitat(), mobil.getColor());
		out.writeObject(m);
	}
	
	public synchronized void enviarMissatgeSalut() throws IOException {
		Message m = new Message(0, 0, 0, 0, Color.BLACK);
		out.writeObject(m);
	}
	
	private synchronized void enviarRespostaSalut() throws IOException {
		Message m = new Message(-1, -1, -1, -1, Color.BLACK);
		out.writeObject(m);
	}
	
	private void rebre() throws ClassNotFoundException, IOException {
		Message m = (Message) in.readObject();
		if (m.getVelocitat() > 0) {
			Mobil mobil = new Mobil(m.getX(), m.getY(), m.getDireccio(), m.getVelocitat(), m.getColor());
			cb.afegirMobil(mobil);
		} else if (m.getVelocitat() == 0) {
			enviarRespostaSalut();
		} else {
			hs.setRespostaPing(true);
		}
	}
}
