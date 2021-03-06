package com.jsjrobotics.prioritydownloader;

import android.net.ConnectivityManager;
import android.util.Log;

import com.jsjrobotics.prioritydownloader.downloader.DownloadRequest;
import com.jsjrobotics.prioritydownloader.downloader.DownloadThread;
import com.jsjrobotics.prioritydownloader.downloader.Downloader;
import com.jsjrobotics.prioritydownloader.downloader.InputStreamReceiver;

import java.io.File;
import java.io.InputStream;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * A downloader that prioritizes url requests and uses
 * the android http response cache if available
 */
public class PriorityDownloader<T> {
    private static final String TAG = "PriorityDownloader";
    private final ExecutorService executor;
    private PriorityBlockingQueue<DownloadRequest> queuedRequests = new PriorityBlockingQueue<>(10,new DownloadRequestComparator());

    private final ConnectivityManager connMgr;
    private Thread pollingThread = new Thread(){
        @Override
        public void run(){
            while (!interrupted()){
                try {
                    DownloadRequest request = queuedRequests.take();
                    executor.execute(new DownloadThread(request, connMgr));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Log.e(TAG,"Interrupted while trying to take a request");
                }
            }
        }
    };
    private final File externalCacheDir;
    private String cacheFolderName = "httpCache";

    public PriorityDownloader(ExecutorService executor, ConnectivityManager connMgr, File externalCacheDir){
        this.executor = executor;
        this.connMgr = connMgr;
        this.externalCacheDir = externalCacheDir;
        init();
    }

    public PriorityDownloader(ConnectivityManager connMgr, File externalCacheDir){
        this.connMgr = connMgr;
        this.externalCacheDir = externalCacheDir;
        this.executor = DefaultExecutorService.newFixedThreadPool(4);
        init();
    }

    public PriorityDownloader(int maxRunningThreads, ConnectivityManager connMgr, File externalCacheDir){
        this.connMgr = connMgr;
        this.externalCacheDir = externalCacheDir;
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
            DownloadThread t = new DownloadThread(TAG+":Urgent:"+request.getRequestName(),request, connMgr);
            t.start();
        }
        else{
            queuedRequests.add(request);
        }
    }

    /**
     * Return a future that will execute the download request when it is needed.
     * There is no gurantee on when the future will be executed or how it affects priority of queued
     * downloads
     * @param request
     * @return
     */
    public Future<T> queueRequestFuture(final DownloadRequest<T> request){
        Future<T> future = executor.submit(new Callable<T>() {
            @Override
            public T call() throws Exception {
                Log.e(TAG, "Executing: " + request.getRequestName());
                Downloader<T> downloader = new Downloader(connMgr);
                if(request.downloadAsInputStream()){
                    InputStream inputStream = downloader.downloadAsInputStream(request.getUrl());
                    InputStreamReceiver receiver = request.getInputStreamReceiver();
                    if(receiver != null){
                        receiver.receiveInputStream(inputStream);
                        Downloader.closeInputStream(inputStream);
                    }
                    return null;
                }
                else{
                    T result = downloader.downloadAndConvertInputStream(request.getUrl(), request.getConverter());
                    return result;
                }
            }
        });
        return future;
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
        if(externalCacheDir == null){
            return null;
        }
        File cacheDir = new File(externalCacheDir, cacheFolderName);
        if(!cacheDir.exists()){
            cacheDir.mkdir();
        }
        return cacheDir;
    }

    public boolean deleteCacheDir(){
        File dir = getCacheDir();
        if(dir == null){
            return false;
        }
        return dir.delete();
    }
}
