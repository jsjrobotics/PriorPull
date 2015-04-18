package com.jsjrobotics.prioritydownloader;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.jsjrobotics.prioritydownloader.downloader.DownloadRequest;
import com.jsjrobotics.prioritydownloader.downloader.InputStreamReceiver;
import com.jsjrobotics.prioritydownloader.downloader.InputStreamToObjectConverters;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNotSame;

/**
 * A test activity to exercise the downloader
 */
public class TestClass extends Activity {
    private static final String TAG = "TestClass";
    private PriorityDownloader downloader;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        File externalFilesDir = getExternalFilesDir(null);
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        downloader = new PriorityDownloader(connectivityManager,externalFilesDir);
    }

    public void testDownload(){
        DownloadRequest reqeust = new DownloadRequest(new InputStreamReceiver() {
            @Override
            public void receiveInputStream(InputStream stream) {
                assertNotNull(stream);
            }
        },"https://developer.android.com/training/basics/network-ops/connecting.html",Priorities.HIGH,"test");
        downloader.queueRequest(reqeust);
    }


    public void testThreadTargeting(){
        final String TEST_URL_1 = "https://developer.android.com/training/basics/network-ops/connecting.html" ;
        final Handler mainThreadHandler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                assertEquals("Failed to land on the main thread",getMainLooper(),Looper.myLooper());{
                }
            }};
        DownloadRequest request = new DownloadRequest(mainThreadHandler,0, InputStreamToObjectConverters.getStringConverter(),TEST_URL_1, Priorities.LOW,"main handler test");

        HandlerThread t = new HandlerThread("BackgroundThread");
        t.start();

        Handler backgroundHandler = new Handler(t.getLooper()){
            @Override
            public void handleMessage(Message msg){
                assertNotSame("Failed to land on background thread",Looper.getMainLooper(),Looper.myLooper());
            }
        };
        DownloadRequest backgroundRequest = new DownloadRequest(backgroundHandler,0, InputStreamToObjectConverters.getStringConverter(),TEST_URL_1,Priorities.HIGH,"background handler test");
        downloader.queueRequest(request);
        downloader.queueRequest(backgroundRequest);
    }

    public void testDownloadInCorrectPriority(){
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
            System.out.println("---------Request "+r.getRequestName()+" queued--------------");
        }
    }

}
