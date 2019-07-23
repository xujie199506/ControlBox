package com.controlbox.nio;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.controlbox.protocol.ProtocelKit;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

public class NettyServerBootstrap {
	private int port;
	private SocketChannel socketChannel;
	private static final Logger logger = LoggerFactory.getLogger(NettyServerHandler.class);

	public NettyServerBootstrap(int port) throws InterruptedException {
		this.port = port;
		bind();
	}

	private void bind() throws InterruptedException {
		EventLoopGroup boss = new NioEventLoopGroup();
		EventLoopGroup worker = new NioEventLoopGroup();
		ServerBootstrap bootstrap = new ServerBootstrap();
		bootstrap.group(boss, worker);
		bootstrap.channel(NioServerSocketChannel.class);
		bootstrap.option(ChannelOption.SO_BACKLOG, 1024);
		// 通过NoDelay禁用Nagle,使消息立即发出去，不用等待到一定的数据量才发出去
		bootstrap.option(ChannelOption.TCP_NODELAY, true);
		// 保持长连接状态
		bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
		bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel socketChannel) throws Exception {
				ChannelPipeline p = socketChannel.pipeline();
				p.addLast("bytesDecoder", new ByteArrayDecoder());
				p.addLast("bytesEncoder", new ByteArrayEncoder());
				p.addLast(new NettyServerHandler());
				 p.addLast("framedecoder",new LengthFieldBasedFrameDecoder(1024*1024*1024, 0, 4,0,4));

			}
		});
		ChannelFuture f = bootstrap.bind(port).sync();
		if (f.isSuccess()) {
			logger.info("server start---------------");
			new Thread(new Runnable() {

				public void run() {
					// TODO Auto-generated method stub

				 //	xt(2);

				}

			}).start();
			
		}
		
		
	}

	private void xt(int i) {
		ProtocelKit protocelKit = new ProtocelKit();
		byte[] sendHartbeat = protocelKit.sendHartbeat();
		while (true) {
			
			try {
				Thread.sleep(30000);
				Map<String, SocketChannel> getmap = NettyChannelMap.getmap();
				for (Map.Entry<String, SocketChannel> entry : getmap.entrySet()) {
					//System.out.println("向模块"+entry.getKey()+"发送心跳:"+TextTools.byteToHexString(sendHartbeat));
					SocketChannel channel = (SocketChannel) entry.getValue();
					if (channel != null) {
						channel.writeAndFlush(sendHartbeat);
					}
				}
			} catch (Exception e) {
			}
		}
	}
	public static void main(String[] args) throws InterruptedException {
		NettyServerBootstrap bootstrap = new NettyServerBootstrap(9999);
		while (true) {

			TimeUnit.SECONDS.sleep(5);

			Map<String, SocketChannel> getmap = NettyChannelMap.getmap();
			for (Map.Entry<String, SocketChannel> entry : getmap.entrySet()) {
				System.out.println(entry.getKey());
				SocketChannel channel = (SocketChannel) entry.getValue();
				if (channel != null) {
					channel.writeAndFlush("ssssssssss".getBytes());
				}
			}

		}
	}
}