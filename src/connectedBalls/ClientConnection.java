package connectedBalls;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;

public class ClientConnection implements Runnable {
	
	private RemoteBall remote;
	private final String HOST;
	private int port;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private Socket socket;
	private ServerConnection server;

	public ClientConnection(RemoteBall rb, ServerConnection s, String host, int port) {
		remote = rb;
		server = s;
		HOST = host;
		this.port = port;
		new Thread(this).start();
	}

	@Override
	public void run() {
		while (true) {
			try {
				while (!server.isPortFixed()) { // espera a que el servidor hagi fixat el seu port (5000 o 5001)
					Thread.sleep(100);
				}
				port = (server.getPort() == port ? port + 1 : port); // el client es connectarà al port que no té el servidor de la seva pantalla
				while (true) {
					if (remote.getSocket() == null) { // si no hi ha connexió, l'intenta establir
						socket = new Socket(HOST, port);
						out = new ObjectOutputStream(socket.getOutputStream());
						in = new ObjectInputStream(socket.getInputStream());
						if (remote.getSocket() == null) // si la connexió no està encara establerta, l'estableix
							remote.setSocket(socket, out, in);
					}
					Thread.sleep(1000);
				}
			} catch (ConnectException c) { // no hi ha servidor al qual connectar-se
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} catch (IOException io) {
				try {
					if (remote.getSocket() != null) {
						remote.getSocket().close();
						remote.setSocket(null, null, null);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
