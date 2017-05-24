package com.heyunqi.easydeliver.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

/**
 * Created by Yunqi He on 2017/5/24.
 */

public class ImageViewDialog extends JDialog {

    private JLabel label;

    public ImageViewDialog() throws HeadlessException {
        setTitle("信号源");
        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        label = new JLabel();
        add(label);
        label.setIcon(new ImageIcon("test.jpg"));
        //setUndecorated(true);
    }

//    public void updateLabel(byte[] imageBytes) {
//        ImageIcon imageIcon = new ImageIcon();
//        label.setIcon(new ImageIcon(imageBytes));
//    }

    public void updateLabel(Image image) {
        label.setIcon(new ImageIcon(image));
    }

    /**
     * 覆盖父类的方法。实现自己的添加了ESCAPE键监听
     */
    protected JRootPane createRootPane() {
        KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        JRootPane rootPane = new JRootPane();
        rootPane.registerKeyboardAction(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                escapeKeyProc();
            }
        }, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);

        return rootPane;
    }

    /**
     * 处理ESCAPE按键。子类可以重新覆盖该方法，实现自己的处理方式。
     */
    protected void escapeKeyProc() {
        setVisible(false);
        dispose();
    }
}