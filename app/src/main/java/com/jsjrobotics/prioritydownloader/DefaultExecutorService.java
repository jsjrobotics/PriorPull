package com.jsjrobotics.prioritydownloader;

import com.jsjrobotics.prioritydownloader.downloader.DownloadThreadFactory;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DefaultExecutorService {
    /**
     * Extends default cached thread pool to spawn at max 4 threads and cause new runnables to wait
     * for a thread to be freed
     * @return
     */
    public static ExecutorService newCachedThreadPool() {
        return new ThreadPoolExecutor(0, 4,
                60L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(), new DownloadThreadFactory());
    }
}
