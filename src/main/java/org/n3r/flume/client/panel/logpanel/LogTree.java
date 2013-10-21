package org.n3r.flume.client.panel.logpanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.net.InetSocketAddress;
import java.util.Iterator;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.MatteBorder;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.n3r.flume.client.common.FlumeClientConstant;
import org.n3r.flume.client.net.FlumeConnector;
import org.n3r.flume.client.panel.LogMainPanel;

import com.alibaba.fastjson.JSONArray;

public class LogTree extends JPanel {

	private static final long serialVersionUID = 5198764773917079460L;
	private final LogMainPanel bottomPanel;

	public LogTree(LogMainPanel bottomPanel) {
		this.bottomPanel = bottomPanel;
		setLayout(new GridBagLayout());
		initialize();
	}

	private void initialize() {
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.weightx = 1;
		c.weighty = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		JLabel label = new JLabel("日志文件", JLabel.CENTER);
		label.setForeground(Color.WHITE);
		label.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		label.setBackground(new Color(53, 44, 10));
		label.setOpaque(true);
		add(label, c);

		final StringBuilder serverFileFolderStr = new StringBuilder();
		FlumeConnector connector = new FlumeConnector()
				.newTextConnector()
				.inetSocketAddress(
						new InetSocketAddress(
								FlumeClientConstant.InetSocketAddress,
								FlumeClientConstant.InetSocketPort))
				.handler(new IoHandlerAdapter() {
					@Override
					public void messageReceived(IoSession session,
							Object message) throws Exception {
						serverFileFolderStr.append(message.toString());
						session.close(true);
					}
				}).build();
		connector.sendMessage("LOG_FILE");

		c.gridx = 0;
		c.weightx = 1;
		c.weighty = 0;
		c.fill = GridBagConstraints.HORIZONTAL;

		JSONArray treeJSON = JSONArray.parseArray(serverFileFolderStr
				.toString());
		Iterator<Object> iterator = treeJSON.iterator();
		int index = 0;
		while (iterator.hasNext()) {
			String logName = (String) iterator.next();
			c.gridy = 1 + index++;
			add(new LogLabel(bottomPanel, logName), c);
		}

		c.gridx = 0;
		c.gridy = index + 1;
		c.weightx = 1;
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
		JPanel emptyPanel = new JPanel();
		emptyPanel.setBackground(new Color(182, 194, 154));
		add(emptyPanel, c);

		setBorder(new MatteBorder(1, 1, 1, 1, new Color(138, 151, 125)));
		setPreferredSize(new Dimension(20, 20));
	}

}
