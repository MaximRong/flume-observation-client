package org.n3r.flume.client.panel;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;
import javax.swing.border.MatteBorder;

import org.n3r.flume.client.FlumeClient;

public class PageTopPanel extends JPanel {

	private static final long serialVersionUID = -8609277126041570131L;
	private final FlumeClient flumeClient;

	public PageTopPanel(FlumeClient flumeClient) {
		this.flumeClient = flumeClient;
		initialize();
	}

	private void initialize() {
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 0.7;
		c.fill = GridBagConstraints.BOTH;
		JPanel noticePanel = new JPanel();
		noticePanel.setBackground(new Color(129, 150, 27));
		add(noticePanel, c);

		c.gridy = 1;
		c.weighty = 0.3;
		MenuPanel menuPanel = new MenuPanel(flumeClient);
		menuPanel.setBorder(new MatteBorder(1, 0, 0, 0, new Color(138, 151, 125)));
		add(menuPanel, c);

	}



}
