package com.jsjrobotics.prioritydownloader;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

import com.jsjrobotics.prioritydownloader.downloader.DownloadRequest;
import com.jsjrobotics.prioritydownloader.downloader.InputStreamReceiver;

import junit.framework.TestCase;

import java.io.InputStream;

public class ApplicationTest extends TestCase{

    private PriorityDownloader download;

    public void setUp(){
        download = new PriorityDownloader();
    }
    public void testDownload(){

        DownloadRequest reqeust = new DownloadRequest(new InputStreamReceiver() {
            @Override
            public void receiveInputStream(InputStream stream) {
                Log.e("ApplicationTest","Received inputstream");
            }
        },"https://developer.android.com/training/basics/network-ops/connecting.html",Priorities.HIGH,"test");
        download.queueRequest(reqeust);
    }
}