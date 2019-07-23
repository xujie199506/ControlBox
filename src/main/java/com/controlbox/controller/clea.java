package com.controlbox.controller;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.controlbox.protocol.utlis;

import io.netty.channel.socket.SocketChannel;
import javafx.scene.text.Text;


@WebServlet("/clear")
public class clea extends HttpServlet {
	private static final long serialVersionUID = 1L;
  
    public clea() {
        super();
        // TODO Auto-generated constructor stub
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html;charset=utf-8");
		response. setCharacterEncoding("UTF-8");
		String dz = request.getParameter("dz").trim();
		utlis.num=0;
		utlis.set=new HashSet<String>();
		System.out.println("清零");
		utlis.setout=new HashSet<String>();
		utlis.setv2=new HashSet<String>();
		System.out.println("清零");
		utlis.setoutv2=new HashSet<String>();
		System.out.println("卡数量:   "+utlis.num);
		response.getWriter().append("正在获取");

		utlis.mapinv2.clear();
		utlis.mapoutv2.clear();
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
