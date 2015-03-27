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

import com.jsjrobotics.prioritydownloader.Priorities;
import com.jsjrobotics.prioritydownloader.PriorityDownloader;
import com.jsjrobotics.prioritydownloader.downloader.DownloadRequest;
import com.jsjrobotics.prioritydownloader.downloader.Downloader;
import com.jsjrobotics.prioritydownloader.downloader.InputStreamReceiver;
import com.jsjrobotics.prioritydownloader.downloader.InputStreamToObjectConverters;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * A test activity to exercise the downloader
 */
public class TestClass extends Activity {
    private static final String TAG = "TestClass";
    private PriorityDownloader downloader;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }


}
