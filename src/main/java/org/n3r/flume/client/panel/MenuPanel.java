package org.n3r.flume.client.panel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.n3r.flume.client.FlumeClient;
import org.n3r.flume.client.common.FlumeClientConstant;

public class MenuPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private final FlumeClient flumeClient;
	private JLabel logLabel;
	private JLabel historyLabel;

	private static final Color DEFAULT_COLOR = new Color(100, 107, 48);
	private static final Color CHOOSE_COLOR = new Color(53, 44, 10);

	public MenuPanel(FlumeClient flumeClient) {
		this.flumeClient = flumeClient;
		initialize();
	}

	private void initialize() {
		setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0.4;
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
		JPanel emptyLeft = new JPanel();
		emptyLeft.setBackground(DEFAULT_COLOR);
		add(emptyLeft, c);

		c.gridx = 1;
		c.weightx = 0.1;
		logLabel = creatNewLabel("日志", "log", c);
		logLabel.setBackground(CHOOSE_COLOR);

		c.gridx = 2;
		c.weightx = 0.1;
		historyLabel = creatNewLabel("历史", "history", c);
		historyLabel.setBackground(DEFAULT_COLOR);

		c.gridx = 3;
		c.weightx = 0.4;
		JPanel emptyRight = new JPanel();
		emptyRight.setBackground(DEFAULT_COLOR);
		add(emptyRight, c);

		setPreferredSize(new Dimension(10, 10));
		setBackground(DEFAULT_COLOR);
	}

	private JLabel creatNewLabel(String name, String command,
			GridBagConstraints c) {
		JLabel label = new JLabel(name, JLabel.CENTER);
		label.setForeground(Color.WHITE);
		label.setFont(new Font("微软雅黑", Font.BOLD, 16));
		label.addMouseListener(new MenuMouseListener(command));
		label.setOpaque(true);

		add(label, c);
		return label;
	}

	class MenuMouseListener extends MouseAdapter {

		private final String commond;

		public MenuMouseListener(String commond) {
			this.commond = commond;
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			final Component evenLabel = e.getComponent();
			Component[] components = evenLabel.getParent().getComponents();
			for (Component component : components) {
				if (component instanceof JPanel)
					continue;
				JLabel label = (JLabel) component;
				label.setBackground(DEFAULT_COLOR);
			}

			if ("log".equals(commond)) {
				logLabel.setBackground(CHOOSE_COLOR);
			} else if ("history".equals(commond)) {
				historyLabel.setBackground(CHOOSE_COLOR);
			}

			BottomPanel bottomPanel = flumeClient.getBottomPanel();
			bottomPanel.showPanel(bottomPanel, commond);

		}

		@Override
		public void mouseEntered(MouseEvent e) {
			final Component evenLabel = e.getComponent();
			evenLabel.setCursor(FlumeClientConstant.HAND_CURSOR);
		}

		@Override
		public void mouseExited(MouseEvent e) {
			final Component evenLabel = e.getComponent();
			evenLabel.setCursor(FlumeClientConstant.DEFAULT_CURSOR);
		}

	}
}
