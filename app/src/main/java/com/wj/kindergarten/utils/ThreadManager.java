package com.wj.kindergarten.utils;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by tangt on 2016/3/1.
 */
public class ThreadManager {

    BlockingQueue<Runnable> queue = new LinkedBlockingDeque<>();
    private  ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2,3,1000*60, TimeUnit.MILLISECONDS,queue);

    private ThreadManager() {
    }
    public static ThreadManager instance = new ThreadManager();
    public void excuteRunnable(Runnable runnable){
        threadPoolExecutor.execute(runnable);
    }
}
