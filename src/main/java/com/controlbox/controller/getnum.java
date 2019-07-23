package com.controlbox.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.controlbox.protocol.utlis;


@WebServlet("/getnum")
public class getnum extends HttpServlet {
	private static final long serialVersionUID = 1L;
  
    public getnum() {
        super();
        // TODO Auto-generated constructor stub
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html;charset=utf-8");
		response. setCharacterEncoding("UTF-8"); 
	 
		int insize = utlis.set.size();
		 
		int ourtsize = utlis.setout.size();
		
		int setv2 = utlis.setv2.size();
		 
		int setoutv2 = utlis.setoutv2.size();

		
		String format = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss.SSS").format(new Date());
		
		
		response.getWriter().append(format+"    进场数量："+insize+",出场数量："+ourtsize+"    v2进场数量："+setv2+",v2出场数量："+setoutv2);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
