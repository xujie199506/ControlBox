package com.controlbox.controller;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.controlbox.nio.NettyChannelMap;

import io.netty.channel.socket.SocketChannel;


@WebServlet("/test")
public class test extends HttpServlet {
	private static final long serialVersionUID = 1L;
  
    public test() {
        super();
        // TODO Auto-generated constructor stub
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String method = request.getParameter("username");
		
		Map<String, SocketChannel> getmap = NettyChannelMap.getmap();
		Set<String> keySet = getmap.keySet();
		String date="";
		for(String str:keySet){
			if(!"".equals(date)){
				date+=",";
			}
			date+=str;
		}
		
		response.getWriter().append(date);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
