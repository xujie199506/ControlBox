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

import io.netty.channel.socket.SocketChannel;
import javafx.scene.text.Text;


@WebServlet("/kill")
public class kill extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(kill.class);
    public kill() {
        super();
        // TODO Auto-generated constructor stub
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html;charset=utf-8");
		response. setCharacterEncoding("UTF-8");
		String dz = request.getParameter("dz").trim();
		
	
		SocketChannel channel= (SocketChannel)NettyChannelMap.get(dz);
	
		if(channel!=null){
			try{
			channel.close();
			logger.warn(dz+"断开成功");
			}catch(Exception e){
				e.printStackTrace();
				System.out.println(dz+"断开失败");
			}
		}else{
			System.out.println(dz+"已下线");
		}
		
		response.getWriter().append("正在获取");
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
