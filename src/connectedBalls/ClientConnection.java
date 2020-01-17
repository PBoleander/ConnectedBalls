package connectedBalls;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;

public class ClientConnection implements Runnable {
	
	private RemoteBall remote;
	private final String HOST = "localhost";
	private int port;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private Socket socket;
	private ServerConnection server;

	public ClientConnection(RemoteBall rb, ServerConnection s) {
		remote = rb;
		server = s;
		new Thread(this).start();
	}

	@Override
	public void run() {
		while (true) {
			try {
				while (!server.isPortFixed()) {
					Thread.sleep(100);
				}
				port = (server.getPort() == 5000 ? 5001 : 5000);
				while (true) {
					if (remote.getSocket() == null) {
						socket = new Socket(HOST, port);
						out = new ObjectOutputStream(socket.getOutputStream());
						in = new ObjectInputStream(socket.getInputStream());
						if (remote.getSocket() == null)
							remote.setSocket(socket, out, in);
					}
					Thread.sleep(1000);
				}
			} catch (ConnectException c) {
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
