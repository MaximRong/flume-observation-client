package org.n3r.flume.client.panel;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.net.InetSocketAddress;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.n3r.flume.client.common.FlumeClientConstant;
import org.n3r.flume.client.net.FlumeConnector;

import com.alibaba.fastjson.JSONArray;

public class HistoryMainPanel extends JPanel {
	private static final long serialVersionUID = 8782318987748165447L;

	public HistoryMainPanel(GridBagLayout gridBagLayout) {
		super(gridBagLayout);
		initialize();
	}

	private void initialize() {
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 0;
		c.gridwidth = 5;
		c.fill = GridBagConstraints.HORIZONTAL;
		JLabel label = new JLabel("历史压缩文件", JLabel.CENTER);
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
		connector.sendMessage("GZ_FILE");

		JSONArray treeJSON = JSONArray.parseArray(serverFileFolderStr
				.toString());
		c.gridwidth = 1;
		int index = 0;
		for (; index < treeJSON.size(); index++) {
			int x = index % 5;
			int y = index / 5;
			c.gridx = x;
			c.gridy = y + 1;
			String fileName = treeJSON.getString(index);
			add(new GzFileJPanel(fileName), c);
		}

		final int remained = 5 - (index % 5);
		if (0 != remained) {
			c.gridx = index % 5;
			c.gridy = index / 5 + 1;
			c.gridwidth = remained;
//			JPanel em = ;
			add(new JPanel(), c);
//			em.setBorder(new MatteBorder(1, 1, 1, 1, new Color(138, 151, 125)));
		}

		c.gridx = 0;
		c.gridy = index / 5 + 2;
		c.weightx = 1;
		c.weighty = 1;
		c.gridwidth = 5;
		c.fill = GridBagConstraints.BOTH;
//		JPanel em2 = new JPanel();
		add(new JPanel(), c);
//		em2.setBorder(new MatteBorder(1, 1, 1, 1, new Color(138, 151, 125)));

	}

}
