package org.n3r.flume.client.panel.logpanel;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.n3r.flume.client.panel.LogMainPanel;

public class LogViewPanel extends JPanel {

	private static final long serialVersionUID = -1449387637748900356L;
	private OperaterPanel operaterPanel;
	private JTextArea viewArea;
	private JScrollPane viewScrollPane;
	private LogMainPanel logMainPanel;

	public LogViewPanel() {
		initialize();
	}

	public LogViewPanel(LogMainPanel logMainPanel) {
		this.logMainPanel = logMainPanel;
		initialize();
	}

	private void initialize() {
		setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 0;
		c.fill = GridBagConstraints.HORIZONTAL;

		operaterPanel = new OperaterPanel(logMainPanel);
		add(operaterPanel, c);

		c.fill = GridBagConstraints.BOTH;
		c.gridy = 1;
		c.weighty = 1;
		c.weightx = 0;

		viewArea = new JTextArea();
		viewArea.setLineWrap(true);
		viewArea.setWrapStyleWord(true);
		JScrollBar scroll = new JScrollBar();
		scroll.add(viewArea);
		viewScrollPane = new JScrollPane(viewArea);
		viewScrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		// 一定要设置宽度 否则textArea会撑开全部区域 而且只能设置ScrollPane的大小
		// 如果设置了viewArea的PreferedSize 滚动条出不来，因为你的区域永远到不了Srcoll所需要
		// 显示的滚动条大小
		viewScrollPane.setPreferredSize(new Dimension(50, 50));

		add(viewScrollPane, c);

	}

	public void clearLogViews() {
		viewArea.setText("");
	}

	public void append(String str) {
		viewArea.append(str + "\n");
		viewArea.setCaretPosition(viewArea.getText().length());
	}
}
