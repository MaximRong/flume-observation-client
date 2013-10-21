package org.n3r.flume.client.panel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.net.InetSocketAddress;

import javax.swing.JPanel;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.n3r.flume.client.common.FlumeClientConstant;
import org.n3r.flume.client.common.ThreadSendMessage;
import org.n3r.flume.client.net.FlumeConnector;
import org.n3r.flume.client.panel.logpanel.LogTree;
import org.n3r.flume.client.panel.logpanel.LogViewPanel;

public class LogMainPanel extends JPanel {

	private static final long serialVersionUID = 4418397631535230822L;
	private JPanel left;
	private LogViewPanel center;
	private String currentLogName;

	private FlumeConnector connector;

	public LogMainPanel() {
		initialize();
	}

	public LogMainPanel(GridBagLayout gridBagLayout) {
		super(gridBagLayout);
		initialize();
	}

	private void initialize() {
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0.2;
		c.weighty = 1;
		c.fill = GridBagConstraints.BOTH;
		left = new LogTree(this);
		add(left, c);

		c.gridx = 1;
		c.gridy = 0;
		c.weightx = 0.8;
		c.weighty = 1;
		center = new LogViewPanel(this);
		add(center, c);

	}

	public LogViewPanel getCenter() {
		return center;
	}

	public JPanel getLeft() {
		return left;
	}

	public void setLeft(JPanel left) {
		this.left = left;
	}

	public void setCenter(LogViewPanel center) {
		this.center = center;
	}

	public void setCurrentLogName(String logName) {
		this.currentLogName = logName;
	}

	public String getCurrentLogName() {
		return currentLogName;
	}

	public void clientQuit() {
		if (null != connector && !connector.isDisposed())
			connector.clientQuit();

	}

	public void newFlumeConnector(IoHandlerAdapter handler) {
		connector = new FlumeConnector()
				.newTextConnector()
				.inetSocketAddress(
						new InetSocketAddress(
								FlumeClientConstant.InetSocketAddress,
								FlumeClientConstant.InetSocketPort)) // 10.142.145.117
				.handler(handler).build();

	}

	public void sendMessageInThread(String message) {
		new Thread(new ThreadSendMessage(connector, message)).start();
	}
}
