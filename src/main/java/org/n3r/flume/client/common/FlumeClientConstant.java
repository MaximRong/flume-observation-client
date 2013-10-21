package org.n3r.flume.client.common;

import java.awt.Cursor;

public interface FlumeClientConstant {

	static String InetSocketAddress = Config.getString("InetSocketAddress");

	static int InetSocketPort = Config.getInt("InetSocketPort");

	static int InetSocketFilePort = Config.getInt("InetSocketFilePort");

	static Cursor HAND_CURSOR = new Cursor(Cursor.HAND_CURSOR);

	static Cursor DEFAULT_CURSOR = new Cursor(Cursor.DEFAULT_CURSOR);

}
