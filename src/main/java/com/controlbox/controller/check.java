package com.controlbox.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
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


@WebServlet("/check")
public class check extends HttpServlet {
	private static final long serialVersionUID = 1L;
  
    public check() {
        super();
        // TODO Auto-generated constructor stub
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html;charset=utf-8");
		response. setCharacterEncoding("UTF-8"); 
	 
		Set<String> set = utlis.set; 
		List<String> setlistuuid=new ArrayList<String>();
		for(Object str:set){ 
			String substring = String.valueOf(str).substring(0, 16); 
			setlistuuid.add(substring);
		}
		
		Set<String> setout = utlis.setout;
		
		List<String> setoutlistuuid=new ArrayList<String>();
		for(String str:setout){ 
			String substring = String.valueOf(str).substring(0, 16); 
			setoutlistuuid.add(substring);
		}
		List<String> setoutlistuuidcache=new ArrayList<String>();
		 
		setoutlistuuidcache.addAll(setoutlistuuid);
		
		setoutlistuuid.removeAll(setlistuuid);
		System.out.println("----------------------");
		System.out.println("----------------------");
		System.out.println("----------------------");
		System.out.println("v1出场比进场多的");
		for(String str:setoutlistuuid){
			System.out.println(str);
		}
		System.out.println("----------------------");
		setlistuuid.removeAll(setoutlistuuidcache);
		System.out.println("v1进场比出场多的");
		for(String str:setlistuuid){
			System.out.println(str);
		}
		duibi();
		response.getWriter().append("正在获取");
	}

	
	private void duibi(){
		Set<String> set = utlis.setv2; 
		List<String> setlistuuid=new ArrayList<String>();
		for(Object str:set){ 
			String substring = String.valueOf(str).substring(0, 16); 
			setlistuuid.add(substring);
		}
		
		Set<String> setout = utlis.setoutv2;
		
		List<String> setoutlistuuid=new ArrayList<String>();
		for(String str:setout){ 
			String substring = String.valueOf(str).substring(0, 16); 
			setoutlistuuid.add(substring);
		}
		List<String> setoutlistuuidcache=new ArrayList<String>();
		 
		setoutlistuuidcache.addAll(setoutlistuuid);
		
		setoutlistuuid.removeAll(setlistuuid);
		System.out.println("----------------------");
		System.out.println("----------------------");
		System.out.println("----------------------");
		System.out.println("v2出场比进场多的");
		for(String str:setoutlistuuid){
			System.out.println(str);
		}
		System.out.println("----------------------");
		setlistuuid.removeAll(setoutlistuuidcache);
		System.out.println("v2进场比出场多的");
		for(String str:setlistuuid){
			System.out.println(str);
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
