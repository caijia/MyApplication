package com.example.administrator.myapplication.http;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by cai.jia on 2017/3/16 0016
 */

public class Schedule {

    private ScheduledExecutorService service;
    private ScheduledFuture<?> scheduledFuture;
    private long period = 1000;

    public Schedule() {
        service = Executors.newScheduledThreadPool(3);
    }

    public static void main(String[] args) {
        new Schedule().start();
    }

    public void start() {
        scheduledFuture = service.scheduleAtFixedRate(task, 0, period, TimeUnit.MILLISECONDS);
    }

    private Runnable task = new Runnable() {
        @Override
        public void run() {
            System.out.println("ddd");
        }
    };
}
