package connectedBalls;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerConnection implements Runnable {
	
	private int port;
	private boolean portFixed;
	private RemoteBall remote;
	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	
	public ServerConnection(RemoteBall rb) {
		remote = rb;
		port = 5000;
		portFixed = false;
		new Thread(this).start();
	}
	
	public int getPort() {
		return port;
	}
	
	public boolean isPortFixed() {
		return portFixed;
	}

	@Override
	public void run() {
		ServerSocket serverSocket = null;
		while (true) {
			try {
				serverSocket = new ServerSocket(port);
				portFixed = true;
				while (true) {
					socket = serverSocket.accept();
					out = new ObjectOutputStream(socket.getOutputStream());
					in = new ObjectInputStream(socket.getInputStream());
					if (remote.getSocket() == null)
						remote.setSocket(socket, out, in);
				}
			} catch (BindException b) {
				port = 5001;
			} catch (IOException e) { // si falla crear serversocket o acceptar
				try {
					if (serverSocket != null) {
						if (remote.getSocket() != null) {
							remote.getSocket().close();
							remote.setSocket(null, null, null);
						}
						serverSocket.close();
						serverSocket = null;
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

}
