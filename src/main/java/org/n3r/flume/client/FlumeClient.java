package org.n3r.flume.client;

import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.n3r.flume.client.panel.BottomPanel;
import org.n3r.flume.client.panel.TopPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlumeClient {

	private static Logger log = LoggerFactory.getLogger(FlumeClient.class);

	private JFrame frame;
	private JPanel topPanel;
	private BottomPanel bottomPanel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					FlumeClient window = new FlumeClient();
					window.frame.setVisible(true);
				} catch (Exception e) {
					log.error("client run error!", e);
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public FlumeClient() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		JFrame.setDefaultLookAndFeelDecorated(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int screenWidth = screenSize.width;
		frame.setBounds((screenWidth / 2 - 550), 10, 1100, 700);
		frame.setResizable(false);

		Container mainPanel = frame.getContentPane();
		mainPanel.setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0.1;
		c.weighty = 0.08;
		topPanel = new TopPanel(this);
		mainPanel.add(topPanel, c);

		c.gridx = 0;
		c.gridy = 1;
		c.weighty = 0.92;
		bottomPanel = new BottomPanel(new CardLayout());
		mainPanel.add(bottomPanel, c);
	}

	public JFrame getFrame() {
		return frame;
	}

	public void setFrame(JFrame frame) {
		this.frame = frame;
	}

	public JPanel getTopPanel() {
		return topPanel;
	}

	public void setTopPanel(JPanel topPanel) {
		this.topPanel = topPanel;
	}

	public BottomPanel getBottomPanel() {
		return bottomPanel;
	}

	public void setBottomPanel(BottomPanel bottomPanel) {
		this.bottomPanel = bottomPanel;
	}

}
