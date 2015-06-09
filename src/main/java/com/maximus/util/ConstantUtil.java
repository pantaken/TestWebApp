package com.maximus.util;

import org.springframework.context.ApplicationContext;


public class ConstantUtil {
	private static ConstantUtil constantUtil = null;
	private ConstantUtil() {}
	static {
		if (null == constantUtil)
			constantUtil = new ConstantUtil();
	}

	public static ConstantUtil getInstance() {
		return constantUtil;
	}
	/**
	 * let
	 */
	public final static String UTF_8 = "UTF-8";
	public final static String ISO_8859_1 = "ISO-8859-1";
	public final static String SUCCESS = "success";
	public final static String MSG = "message";
	/**
	 * 10M
	 */
	public final static int MAX_FILE_SIZE = 1024*1024*10;
	public final static String SEPARATOR = System.getProperty("file.separator");
	public static final String UPLOAD_DIR = "resources/";
	public static final String TEMPLATE_DIR = "template/";
	
	/**
	 * var
	 */
	private String contextName;
	private String realPath;
	private String contextPath;
	private String serverInfo;
	private ApplicationContext ctx;
	private String basePath;
	public String getContextName() {
		return contextName;
	}
	public void setContextName(String contextName) {
		this.contextName = contextName;
	}
	public String getRealPath() {
		return realPath;
	}
	public void setRealPath(String realPath) {
		this.realPath = realPath;
	}
	public String getContextPath() {
		return contextPath;
	}
	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}
	public String getServerInfo() {
		return serverInfo;
	}
	public void setServerInfo(String serverInfo) {
		this.serverInfo = serverInfo;
	}
	public ApplicationContext getCtx() {
		return ctx;
	}
	public void setCtx(ApplicationContext ctx) {
		this.ctx = ctx;
	}
	public String getBasePath() {
		return "http://localhost:8080";
	}
}
