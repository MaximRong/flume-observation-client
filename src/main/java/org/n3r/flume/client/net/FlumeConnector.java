package org.n3r.flume.client.net;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.LineDelimiter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

public class FlumeConnector {

	private IoConnector connector;
	private IoSession session;
	private long connectTimeoutMillis = 3000;
	private ConnectorType connectorType;
	private IoHandlerAdapter ioHandlerAdapter;
	private InetSocketAddress inetSocketAddress = new InetSocketAddress(
			"localhost", 9123);

	public FlumeConnector newTextConnector() {
		connectorType = ConnectorType.TextConnector;
		return this;
	}

	public FlumeConnector newFileDownloadConnector() {
		connectorType = ConnectorType.FileDownloadConnector;
		return this;
	}

	public FlumeConnector connectTimeoutMillis(long connectTimeoutMillis) {
		this.connectTimeoutMillis = connectTimeoutMillis;
		return this;
	}

	public FlumeConnector build() {
		connector = new NioSocketConnector();
		connector.setConnectTimeoutMillis(connectTimeoutMillis);
		switch (connectorType) {
		case TextConnector:
			TextLineCodecFactory lineCodec = new TextLineCodecFactory(
					Charset.forName("UTF-8"), LineDelimiter.WINDOWS.getValue(),
					LineDelimiter.WINDOWS.getValue());
			lineCodec.setDecoderMaxLineLength(1024 * 1024); // 1M
			lineCodec.setEncoderMaxLineLength(1024 * 1024);

			connector.getFilterChain().addLast("codec",
					new ProtocolCodecFilter(lineCodec));
			break;
		case FileDownloadConnector :
			connector.getFilterChain();
			break;
		default:
			throw new RuntimeException("you must set the connector type!");
		}

		if (null == ioHandlerAdapter) {
			throw new RuntimeException("you must set the Handler!");
		}
		connector.setHandler(ioHandlerAdapter);

		return this;

	}

	public void sendMessage(Object message) {
		try {
			if (null == message) {
				throw new RuntimeException("you must set the Message!");
			}

			ConnectFuture connFuture = connector.connect(inetSocketAddress);
			connFuture.awaitUninterruptibly();
			session = connFuture.getSession();
			session.write(message);
			session.getCloseFuture().awaitUninterruptibly();
		} finally {
			session.close(true);
			connector.dispose(true);
		}
	}

	enum ConnectorType {
		TextConnector, FileDownloadConnector;
	}

	public FlumeConnector handler(IoHandlerAdapter ioHandlerAdapter) {
		this.ioHandlerAdapter = ioHandlerAdapter;
		return this;
	}

	public FlumeConnector inetSocketAddress(InetSocketAddress inetSocketAddress) {
		this.inetSocketAddress = inetSocketAddress;
		return this;
	}

	public void dispose() {
		session.close(true);
		connector.dispose(true);
	}

	public void clientQuit() {
		session.write("QUIT");
	}

	public boolean isDisposed() {
		return connector.isDisposed();
	}


}
