package org.n3r.flume.client.panel.logpanel;

import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

import org.apache.commons.lang3.StringUtils;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.n3r.flume.client.panel.LogMainPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;

public class OperaterPanel extends JPanel {
	private static Logger log = LoggerFactory.getLogger(OperaterPanel.class);

	private static final long serialVersionUID = 7959857606960746796L;
	private JButton searchButton;
	private JTextField searchTextField;
	private final LogMainPanel logMainPanel;

	public OperaterPanel(LogMainPanel logMainPanel) {
		this.logMainPanel = logMainPanel;
		initialize();
	}

	private void initialize() {
		setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		setLayout(new GridBagLayout());
		setPreferredSize(new Dimension(10, 30));

		//
		Border roverBorder = new Border() {

			@Override
			public void paintBorder(Component c, Graphics g, int x, int y,
					int width, int height) {
				g.setColor(Color.gray);
				g.drawRect(x, y, width - 1, height - 1);
			}

			@Override
			public Insets getBorderInsets(Component c) {
				return new Insets(1, 1, 1, 1);
			}

			@Override
			public boolean isBorderOpaque() {
				return true;
			}
		};
		//

		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0.1;
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
		searchButton = new JButton("查询");
		searchButton.setOpaque(false);
		searchButton.setBorder(roverBorder);
		searchButton.setContentAreaFilled(false);
		searchButton.setFocusPainted(false);
		searchButton.setRolloverEnabled(true);

		searchButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				final String searchText = searchTextField.getText();
				if (StringUtils.isEmpty(searchText))
					return;

				String currentLogName = logMainPanel.getCurrentLogName();
				if (StringUtils.isEmpty(currentLogName))
					return;

				clearOldTrack();

				Map<String, String> paramMap = Maps.newHashMap();
				paramMap.put("fileName", currentLogName + ".log");
				paramMap.put("content", searchText);
				String param = JSONObject.toJSONString(paramMap);
				String message = "SEARCH_FILE_CONTENT^" + param;

				final LogViewPanel viewPanel = logMainPanel.getCenter();
				logMainPanel.newFlumeConnector(new IoHandlerAdapter() {

					@Override
					public void messageReceived(IoSession session,
							Object message) throws Exception {
						String str = message.toString();
						viewPanel.append(str);
					}

					@Override
					public void exceptionCaught(IoSession session,
							Throwable cause) throws Exception {
						log.error("search file content has error!", cause);
						session.close(true);
					}

				});

				logMainPanel.sendMessageInThread(message);
			}

		});
		add(searchButton, c);

		c.gridx = 1;
		c.weightx = 0.7;
		searchTextField = new JTextField();
		add(searchTextField, c);

		c.gridx = 2;
		c.weightx = 0.2;
		JPanel empty = new JPanel();
		empty.setMaximumSize(new Dimension(10, 10));
		add(empty, c);

	}

	private void clearOldTrack() {
		// 退出之前的链接
		logMainPanel.clientQuit();
		// 清空显示面板，并且设定当前操作文件名
		final LogViewPanel viewPanel = logMainPanel.getCenter();
		viewPanel.clearLogViews();
	}

}
