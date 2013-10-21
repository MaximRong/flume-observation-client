package org.n3r.flume.client.panel.logpanel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.MatteBorder;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.n3r.flume.client.panel.LogMainPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.io.Resources;

public class LogLabel extends JPanel {

	private static Logger log = LoggerFactory.getLogger(LogLabel.class);

	private static final long serialVersionUID = 5198764773917079460L;
	private final LogMainPanel logMainPanel;
	private final String logName;

	private JLabel image;
	private JLabel logNameLabel;
	private JLabel viewLabel;
	private JLabel downloadLabel;

	private final Font normalFont = new Font("楷体", Font.PLAIN, 14);
	private final Font boldFont = new Font("楷体", Font.BOLD, 14);

	private final Color noramlLabelColor = new Color(138, 151, 123);
	private final Color mouseOnLabelColor = new Color(0, 49, 79);

	public LogLabel(LogMainPanel bottomPanel, String logName) {
		this.logMainPanel = bottomPanel;
		this.logName = logName;
		initialize();
	}

	private void initialize() {
		setPreferredSize(new Dimension(20, 50));
		setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 0.1;
		c.weighty = 1;
		image = new JLabel(new ImageIcon(
				Resources.getResource("images/file-24.png")), JLabel.RIGHT);
		image.setBackground(Color.red);
		add(image, c);

		c.weightx = 0.5;
		c.gridx = 1;
		c.gridwidth = 5;
		logNameLabel = new JLabel(logName, JLabel.LEFT);
		logNameLabel.setFont(new Font("华文新魏", Font.PLAIN, 16));
		logNameLabel.setPreferredSize(new Dimension(130, 40));
		add(logNameLabel, c);

		c.gridx = 6;
		c.weightx = 0.1;
		c.gridwidth = 1;
		viewLabel = new JLabel("查看", JLabel.RIGHT);
		viewLabel.setFont(normalFont);
		viewLabel.setForeground(noramlLabelColor);
		viewLabel.addMouseListener(new LabelMouseListener("FILE_CONTENT^"
				+ logName + ".log"));
		add(viewLabel, c);

		c.gridx = 7;
		downloadLabel = new JLabel("下载", JLabel.CENTER);
		downloadLabel.setFont(normalFont); // 196 226 216
		downloadLabel.setForeground(noramlLabelColor);
		downloadLabel.addMouseListener(new LabelMouseListener(
				"SEARCH_FILE_CONTENT^" + logName + ".log"));
		add(downloadLabel, c);

		setBackground(new Color(182, 194, 154));
		setBorder(new MatteBorder(0, 0, 1, 0, new Color(138, 151, 125)));
	}

	private void clearOldTrack() {
		// 退出之前的链接
		logMainPanel.clientQuit();

		// 清空显示面板，并且设定当前操作文件名
		final LogViewPanel viewPanel = logMainPanel.getCenter();
		viewPanel.clearLogViews();
		logMainPanel.setCurrentLogName(logName);
	}

	class LabelMouseListener extends MouseAdapter {
		private final String message;

		public LabelMouseListener(String message) {
			this.message = message;
		}

		@Override
		public void mouseEntered(MouseEvent event) {
			Component label = event.getComponent();
			label.setFont(boldFont);
			label.setForeground(mouseOnLabelColor);
			label.setCursor(new Cursor(Cursor.HAND_CURSOR));
		}

		@Override
		public void mouseClicked(MouseEvent event) {

			clearOldTrack();

			final LogViewPanel viewPanel = logMainPanel.getCenter();
			logMainPanel.newFlumeConnector(new IoHandlerAdapter() {

				@Override
				public void messageReceived(IoSession session, Object message)
						throws Exception {
					String str = message.toString();
					viewPanel.append(str);
				}

				@Override
				public void exceptionCaught(IoSession session, Throwable cause)
						throws Exception {
					log.error("show file content has error!", cause);
					session.close(true);
				}

			});

			logMainPanel.sendMessageInThread(message);
		}

		@Override
		public void mouseExited(MouseEvent event) {
			Component label = event.getComponent();
			label.setFont(normalFont);
			label.setForeground(noramlLabelColor);
			label.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
	}

}
