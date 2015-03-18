package com.jsjrobotics.prioritydownloader.downloader;

import java.util.concurrent.ThreadFactory;

public class DownloadThreadFactory implements ThreadFactory {
    private static final String TAG = "DownloadThreadFactory";
    private int threadCount = 0;
    @Override
    public Thread newThread(final Runnable task) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                task.run();
                threadCount -= 1;
            }
        };
        Thread t = new Thread(TAG+":"+threadCount);
        threadCount += 1;
        return t;
    }
}
