package com.jsjrobotics.prioritydownloader;

import android.app.Application;

public class PriorityDownloaderApp extends Application {
    private static PriorityDownloaderApp app;

    @Override
    public void onCreate(){
        app = this;
    }

    public static PriorityDownloaderApp getContext(){
        return app;
    }
}
