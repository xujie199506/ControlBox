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
import com.controlbox.nio.TextTools;
import com.controlbox.protocol.ProtocelKit;

import io.netty.channel.socket.SocketChannel;
import javafx.scene.text.Text;


@WebServlet("/getstate")
public class getstate extends HttpServlet {
	private static final long serialVersionUID = 1L;
  
    public getstate() {
        super();
        // TODO Auto-generated constructor stub
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html;charset=utf-8");
		response. setCharacterEncoding("UTF-8");
		String dz = request.getParameter("dz").trim();
		String zthq = request.getParameter("zthq").trim();
		System.out.println(zthq);
		byte zthqq=(byte)0x00;
		if("信号强度".equals(zthq)){
			zthqq=(byte)0x00;
		}else{
			zthqq=(byte)0x01;
		}
		ProtocelKit protocelKit2 = new ProtocelKit();
		
		byte[] body=new byte[]{Content.key[0],Content.key[1],Content.key[2],Content.key[3],zthqq};
		
		byte[] cmd = protocelKit2.getCmd((byte)0x07, body);
		
		System.out.println(TextTools.byteToHexString(cmd));
		if(cmd==null||cmd.length<=0||dz==null||"".equals(dz)){
			response.getWriter().append("获取失败1");
			return ;
		}
	
		SocketChannel channel= (SocketChannel)NettyChannelMap.get(dz);
	
		if(cmd!=null){
			System.out.println("获取状态 发送："+TextTools.byteToHexString(cmd));
			channel.writeAndFlush(cmd);
		}else{
			response.getWriter().append("获取失败");return ;
		}
		
		response.getWriter().append("正在获取");
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
