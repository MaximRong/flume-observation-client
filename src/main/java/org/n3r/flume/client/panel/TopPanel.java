package org.n3r.flume.client.panel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.MatteBorder;

import org.n3r.flume.client.FlumeClient;

public class TopPanel extends JPanel {
	private static final long serialVersionUID = 8425341709753321626L;
	private final FlumeClient flumeClient;

	public TopPanel(FlumeClient flumeClient) {
		this.flumeClient = flumeClient;
		initialize();
	}

	private void initialize() {
		setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0.17;
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
		JLabel rocketLogo = new JLabel("Rocket", JLabel.CENTER);
		rocketLogo.setForeground(Color.BLACK);
		rocketLogo.setBackground(new Color(129, 150, 27));
		rocketLogo.setOpaque(true);
		rocketLogo.setFont(new Font("Cooper Black", Font.BOLD | Font.ITALIC, 52));
		rocketLogo.setBorder(new MatteBorder(0, 0, 0, 1, Color.GRAY));
		rocketLogo.setPreferredSize(new Dimension(50, 50));

		add(rocketLogo, c);


		c.fill = GridBagConstraints.BOTH;
		c.gridx = 1;
		c.gridy = 0;
		c.weightx = 0.83;
		c.weighty = 1;
		PageTopPanel pageTopPanel = new PageTopPanel(flumeClient);
		add(pageTopPanel, c);

//		setBorder(new MatteBorder(1, 1, 1, 1, new Color(138, 151, 125)));
		setPreferredSize(new Dimension(50, 50));
	}

}
