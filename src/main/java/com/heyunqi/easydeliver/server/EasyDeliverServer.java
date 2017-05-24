package com.heyunqi.easydeliver.server;

import com.heyunqi.easydeliver.client.EasyDeliverClient;
import com.heyunqi.easydeliver.ui.HelloPage;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.bytes.ByteArrayDecoder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yunqi He on 2017/5/24.
 */
public class EasyDeliverServer {

    private int port;

    private ChannelFuture future;

    private List<ChannelHandlerContext> contexts;

    private ServerBootstrap bootstrap;

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    public EasyDeliverServer() {
        this(8888);
    }

    public EasyDeliverServer(int port) {

        this.port = port;
        contexts = new ArrayList<>();

        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup);

        bootstrap.channel(NioServerSocketChannel.class);
        bootstrap.option(ChannelOption.SO_BACKLOG, 1024);
        bootstrap.option(ChannelOption.SO_SNDBUF, EasyDeliverClient.MAX_FRAME_LENGTH);
        bootstrap.option(ChannelOption.SO_RCVBUF, EasyDeliverClient.MAX_FRAME_LENGTH);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);

        EasyDeliverServer server = this;

        bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                ByteBuf byteBuf = Unpooled.copiedBuffer(HelloPage.FRAME_DELIMITER.getBytes());
                socketChannel.pipeline().addLast(new DelimiterBasedFrameDecoder(EasyDeliverClient.MAX_FRAME_LENGTH, byteBuf));
                socketChannel.pipeline().addLast(new ByteArrayDecoder());
                socketChannel.pipeline().addLast(new ServerHandler(server));
            }
        });

        this.bootstrap = bootstrap;

    }

    public void listen() {
        try {
            future = bootstrap.bind(port).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }

    public void addContext(ChannelHandlerContext ctx) {
        contexts.add(ctx);
    }

    public void removeContext(ChannelHandlerContext ctx) {
        contexts.remove(ctx);

    }

    public void sendToAll(byte[] bytes) {
        contexts.forEach(ctx -> {
            ctx.writeAndFlush(Unpooled.wrappedBuffer(bytes, HelloPage.FRAME_DELIMITER.getBytes()));
        });
    }

}