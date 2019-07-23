package com.controlbox.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.controlbox.nio.NettyChannelMap;
import com.controlbox.nio.NettyServerHandler;
import com.controlbox.nio.TextTools;
import com.controlbox.protocol.ProtocelKit;

import io.netty.channel.Channel;
import io.netty.channel.socket.SocketChannel;

/**
 * Servlet implementation class Redata
 */
@WebServlet("/Redata")
public class Redata extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Redata() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String dz = request.getParameter("dz").trim();
		String cmd=request.getParameter("type").trim();
		String con = request.getParameter("con").trim();
		response.setContentType("text/html;charset=utf-8");
		response. setCharacterEncoding("UTF-8");
		byte[] paramType=null;
		byte[] paramValue=null;
		if(dz!=null&&!"".equals(dz)&&cmd!=null&&!"".equals(cmd)&&con!=null&&!"".equals(con)){
			paramType=TextTools.hexStringToByteArray(cmd);
			paramValue=TextTools.hexStringToByteArray(con);
		}else{
			response.getWriter().append("发送失败");
			return;
		}
		
		
		if((byte)0x06==paramType[0]){
			
			paramValue=con.getBytes();
		}
		 
		 SocketChannel channel= (SocketChannel)NettyChannelMap.get(dz);
    	 if(channel!=null&&con!=null&&!"".equals(con)){
    		 
    		 	ProtocelKit protocelKit = new ProtocelKit();
    		 	byte[] setDevParam = protocelKit.setDevParam(NettyServerHandler.key, paramType[0], paramValue);
    		 	if(setDevParam!=null){
    		 		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date())+"    发送");
      	    	  
    		 		System.out.println(TextTools.byteToHexString(setDevParam));
    		 		channel.writeAndFlush(setDevParam);
    		 	}else{
    		 		response.getWriter().append("发送失败 ");
    		 		return;
    		 	}
    		 
          
         }else{
		 		response.getWriter().append("发送失败 ");
		 		return;
		 	}
		response.getWriter().append("发送成功 ");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
