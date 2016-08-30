package com.test.netty.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;

public class NettyClient 
{
    public static void main(String[] args){
        ClientBootstrap bootstrap=new ClientBootstrap(
        		new NioClientSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
        	
			public ChannelPipeline getPipeline() throws Exception {
				// TODO Auto-generated method stub
				return Channels.pipeline(new StringDecoder(),new StringEncoder(), new ClientHandler());
			}
		});
        ChannelFuture future=bootstrap.connect(new InetSocketAddress("localhost", 8000));
        future.getChannel().getCloseFuture().awaitUninterruptibly();
        bootstrap.releaseExternalResources();
    }
    
    private static class ClientHandler extends SimpleChannelHandler{
    	private BufferedReader sin=new BufferedReader(new InputStreamReader(System.in));

		@Override
		public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
			System.out.println("已经与Server端建立连接。。。。");
			System.out.println("\n请输入要发送的信息：");
			super.channelConnected(ctx, e);
		}

		@Override
		public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
			// TODO Auto-generated method stub
			if(e.getMessage() instanceof String){
				String message=e.getMessage().toString();
				System.out.println(message);
				e.getChannel().write(sin.readLine());
				System.out.println("\n等待服务器端返回消息。。。");
			}
			super.messageReceived(ctx, e);
		}
    }
}
