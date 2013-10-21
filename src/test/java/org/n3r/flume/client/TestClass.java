package org.n3r.flume.client;

import java.net.InetSocketAddress;
import java.net.URL;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.junit.Test;
import org.n3r.flume.client.net.FlumeConnector;

import com.google.common.io.Resources;

public class TestClass {

//	@Test
	public void test() {
		URL resource = Resources.getResource("images/file-24.png");
		System.out.println(resource);
	}

	@Test
	public void client() {

		FlumeConnector connector = new FlumeConnector()
				.newTextConnector()
				.inetSocketAddress(
						new InetSocketAddress("192.168.93.134", 26699)) // 10.142.145.117
				.handler(new IoHandlerAdapter() {
					@Override
					public void messageReceived(IoSession session,
							Object message) throws Exception {
						System.out.println(message.toString());
//						 session.close(true);
					}

					@Override
					public void exceptionCaught(IoSession session,
							Throwable cause) throws Exception {
						cause.printStackTrace();
					}
				}).build();
		new Thread(new LogConsole(connector)).start();
//
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

//		connector.sendMessage("FILE_CONTENT^AOP.log");
//		connector.dispose();
		connector.clientQuit();


		// System.out.println(serverFileFolderStr.toString());

		try {
			Thread.sleep(20000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println("开心");
	}

	class LogConsole implements Runnable {

		private final FlumeConnector connector;

		public LogConsole(FlumeConnector connector) {
			this.connector = connector;
		}

		@Override
		public void run() {
			connector.sendMessage("FILE_CONTENT^AOP.log");
		}

	}
}
