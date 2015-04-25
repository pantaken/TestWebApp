package com.maximus.util;

import java.util.UUID;

public class RandomToolkit {
	
	public static String getId(boolean isSplit) {
		UUID uuid = UUID.randomUUID();
		return isSplit ?  uuid.toString() : uuid.toString().replace("-", "");
	}
}
