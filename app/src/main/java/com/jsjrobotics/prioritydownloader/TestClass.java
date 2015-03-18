package com.jsjrobotics.prioritydownloader;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.jsjrobotics.prioritydownloader.downloader.DownloadRequest;
import com.jsjrobotics.prioritydownloader.downloader.Downloader;
import com.jsjrobotics.prioritydownloader.downloader.InputStreamReceiver;

import java.io.InputStream;

public class TestClass extends Activity {
    private static final String TAG = "TestClass";
    private PriorityDownloader downloader;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        testPriorityDownloader();
    }

    public void testPriorityDownloader(){
        downloader = new PriorityDownloader();
        DownloadRequest reqeust = new DownloadRequest(new InputStreamReceiver() {
            @Override
            public void receiveInputStream(InputStream stream) {
                Log.e("ApplicationTest", "Received inputstream");
            }
        },"https://developer.android.com/training/basics/network-ops/connecting.html",Priorities.HIGH,"test");
        downloader.queueRequest(reqeust);
        Log.e(TAG,"---------Request queued--------------");
    }

    public void testDownloadSingleUrl(){
        Thread t = new Thread(){
            @Override
            public void run(){
                Downloader d = new Downloader();
                String result = d.downloadAsString("https://www.google.com/search?client=ubuntu&channel=fs&q=android+manifest&ie=utf-8&oe=utf-8");
                Log.e(TAG,result);
            }
        };
        t.start();
    }
}
