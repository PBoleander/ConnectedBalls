package connectedBalls;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ControlPanel extends JPanel implements MouseListener, ChangeListener, ItemListener, Runnable {
	
	private static final long serialVersionUID = 1L;
	
	private ConnectedBalls mainProgram;
	
	private JSlider frequencia;
	private JSpinner nMaxMobils, nBanys, ampleBanys, altBanys, velMinMobils, velMaxMobils;
	private JCheckBox cbFerBanys, cbFerMobils;
	private JLabel infoMode, infoSocket;

	public ControlPanel(ConnectedBalls c) {
		super(new GridBagLayout());
		
		this.mainProgram = c;
		
		int y = 0;
		
		GridBagConstraints label = new GridBagConstraints();
		label.gridwidth = GridBagConstraints.REMAINDER;
		GridBagConstraints numeros = new GridBagConstraints();
		numeros.anchor = GridBagConstraints.EAST;
		
		infoMode = afegirLabelNou(0, y++, label);
		infoSocket = afegirLabelNou(0, y++, label);
		
		cbFerBanys = afegirCheckBox("Crear banys", mainProgram.isFerBanys(), 0, y, new GridBagConstraints());
		cbFerMobils = afegirCheckBox("Crear mòbils", mainProgram.isFerMobils(), 1, y++, new GridBagConstraints());
		
		afegirLabelNou("Freqüència", 0, y++, label);
		this.frequencia = afegirSliderNou(this.frequencia, 2, 200, mainProgram.getFrequencia(), 0, y++, 20, true, new GridBagConstraints());
		
		label.gridwidth = 1;
		label.anchor = GridBagConstraints.WEST;
		
		afegirLabelNou("Nº màxim mòbils", 0, y, label);
		this.nMaxMobils = afegirSpinnerNou(this.nMaxMobils, mainProgram.getNumMaxMobils(), 1, 50, 1, 1, y++, numeros);
		
		afegirLabelNou("Nº banys", 0, y, label);
		this.nBanys = afegirSpinnerNou(this.nBanys, mainProgram.getNumBanys(), mainProgram.getNumMinBanys(), mainProgram.getNumMaxBanys(), 1, 1, y++, numeros);
		
		afegirLabelNou("Amplada banys", 0, y, label);
		this.ampleBanys = afegirSpinnerNou(this.ampleBanys, mainProgram.getAmpleBanys(), 20, 50, 5, 1, y++, numeros);
		
		afegirLabelNou("Altura banys", 0, y, label);
		this.altBanys = afegirSpinnerNou(this.altBanys, mainProgram.getAltBanys(), 20, 50, 5, 1, y++, numeros);
		
		afegirLabelNou("Velocitat mínima mòbils", 0, y, label);
		this.velMinMobils = afegirSpinnerNou(this.velMinMobils, mainProgram.getVelocitatMinMobils(), 1, 10, 1, 1, y++, numeros);
		
		afegirLabelNou("Velocitat màxima mòbils", 0, y, label);
		this.velMaxMobils = afegirSpinnerNou(this.velMaxMobils, mainProgram.getVelocitatMaxMobils(), 1, 30, 1, 1, y++, numeros);
		
		new Thread(this).start();
	}

	@Override
	public void mouseClicked(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	@Override
	public void mousePressed(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.getSource().equals(frequencia)) {
			mainProgram.setFrequencia(frequencia.getValue());
			mainProgram.setReinici(true);
		}
	}
	
	@Override
	public void stateChanged(ChangeEvent e) {
		if (e.getSource().equals(nMaxMobils)) {
			mainProgram.setNumMaxMobils((int) nMaxMobils.getValue());
		} else if (e.getSource().equals(nBanys)) {
			mainProgram.setNumBanys((int) nBanys.getValue());
		} else if (e.getSource().equals(ampleBanys)) {
			mainProgram.setAmpleBanys((int) ampleBanys.getValue());
		} else if (e.getSource().equals(altBanys)) {
			mainProgram.setAltBanys((int) altBanys.getValue());
		} else if (e.getSource().equals(velMaxMobils)) {
			mainProgram.setVelocitatMaxMobils((int) velMaxMobils.getValue());
		} else if (e.getSource().equals(velMinMobils)) {
			mainProgram.setVelocitatMinMobils((int) velMinMobils.getValue());
		}
		mainProgram.setReinici(true);
	}
	
	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getSource().equals(cbFerBanys)) {
			mainProgram.setFerBanys(cbFerBanys.isSelected());
			mainProgram.setReinici(true);
		} else if (e.getSource().equals(cbFerMobils)) {
			mainProgram.setFerMobils(cbFerMobils.isSelected());
		}
	}
	
	private JCheckBox afegirCheckBox(String titol, boolean selected, int x, int y, GridBagConstraints c) {
		JCheckBox cb = new JCheckBox(titol, selected);
		cb.addItemListener(this);
		c.gridx = x;
		c.gridy = y;
		c.anchor = GridBagConstraints.WEST;
		this.add(cb, c);
		
		return cb;
	}
	
	private void afegirLabelNou(String titol, int x, int y, GridBagConstraints l) {
		JLabel label = new JLabel(titol);
		l.gridx = x;
		l.gridy = y;
		this.add(label, l);
	}
	
	private JLabel afegirLabelNou(int x, int y, GridBagConstraints l) {
		JLabel label = new JLabel("");
		l.gridx = x;
		l.gridy = y;
		label.setFont(new Font(Font.MONOSPACED, Font.BOLD, 11));
		this.add(label, l);
		
		return label;
	}
	
	private JSlider afegirSliderNou(JSlider slider, int valorMinim, int valorMaxim, int valorInicial, int x, int y, int espaiTicks, boolean enabled, GridBagConstraints s) {
		slider = new JSlider(valorMinim, valorMaxim, valorInicial);
		slider.addMouseListener(this);
		slider.setEnabled(enabled);
		textSliders(slider, espaiTicks);
		s.gridx = x;
		s.gridy = y;
		s.weightx = 1.0;
		s.gridwidth = GridBagConstraints.REMAINDER;
		s.fill = GridBagConstraints.HORIZONTAL;
		this.add(slider, s);
		
		return slider;
	}
	
	private JSpinner afegirSpinnerNou(JSpinner spinner, int valorInicial, int valorMin, int valorMax, int pas, int x, int y, GridBagConstraints s) {
		SpinnerNumberModel model = new SpinnerNumberModel(valorInicial, valorMin, valorMax, pas);
		spinner = new JSpinner(model);
		spinner.addChangeListener(this);
		spinner.setEnabled(true);
		s.gridx = x;
		s.gridy = y;
		s.insets = new Insets(0, 20, 0, 0);
		
		this.add(spinner, s);
		return spinner;
	}
	
	private int calculMode(int localPort, int port) {
		switch (localPort) {
		case 5000:
			return 1;
		case 5001:
			return 2;
		}
		
		switch (port) {
		case 5000:
			return 2;
		case 5001:
			return 1;
		}
		
		return 0; // aquí no hi ha d'arribar
	}
	
	private void textSliders(JSlider s, int espaiTicks) {
		Font f = new Font(Font.MONOSPACED, Font.PLAIN, 12);
		s.setMajorTickSpacing(espaiTicks);
		s.setFont(f);
		s.setPaintTicks(true);
		s.setPaintLabels(true);
	}

	@Override
	public void run() {
		while (true) {
			RemoteBall remote = mainProgram.getRemote();
			if (remote.getSocket() != null) {
				int localPort = remote.getSocket().getLocalPort();
				int port = remote.getSocket().getPort();
				infoMode.setText("Connectat com a mode " + calculMode(localPort, port));
				infoSocket.setText("Port local: " + localPort + " - port: " + port);
			} else {
				infoMode.setText(null);
				infoSocket.setText("Sense connexió");
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
