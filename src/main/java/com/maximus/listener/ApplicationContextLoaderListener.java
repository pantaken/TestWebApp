package com.maximus.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.maximus.util.ConstantUtil;



public class ApplicationContextLoaderListener extends ContextLoaderListener {
	@Override
	public void contextInitialized(ServletContextEvent event) {
		super.contextInitialized(event);
		ServletContext sc = event.getServletContext();
		ApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(sc);
		String contextName = sc.getServletContextName();
		String realPath = sc.getRealPath("/");
		String contextPath = sc.getContextPath();
		String serverInfo = sc.getServerInfo();
		
		ConstantUtil.getInstance().setContextName(contextName);
		ConstantUtil.getInstance().setRealPath(realPath);
		ConstantUtil.getInstance().setContextPath(contextPath);
		ConstantUtil.getInstance().setServerInfo(serverInfo);
		ConstantUtil.getInstance().setCtx(ctx);
	}
}
