package com.controlbox.controller;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.controlbox.nio.NettyChannelMap;
import com.controlbox.nio.NettyServerHandler;
import com.controlbox.nio.TextTools;
import com.controlbox.protocol.ProtocelKit;
import com.controlbox.protocol.bean.UpgradePackage;

import io.netty.channel.socket.SocketChannel;


@WebServlet("/updata")
public class updata extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(updata.class);
    public updata() {
        super();
        // TODO Auto-generated constructor stub
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html;charset=utf-8");
		response. setCharacterEncoding("UTF-8");
		String dz = request.getParameter("dz").trim();
		String version = request.getParameter("version").trim();
		System.out.println(version);
		if(version==null||"".equals(version)||dz==null||"".equals(dz)){
			response.getWriter().append("启动升级失败1");
			return ;
		}
		
		byte[] bversion=TextTools.hexStringToByteArray(version);
		
		ProtocelKit protocelKit = new ProtocelKit();
		SocketChannel channel= (SocketChannel)NettyChannelMap.get(dz);
		UpgradePackage upgradePackage = Content.upgradePackage;
		if(upgradePackage!=null){
			byte[] bootInfo = upgradePackage.getBootInfo();
			logger.warn("升级发送："+TextTools.byteToHexString(bootInfo));
			channel.writeAndFlush(bootInfo);
		}else{
			response.getWriter().append("启动升级失败");return ;
		}
		
		response.getWriter().append("启动升级");
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
