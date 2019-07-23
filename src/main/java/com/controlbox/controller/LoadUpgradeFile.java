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

@WebServlet("/LoadUpgradeFile")
public class LoadUpgradeFile extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(LoadUpgradeFile.class);

	public LoadUpgradeFile() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		String FeilName = request.getParameter("FeilName").trim();
		String version = request.getParameter("version").trim();
		System.out.println(version);
		if (version == null || "".equals(version) || FeilName == null || "".equals(FeilName)) {
			response.getWriter().append("加载升级版本失败1");
			return;
		}

		byte[] bversion = TextTools.hexStringToByteArray(version);

		ProtocelKit protocelKit = new ProtocelKit();
		UpgradePackage upgradePackage = protocelKit.getUpgradePackage(Content.key, bversion, Content.path+FeilName+".bin");
		if (upgradePackage != null) {
			Content.upgradePackage = upgradePackage;
				response.getWriter().append("加载升级文件成功,包数量"+upgradePackage.getUpgradePackageList().size());

		} else {
			response.getWriter().append("加载升级文件失败");
			return;
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
