package com.heyunqi.easydeliver.util;

import com.heyunqi.easydeliver.ui.HelloPage;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Yunqi He on 2017/5/22.
 */

public class ScreenCapture implements Runnable {

    //public BufferedImage fullScreenImage;
    //private int bufferPointer;
    private int refreshTime = 1000;

//    public static BufferedImage toBufferedImage(Image image) {
//        if (image instanceof BufferedImage) {
//            return (BufferedImage)image;
//        }
//
//        // This code ensures that all the pixels in the image are loaded
//        image = new ImageIcon(image).getImage();
//
//        // Determine if the image has transparent pixels; for this method's
//        // implementation, see e661 Determining If an Image Has Transparent Pixels
//        //boolean hasAlpha = hasAlpha(image);
//
//        // Create a buffered image with a format that's compatible with the screen
//        BufferedImage bimage = null;
//        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
//        try {
//            // Determine the type of transparency of the new buffered image
//            int transparency = Transparency.OPAQUE;
//           /* if (hasAlpha) {
//             transparency = Transparency.BITMASK;
//             }*/
//
//            // Create the buffered image
//            GraphicsDevice gs = ge.getDefaultScreenDevice();
//            GraphicsConfiguration gc = gs.getDefaultConfiguration();
//            bimage = gc.createCompatibleImage(
//                    image.getWidth(null), image.getHeight(null), transparency);
//        } catch (HeadlessException e) {
//            // The system does not have a screen
//        }
//
//        if (bimage == null) {
//            // Create a buffered image using the default color model
//            int type = BufferedImage.TYPE_INT_RGB;
//            //int type = BufferedImage.TYPE_3BYTE_BGR;//by wang
//            /*if (hasAlpha) {
//             type = BufferedImage.TYPE_INT_ARGB;
//             }*/
//            bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
//        }
//
//        // Copy image to buffered image
//        Graphics g = bimage.createGraphics();
//
//        // Paint the image onto the buffered image
//        g.drawImage(image, 0, 0, null);
//        g.dispose();
//
//        return bimage;
//    }

    private void captureFullScreen() {
        try {
            Robot robot = new Robot();
            Rectangle rectangle = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            HelloPage.sendedImage = robot.createScreenCapture(rectangle);
//            ByteArrayOutputStream out = new ByteArrayOutputStream();
//            try {
//                boolean flag = ImageIO.write(HelloPage.sendedImage, "png", out);
//            } catch (IOException e) {
//                System.out.println("ImageToBytesFailed");
//            }
//            if(out != null)
//                System.out.println("send real");
//            HelloPage.server.sendToAll(out.toByteArray());
//           byte[] b = out.toByteArray();
//            InputStream in = new ByteArrayInputStream(b);
//            try {
//                HelloPage.receivedImage = ImageIO.read(in);
//            } catch (IOException e) {
//                System.out.println("fuck");
//            }
        } catch (AWTException e) {
            System.err.println("Internal Error: " + e);
            e.printStackTrace();
        }
    }

    public ScreenCapture(int refreshTime) {
        //bufferPointer = 1;
        this.refreshTime = refreshTime;
    }

//    public void saveOneFrame() throws IOException {
//
//        captureFullScreen();
//
//        String userdir = System.getProperty("user.dir") + "\\res\\out";
//        File file = new File(userdir, bufferPointer + ".png");
//        if(!file.exists())
//            file.mkdirs();

//        long time = System.currentTimeMillis();
//
//        ImageIO.write(fullScreenImage, "png", file);
//
//
//        System.out.print("\r" + bufferPointer + " 存储, 用时：" + (System.currentTimeMillis() - time));
//
//        bufferPointer = bufferPointer + 1;
//
//        if(bufferPointer > BUFFER_LENGTH) {
//            bufferPointer = 1;
//        }
//    }

    public void setRefreshTime(int refreshTime) {
        this.refreshTime = refreshTime;
    }

    boolean suspended = false;
    public boolean started = false;

    private String control = ""; // 只是需要一个对象而已，这个对象没有实际意义
    public void setSuspend(boolean suspend) {
        if (!suspend) {
            synchronized (control) {
                control.notifyAll();
            }
        }
        this.suspended = suspend;
    }

    public void run() {
//        Server broadcast = new Server();
//        try {
//            broadcast.run(9873);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        while(true) {
            captureFullScreen();
            //System.out.print("\r截屏成功" + System.currentTimeMillis());
            synchronized (control) {
                if (suspended) {
                    try {
                        control.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}