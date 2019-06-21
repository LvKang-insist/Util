package com.admin.util;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Lv
 * Created at 2019/6/16
 */
public class Test {

    private static final ThreadFactory FACTORY = new ThreadFactory(){
        private final AtomicInteger mCount = new AtomicInteger();
        @Override
        public Thread newThread(Runnable r){
            return new Thread(r,"text ----＃"+ mCount.getAndIncrement());
        }
    };
    public static void main(String [] args){
        ThreadPoolExecutor textPool = new ThreadPoolExecutor(3,5,0L 
,TimeUnit.MILLISECONDS, new LinkedBlockingDeque<Runnable>(),FACTORY);
        for(int i = 0; i <10; i ++){
            textPool.execute(new Runnable(){
                @Override
                public void run(){
                    System.out.println(Thread.currentThread().getName());
                }
            } );
        }
    }
}
