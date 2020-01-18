package connectedBalls;

import java.io.IOException;

public class HealthSurveillor implements Runnable {
	
	private RemoteBall remote;
	private boolean respostaPing;
	
	public HealthSurveillor(RemoteBall remote) {
		this.remote = remote;
		new Thread(this).start();
	}

	@Override
	public void run() {
		while (true) {
			try {
				if (remote.getSocket() != null) {
					respostaPing = false;
					remote.enviarPing();
					Thread.sleep(500);
					if (remote.getSocket() != null && !respostaPing) {
						remote.getSocket().close();
						remote.setSocket(null, null, null);
					}
				}
				Thread.sleep(500);
			} catch (IOException e) { // si falla enviar missatge o tancar socket (redundant)
				try {
					if (remote.getSocket() != null) {
						remote.getSocket().close();
						remote.setSocket(null, null, null);
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void setRespostaPing(boolean resposta) {
		this.respostaPing = resposta;
	}
}
