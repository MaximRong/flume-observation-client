package org.n3r.flume.client.common;

import org.n3r.flume.client.common.base.Configable;

public class Config {

	private static Configable configimpl;

	static {
		configimpl = new ConfigableImpl();
	}

	public static String getString(String key) {
		return configimpl.getString(key);
	}

	public static int getInt(String key) {
		return configimpl.getInt(key);
	}

}
