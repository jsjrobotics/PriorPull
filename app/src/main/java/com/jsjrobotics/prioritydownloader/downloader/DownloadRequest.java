package com.jsjrobotics.prioritydownloader.downloader;

import android.os.Handler;

import com.jsjrobotics.prioritydownloader.Priorities;

public class DownloadRequest {
    private final boolean downloadAsInputStream;
    private final String url;
    private final InputStreamReceiver inputStreamReceiver;
    private final Handler handler;
    private final int messageWhat;
    private final Priorities priority;
    private final String requestName;
    private final InputStreamToObject converter;


    public DownloadRequest(InputStreamReceiver inputStreamReceiver, String url, Priorities priority, String requestName){
        this.priority = priority;
        this.requestName = requestName;
        this.downloadAsInputStream = true;
        this.url = url;
        this.inputStreamReceiver = inputStreamReceiver;
        handler = null;
        messageWhat = -1;
        converter = null;
    }

    public DownloadRequest(Handler receivingHandler, int messageWhat, String url, Priorities priority, String requestName){
        this.priority = priority;
        this.requestName = requestName;
        this.downloadAsInputStream = false;
        this.url = url;
        this.inputStreamReceiver = null;
        handler = receivingHandler;
        this.messageWhat = messageWhat;
        converter = InputStreamToObjectConverters.getStringConverter();
    }

    public DownloadRequest(Handler receivingHandler, int messageWhat,InputStreamToObject converter, String url, Priorities priority, String requestName){
        this.priority = priority;
        this.requestName = requestName;
        this.downloadAsInputStream = false;
        this.url = url;
        this.inputStreamReceiver = null;
        handler = receivingHandler;
        this.messageWhat = messageWhat;
        this.converter = converter;
    }

    public boolean downloadAsInputStream() {
        return downloadAsInputStream;
    }

    public String getUrl() {
        return url;
    }

    public InputStreamReceiver getInputStreamReceiver() {
        return inputStreamReceiver;
    }

    public Handler getHandler() {
        return handler;
    }

    public int getMessageWhat() {
        return messageWhat;
    }

    public Priorities getPriority() {
        return priority;
    }

    public String getRequestName() {
        return requestName;
    }

    public InputStreamToObject getConverter() {
        return converter;
    }
}
