package org.example;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.EventExecutorGroup;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    private final int port;

    public Server(int port) {
        this.port = port;
    }

    public void start() throws Exception {
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST, true)
                    .handler(new ChannelInitializer<NioDatagramChannel>() {
                        @Override
                        protected void initChannel(NioDatagramChannel ch) {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new LoggingHandler(LogLevel.INFO));
                            pipeline.addLast(new ServerHandler());
                        }
                    });

            ChannelFuture f = b.bind(port).sync();
            f.channel().closeFuture().await();
        } finally {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        int port = 8080; // Set your desired port here
        new Server(port).start();
    }

    private static class ServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {
        private final Map<String, Channel> connectedClients = new ConcurrentHashMap<>();

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) {

        }

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) {
            ByteBuf content = packet.content();
            String message = content.toString(CharsetUtil.UTF_8);
            System.out.println("Server received: " + message);

            // Add your custom string to the response
            String response = "Server says: " + message;

            // Convert the response to a ByteBuf
            ByteBuf responseBuf = ctx.alloc().buffer();
            responseBuf.writeBytes(response.getBytes(CharsetUtil.UTF_8));

            // Send the modified response back to the client
            ctx.writeAndFlush(new DatagramPacket(responseBuf, packet.sender()));
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {

        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            cause.printStackTrace();
            ctx.close();
        }

    }


}
