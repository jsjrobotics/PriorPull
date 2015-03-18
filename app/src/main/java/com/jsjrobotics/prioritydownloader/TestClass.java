package com.jsjrobotics.prioritydownloader;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.jsjrobotics.prioritydownloader.downloader.DownloadRequest;
import com.jsjrobotics.prioritydownloader.downloader.Downloader;
import com.jsjrobotics.prioritydownloader.downloader.InputStreamReceiver;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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
        List<DownloadRequest> requestList = new ArrayList<>();
        DownloadRequest reqeust = new DownloadRequest(new InputStreamReceiver() {
            @Override
            public void receiveInputStream(InputStream stream) {
                Log.e("ApplicationTest", "Received inputstream");
            }
        },"https://developer.android.com/training/basics/network-ops/connecting.html",Priorities.HIGH,"HIGHtest1");
        requestList.add(reqeust);

        DownloadRequest reqeust2 = new DownloadRequest(new InputStreamReceiver() {
            @Override
            public void receiveInputStream(InputStream stream) {
                Log.e("ApplicationTest", "Received inputstream");
            }
        },"https://developer.android.com/training/basics/network-ops/connecting.html",Priorities.HIGH,"HIGHtest2");
        requestList.add(reqeust2);

        DownloadRequest reqeust3 = new DownloadRequest(new InputStreamReceiver() {
            @Override
            public void receiveInputStream(InputStream stream) {
                Log.e("ApplicationTest", "Received inputstream");
            }
        },"https://developer.android.com/training/basics/network-ops/connecting.html",Priorities.MEDIUM,"Mediumtest1");
        requestList.add(reqeust3);

        DownloadRequest reqeust4 = new DownloadRequest(new InputStreamReceiver() {
            @Override
            public void receiveInputStream(InputStream stream) {
                Log.e("ApplicationTest", "Received inputstream");
            }
        },"https://developer.android.com/training/basics/network-ops/connecting.html",Priorities.MEDIUM,"Mediumtest2");
        requestList.add(reqeust4);

        DownloadRequest reqeust5 = new DownloadRequest(new InputStreamReceiver() {
            @Override
            public void receiveInputStream(InputStream stream) {
                Log.e("ApplicationTest", "Received inputstream");
            }
        },"https://developer.android.com/training/basics/network-ops/connecting.html",Priorities.LOW,"Lowtest1");
        requestList.add(reqeust5);

        DownloadRequest reqeust6 = new DownloadRequest(new InputStreamReceiver() {
            @Override
            public void receiveInputStream(InputStream stream) {
                Log.e("ApplicationTest", "Received inputstream");
            }
        },"https://developer.android.com/training/basics/network-ops/connecting.html",Priorities.LOW,"Lowtest2");
        requestList.add(reqeust6);

        for(int index = 0; index < 10; index++) {
            DownloadRequest urgent = new DownloadRequest(new InputStreamReceiver() {
                @Override
                public void receiveInputStream(InputStream stream) {
                    Log.e("ApplicationTest", "Received inputstream");
                }
            }, "https://developer.android.com/training/basics/network-ops/connecting.html", Priorities.URGENT, "UrgentTest:"+index);
            requestList.add(urgent);
        }
        for(DownloadRequest r : requestList) {
            downloader.queueRequest(r);
            Log.e(TAG, "---------Request "+r.getRequestName()+" queued--------------");
        }
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
