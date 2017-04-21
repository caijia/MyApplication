package com.example.administrator.myapplication.schedule;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by cai.jia on 2017/4/21 0021
 */

public class MainSchduler {

    private static final int A_PERIOD = 4;
    private static final int B_PERIOD = 2;

    public static void main(String[] args) {
        final TaskExecutor taskExecutor = new TaskExecutor();

        ScheduledExecutorService scheduledExecutor = Executors.newScheduledThreadPool(5);

        //A启动
        System.out.println(LogTime.getInstance().getCurrentTime() + "   A启动" + A_PERIOD + "秒后开始");
        scheduledExecutor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                taskExecutor.executeTaskA();
            }
        }, A_PERIOD, A_PERIOD, TimeUnit.SECONDS);


        //B启动
        System.out.println(LogTime.getInstance().getCurrentTime() + "   B 启动" + B_PERIOD + "秒后开始");
        scheduledExecutor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                taskExecutor.executeTaskB();
            }
        }, B_PERIOD, B_PERIOD, TimeUnit.SECONDS);
    }


    static class LogTime {

        static volatile LogTime instance = null;
        private SimpleDateFormat simpleDateFormat;

        private LogTime() {
            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINESE);
        }

        public static LogTime getInstance() {
            if (instance == null) {
                synchronized (LogTime.class) {
                    if (instance == null) {
                        instance = new LogTime();
                    }
                }
            }
            return instance;
        }

        public String getCurrentTime() {
            return simpleDateFormat.format(new Date());
        }
    }

    static class TaskExecutor {

        Lock lock;
        Condition condition;
        boolean aFinish;

        public TaskExecutor() {
            lock = new ReentrantLock();
            condition = lock.newCondition();
        }

        public void executeTaskA() {
            if (lock.tryLock()) {
                try {
                    System.out.println(LogTime.getInstance().getCurrentTime() + "    taskA");
                    aFinish = true;
                    condition.signal();

                } catch (Exception e) {

                }finally {
                    lock.unlock();
                }
            }

        }

        public void executeTaskB() {
            if (aFinish) {
                //A完成后直接执行B,不必要等待,避免上锁影响效率。
                System.out.println(LogTime.getInstance().getCurrentTime() + "    taskB");
                return;
            }

            //A没有完成,等待。
            if (lock.tryLock()) {
                try {
                    while (!aFinish) {
                        System.out.println(LogTime.getInstance().getCurrentTime() + "    taskB wait");
                        condition.await();
                    }
                    //执行B
                    System.out.println(LogTime.getInstance().getCurrentTime() + "    taskB");
                } catch (Exception e) {
                } finally {
                    lock.unlock();
                }
            }
        }
    }

}
