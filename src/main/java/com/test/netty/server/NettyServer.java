package com.test.netty.server;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.channel.*;
import org.jboss.netty.handler.codec.string.StringDecoder;  
import org.jboss.netty.handler.codec.string.StringEncoder;


public class NettyServer {
	public static void main(String[] args){
		ServerBootstrap bootstrap=new ServerBootstrap(
				new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
		bootstrap.setPipelineFactory(new ChannelPipelineFactory() {

			public ChannelPipeline getPipeline() throws Exception {
				return Channels.pipeline(new StringDecoder(),new StringEncoder(),new ServerHandler());
			}
		});
		Channel bind=bootstrap.bind(new InetSocketAddress(8000));
		System.out.println("Server已经启动，监听端口："+bind.getLocalAddress()+",正在等待客户端连接。。。");
	}
	
	private static class ServerHandler extends SimpleChannelHandler{

		@Override
		public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
			// TODO Auto-generated method stub
			if(e.getMessage() instanceof String){
				String message=(String)e.getMessage();
				System.out.println("client发来消息： "+message);
				
				e.getChannel().write("Server已经收到刚刚发送来的："+message);
				System.out.println("\n等待客户端输入。。。。");
			}
			super.messageReceived(ctx, e);  
		}

		@Override
		public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
			// TODO Auto-generated method stub
			System.out.println("有一个客户端连接进来了。。。");
			System.out.println("Client:"+e.getChannel().getRemoteAddress());
			System.out.println("Server:"+e.getChannel().getLocalAddress());
			System.out.println("\n等待客户端输入。。。。");
			super.channelConnected(ctx, e);
		}

		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
			// TODO Auto-generated method stub
			super.exceptionCaught(ctx, e);
		}
		
	}
}
