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
    public static ExecutorService newCachedThreadPool(int nThreads) {
        return new ThreadPoolExecutor(nThreads, nThreads,
                30L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(), new DownloadThreadFactory());
    }

    /**
     * Extends default fixed thread pool to terminate threads after 30 seconds of idle
     * @param nThreads
     * @return
     */
    public static ExecutorService newFixedThreadPool(int nThreads) {
        return new ThreadPoolExecutor(nThreads, nThreads,
                30L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>());
    }
}
