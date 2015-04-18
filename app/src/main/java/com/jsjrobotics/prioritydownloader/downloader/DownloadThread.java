package com.jsjrobotics.prioritydownloader.downloader;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.InputStream;

/**
 * A Thread to service a @{link DownloadRequest}
 */
public class DownloadThread extends Thread {
    private static final String TAG = "DownloadThread";
    private DownloadRequest request;
    private final ConnectivityManager connMgr;

    public DownloadThread(String name, ConnectivityManager connMgr){
        super(name);
        this.connMgr = connMgr;
        request = null;
    }

    public DownloadThread(DownloadRequest request, ConnectivityManager connMgr){
        super(request.getRequestName());
        this.request = request;
        this.connMgr = connMgr;
    }

    public DownloadThread(String threadName,DownloadRequest request, ConnectivityManager connMgr){
        super(threadName);
        this.request = request;
        this.connMgr = connMgr;
    }

    public void setRequest(DownloadRequest request){
        if(!isAlive()){
            this.request = request;
            setName(request.getRequestName());
        }
    }

    @Override
    public void run(){
        Log.e(TAG, "Executing: " + request.getRequestName());
        Downloader downloader = new Downloader(connMgr);
        if(request.downloadAsInputStream()){
            InputStream inputStream = downloader.downloadAsInputStream(request.getUrl());
            InputStreamReceiver receiver = request.getInputStreamReceiver();
            if(receiver != null){
                receiver.receiveInputStream(inputStream);
                Downloader.closeInputStream(inputStream);
            }
        }
        else{
            Object result = downloader.downloadAndConvertInputStream(request.getUrl(),request.getConverter());
            Handler h = request.getHandler();
            Message msg = h.obtainMessage(request.getMessageWhat(),result);
            h.sendMessage(msg);
        }
    }
}
