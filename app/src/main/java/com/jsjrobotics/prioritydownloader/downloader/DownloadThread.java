package com.jsjrobotics.prioritydownloader.downloader;

import android.os.Handler;
import android.os.Message;

import java.io.InputStream;

public class DownloadThread extends Thread {
    private static final String TAG = "DownloadThread";
    private DownloadRequest request;

    public DownloadThread(String name){
        super(name);
        request = null;
    }

    public DownloadThread(String name,DownloadRequest request){
        super(name);
        this.request = request;
    }

    public void setRequest(DownloadRequest request){
        if(!isAlive()){
            this.request = request;
        }
    }

    @Override
    public void run(){
        Downloader downloader = new Downloader();
        if(request.downloadAsInputStream()){
            InputStream inputStream = downloader.downloadAsInputStream(request.getUrl());
            InputStreamReceiver receiver = request.getInputStreamReceiver();
            if(receiver != null){
                receiver.receiveInputStream(inputStream);
                Downloader.closeInputStream(inputStream);
            }
        }
        else{
            String result = downloader.downloadAsString(request.getUrl());
            Handler h = request.getHandler();
            Message msg = h.obtainMessage(request.getMessageWhat(),result);
            h.sendMessage(msg);
        }
    }
}
