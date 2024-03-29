package com.chad.baserecyclerviewadapterhelper.thread;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * TaskExecutor
 * 提高线程利用率
 *
 * @author Kris.Zhang
 * @datetime 2017-03-03 11:20:17
 */
public class XCTaskExecutor {
    private static ExecutorService sThreadPoolExecutor = null;
    private static ScheduledThreadPoolExecutor sScheduledThreadPoolExecutor = null;
    private static Handler sMainHandler = null;

    public static void executeTask(Runnable task) {
        ensureThreadPoolExecutor();
        sThreadPoolExecutor.execute(task);
    }

    public static <T> Future<T> submitTask(Callable<T> task) {
        ensureThreadPoolExecutor();
        return sThreadPoolExecutor.submit(task);
    }

    public static void scheduleTask(long delay, Runnable task) {
        ensureScheduledThreadPoolExecutor();
        sScheduledThreadPoolExecutor.schedule(task, delay, TimeUnit.MILLISECONDS);
    }

    public static void scheduleAtFixedRate(long initialDelay, long period, Runnable task) {
        ensureScheduledThreadPoolExecutor();
        sScheduledThreadPoolExecutor.scheduleAtFixedRate(task, initialDelay, period, TimeUnit.MILLISECONDS);
    }

    public static void scheduleWithFixedDelay(long initialDelay, long period, Runnable task) {
        ensureScheduledThreadPoolExecutor();
        sScheduledThreadPoolExecutor.scheduleWithFixedDelay(task, initialDelay, period, TimeUnit.MILLISECONDS);
    }

    public static void scheduleTaskOnUiThread(long delay, Runnable task) {
        ensureMainHandler();
        sMainHandler.postDelayed(task, delay);
    }

    public static void runTaskOnUiThread(Runnable task) {
        ensureMainHandler();
        sMainHandler.post(task);
    }

    public static void executeTaskOnUiThreadAndWait(final Runnable task) {
        final BlockingQueue<Object> queue = new LinkedBlockingQueue<Object>();
        ensureMainHandler();
        sMainHandler.post(new Runnable() {
            public void run() {
                try {
                    task.run();
                } finally {
                    queue.offer(new Object());
                }
            }
        });
        try {
            queue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void ensureMainHandler() {
        if (sMainHandler == null)
            sMainHandler = new Handler(Looper.getMainLooper());
    }

    private static void ensureThreadPoolExecutor() {
        if (sThreadPoolExecutor == null) {
            // sThreadPoolExecutor = new ThreadPoolExecutor(5, 10, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(),
            // Executors.defaultThreadFactory());
            sThreadPoolExecutor = Executors.newCachedThreadPool();
        }
    }

    private static void ensureScheduledThreadPoolExecutor() {
        if (sScheduledThreadPoolExecutor == null) {
            sScheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(2);
        }
    }

    public static void shutdown() {
        if (sThreadPoolExecutor != null) {
            sThreadPoolExecutor.shutdown();
            sThreadPoolExecutor = null;
        }

        if (sScheduledThreadPoolExecutor != null) {
            sScheduledThreadPoolExecutor.shutdown();
            sScheduledThreadPoolExecutor = null;
        }
    }

}
