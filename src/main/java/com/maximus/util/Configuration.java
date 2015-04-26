package com.maximus.util;

import java.util.ResourceBundle;

public class Configuration {

	private static Configuration conf;
	private final static String CONFIG_FILE = "config";
	private ResourceBundle bundle;
	private Configuration() {
		//Forbidden Constructor func
		this.bundle = ResourceBundle.getBundle(CONFIG_FILE);
	}
	public static Configuration getInstance() {
		if (conf == null) 
			return new Configuration();
		else 
			return conf;
	}
	public String getValue(String key) {
		if (this.bundle == null) {
			return null;
		} else {
			return bundle.getString(key);
		}
	}
//	public static void main(String[] args) {
//		String a = Configuration.getInstance().getValue("msmf2imgScale");
//		System.out.println(a);
//	}
}
