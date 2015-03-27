package com.jsjrobotics.prioritydownloader;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ActivityTestCase;
import android.test.ApplicationTestCase;
import android.util.Log;

import com.jsjrobotics.prioritydownloader.downloader.DownloadRequest;
import com.jsjrobotics.prioritydownloader.downloader.Downloader;
import com.jsjrobotics.prioritydownloader.downloader.InputStreamReceiver;
import com.jsjrobotics.prioritydownloader.downloader.InputStreamToObjectConverters;

import junit.framework.TestCase;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ApplicationTest extends ActivityInstrumentationTestCase2<TestClass> {


    private PriorityDownloader download;
    private File externalFilesDir;
    private ConnectivityManager connectivityManager;
    private TestClass activity;

    public ApplicationTest() {
        super(TestClass.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        activity = getActivity();
        externalFilesDir = activity.getExternalFilesDir(null);
        connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        download = new PriorityDownloader(connectivityManager,externalFilesDir);
    }

    public void tearDown(){
        download.deleteCacheDir();
    }

    /*public void testPreconditions() {
        assertNotNull("Activity is null",activity);
        assertNotNull("External files directory is null",externalFilesDir);
        assertNotNull("Connectivity manager is null",connectivityManager);
    }*/

    public void testDownload(){
        DownloadRequest reqeust = new DownloadRequest(new InputStreamReceiver() {
            @Override
            public void receiveInputStream(InputStream stream) {
                assertNotNull(stream);
            }
        },"https://developer.android.com/training/basics/network-ops/connecting.html",Priorities.HIGH,"test");
        download.queueRequest(reqeust);
    }


    public void testThreadTargeting(){
        final String TEST_URL_1 = "https://developer.android.com/training/basics/network-ops/connecting.html" ;
        final Handler mainThreadHandler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                assertEquals("Failed to land on the main thread",activity.getMainLooper(),Looper.myLooper());{
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
        download.queueRequest(request);
        download.queueRequest(backgroundRequest);
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
            download.queueRequest(r);
            System.out.println("---------Request "+r.getRequestName()+" queued--------------");
        }
    }

}