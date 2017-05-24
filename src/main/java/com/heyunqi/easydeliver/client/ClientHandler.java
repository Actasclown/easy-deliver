package com.heyunqi.easydeliver.client;

import com.heyunqi.easydeliver.util.ImageUtil;
import com.heyunqi.easydeliver.ui.ImageViewDialog;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.awt.*;
import java.awt.image.BufferedImage;


/**
 * Created by Yunqi He on 2017/5/24.
 */

public class ClientHandler extends SimpleChannelInboundHandler<byte[]> {

    private ImageViewDialog imageViewDialog;

    public ClientHandler(ImageViewDialog imageViewDialog) {
        this.imageViewDialog = imageViewDialog;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, byte[] bytes) throws Exception {
        //bytes里面就是图片的数据。
        BufferedImage image = ImageUtil.bytesToImage(bytes);
        Image tmp = image.getScaledInstance(Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height, Image.SCALE_DEFAULT);
        imageViewDialog.updateLabel(ImageUtil.toBufferedImage(tmp));
    }
}