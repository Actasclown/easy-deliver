package com.heyunqi.easydeliver.client;

import com.heyunqi.easydeliver.ui.HelloPage;
import com.heyunqi.easydeliver.ui.ImageViewDialog;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.bytes.ByteArrayDecoder;

/**
 * Created by Yunqi He on 2017/5/24.
 */

public class EasyDeliverClient {
    public static int MAX_FRAME_LENGTH = 4 * 1024 * 1024;

    private EventLoopGroup workerGroup;

    private ChannelFuture future;

    private Bootstrap bootstrap;

    private ImageViewDialog imageViewDialog;

    private String ip;
    private int port;

    public EasyDeliverClient(String ip, int port, ImageViewDialog imageViewDialog) {
        this.ip = ip;
        this.port = port;
        this.imageViewDialog = imageViewDialog;

        workerGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(workerGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_SNDBUF, EasyDeliverClient.MAX_FRAME_LENGTH)
                .option(ChannelOption.SO_RCVBUF, EasyDeliverClient.MAX_FRAME_LENGTH)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ByteBuf byteBuf = Unpooled.copiedBuffer(HelloPage.FRAME_DELIMITER.getBytes());
                        socketChannel.pipeline().addLast(new DelimiterBasedFrameDecoder(EasyDeliverClient.MAX_FRAME_LENGTH, byteBuf));
                        socketChannel.pipeline().addLast(new ByteArrayDecoder());
                        socketChannel.pipeline().addLast(new ClientHandler(imageViewDialog));
                    }
                });

        this.bootstrap = bootstrap;
    }

    public void connect() {
        try {
            future = bootstrap.connect(ip, port).sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void close() {
        try {
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        workerGroup.shutdownGracefully();
    }
}