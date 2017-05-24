package com.heyunqi.easydeliver.ui;

import com.heyunqi.easydeliver.client.EasyDeliverClient;
import com.heyunqi.easydeliver.server.EasyDeliverServer;
import com.heyunqi.easydeliver.util.ImageUtil;
import com.heyunqi.easydeliver.util.ScreenCapture;
import com.heyunqi.easydeliver.util.ThreadUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

/**
 * Created by Yunqi He on 2017/5/22.
 */
public class HelloPage {

    public static String FRAME_DELIMITER = "MOUNTAIN_EVEREST_IS_THE_HIGHEST_MOUNTAIN";

    public static JDialog screenShow = null;
    public static BufferedImage receivedImage = null;
    public static BufferedImage sendedImage = null;

    public static void main(String[] args) {

        final ScreenCapture screenCapture = new ScreenCapture(400);
        final Thread screenCaptureThread = new Thread(screenCapture);

        final JFrame helloPage = new JFrame("EasyDeliver");
        Container helloPageContainer = helloPage.getContentPane();

        helloPage.setLayout(null);

        final JButton actAsServer = new JButton("传出");
        actAsServer.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if(actAsServer.getText() == "传出") {
                    if(!screenCapture.started) {
                        screenCaptureThread.start();
                        screenCapture.started = true;
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                startServer();
                            }
                        }).start();
                    }
                    else {
                        screenCapture.setSuspend(false);
                    }
                    actAsServer.setText("停止");
                } else {
                    screenCapture.setSuspend(true);
                    actAsServer.setText("传出");
                }
            }
        });
        actAsServer.setBounds(75, 100, 80, 40);
        helloPageContainer.add(actAsServer);

        final JButton actAsClient = new JButton("接收");
        actAsClient.addActionListener(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                startClient();
            }
        });
        actAsClient.setBounds(225, 100, 80, 40);
        helloPageContainer.add(actAsClient);

        helloPage.setSize(400,300);
        helloPage.setVisible(true);
        helloPage.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public static void startServer() {
        EasyDeliverServer server = new EasyDeliverServer();
        Thread thread = new Thread(server::listen);
        thread.setDaemon(true);
        thread.start();

        while(true) {
            byte[] bytes = ImageUtil.imageToBytes(sendedImage);
            server.sendToAll(bytes);
            ThreadUtil.sleep(16);
        }
    }

    public static void startClient() {
        ImageViewDialog jDialog = new ImageViewDialog();
        jDialog.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

        jDialog.setVisible(true);

        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        gd.setFullScreenWindow(jDialog);

        EasyDeliverClient client = new EasyDeliverClient("127.0.0.1", 8888, jDialog);
        client.connect();
    }
}
