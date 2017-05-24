package com.heyunqi.easydeliver.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by Yunqi He on 2017/5/24.
 */

public class ServerHandler extends SimpleChannelInboundHandler<byte[]> {

    private EasyDeliverServer server;

    public ServerHandler(EasyDeliverServer server) {
        this.server = server;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, byte[] bytes) throws Exception {
        //ignore
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        server.addContext(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        server.removeContext(ctx);
    }
}
