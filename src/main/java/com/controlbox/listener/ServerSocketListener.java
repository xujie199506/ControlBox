package com.controlbox.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.controlbox.nio.NettyServerBootstrap;
 


public class ServerSocketListener implements ServletContextListener {

	public void contextDestroyed(ServletContextEvent e) { 
		
	}

	public void contextInitialized(ServletContextEvent e) {
		try {
			NettyServerBootstrap bootstrap=new NettyServerBootstrap(5002);
			
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}         
		
	}
}