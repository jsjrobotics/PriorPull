package com.jsjrobotics.prioritydownloader.downloader;

import android.os.Handler;

import com.jsjrobotics.prioritydownloader.Priorities;

/**
 * A DownloadRequest that can return downloaded data as a raw stream, or an object on a
 * different thread through a handler
 */
public class DownloadRequest<T> {
    private final boolean downloadAsInputStream;
    private final String url;
    private final InputStreamReceiver inputStreamReceiver;
    private final Handler handler;
    private final int messageWhat;
    private final Priorities priority;
    private final String requestName;
    private final InputStreamToObject<T> converter;


    /**
     * Build a download request that will call the inputStreamReader upon download completion
     * @param inputStreamReceiver Receives input stream on success, null on failure
     * @param url Url to download
     * @param priority Priority of this request
     * @param requestName Caller supplied request name
     */
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


    /**
     * Build a download request that will call send a caller specefied object as the message object to the receiving
     * handler upon download completion
     * @param receivingHandler The handler that will receive the download complete method
     * @param messageWhat The what of the received message
     * @param converter An inputstream to object converter to build the message object
     * @param url url to download
     * @param priority Download request priority
     * @param requestName Caller supplied request name
     */
    public DownloadRequest(Handler receivingHandler, int messageWhat,InputStreamToObject<T> converter, String url, Priorities priority, String requestName){
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

    public InputStreamToObject<T> getConverter() {
        return converter;
    }
}
