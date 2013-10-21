package org.n3r.flume.client.common;

import org.n3r.flume.client.net.FlumeConnector;

public class ThreadSendMessage implements Runnable {

	private final FlumeConnector connector;
	private final String message;

	public ThreadSendMessage(FlumeConnector connector, String message) {
		this.connector = connector;
		this.message = message;
	}

	@Override
	public void run() {
		connector.sendMessage(message);
	}
}
