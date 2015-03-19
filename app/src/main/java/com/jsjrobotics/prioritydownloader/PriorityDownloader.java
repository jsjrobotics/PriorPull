package com.jsjrobotics.prioritydownloader;

import android.util.Log;

import com.jsjrobotics.prioritydownloader.downloader.DownloadRequest;
import com.jsjrobotics.prioritydownloader.downloader.DownloadThread;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;

public class PriorityDownloader {
    private static final String TAG = "PriorityDownloader";
    private final ExecutorService executor;
    private PriorityBlockingQueue<DownloadRequest> queuedRequests = new PriorityBlockingQueue<>(10,new DownloadRequestComparator());
    private Thread pollingThread = new Thread(){
        @Override
        public void run(){
            while (!interrupted()){
                try {
                    DownloadRequest request = queuedRequests.take();
                    executor.execute(new DownloadThread(request.getRequestName(), request));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Log.e(TAG,"Interrupted while trying to take a request");
                }
            }
        }
    };

    public PriorityDownloader(ExecutorService executor){
        this.executor = executor;
        init();
    }

    public PriorityDownloader(){
        this.executor = DefaultExecutorService.newFixedThreadPool(4);
        init();
    }

    public PriorityDownloader(int maxRunningThreads){
        this.executor = DefaultExecutorService.newCachedThreadPool(maxRunningThreads);
        init();
    }

    private void init(){
        enableHttpResponseCache();
        pollingThread.start();
    }

    /**
     * Queue a download request onto an available background thread
     * @param request
     */
    public void queueRequest(final DownloadRequest request){
        if(request.getPriority() == Priorities.URGENT){
            DownloadThread t = new DownloadThread(TAG+":Urgent:"+request.getRequestName(),request);
            t.start();
        }
        else{
            queuedRequests.add(request);
        }
    }



    /* http://android-developers.blogspot.com/2011/09/androids-http-clients.html */
    private void enableHttpResponseCache() {
        File cacheDir = getCacheDir();
        if(cacheDir == null){
            Log.e(TAG,"Failed to get cache directory");
            return;
        }
        try {
            long httpCacheSize = 10 * 1024 * 1024; // 10 MiB
            File httpCacheDir = new File(cacheDir, "http");
            Class.forName("android.net.http.HttpResponseCache")
                    .getMethod("install", File.class, long.class)
                    .invoke(null, httpCacheDir, httpCacheSize);
        } catch (Exception httpResponseCacheNotAvailable) {
            Log.e(TAG, "http response cache not available");
        }
    }

    private File getCacheDir() {
        File externalDir = PriorityDownloaderApp.getContext().getExternalCacheDir();
        if(externalDir == null){
            return null;
        }
        File cacheDir = new File(externalDir, "httpCache");
        if(!cacheDir.exists()){
            cacheDir.mkdir();
        }
        return cacheDir;
    }
}
