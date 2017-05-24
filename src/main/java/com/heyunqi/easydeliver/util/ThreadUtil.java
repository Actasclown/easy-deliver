package com.heyunqi.easydeliver.util;

/**
 * Created by Yunqi He on 2017/5/24.
 */
public class ThreadUtil {
    public static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}