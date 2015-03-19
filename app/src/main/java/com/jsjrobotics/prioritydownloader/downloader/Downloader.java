package com.jsjrobotics.prioritydownloader.downloader;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.jsjrobotics.prioritydownloader.PriorityDownloaderApp;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Downloader {
    private static final String TAG = "Downloader";


    private boolean isOnline(){
        PriorityDownloaderApp app = PriorityDownloaderApp.getContext();
        ConnectivityManager connMgr = (ConnectivityManager) app.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        boolean isWifiConn = networkInfo.isConnected();
        networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        boolean isMobileConn = networkInfo.isConnected();
        return isMobileConn || isWifiConn;
    }

    public String downloadAsString(String url){
        InputStream inputStream = downloadUrl(url);
        InputStreamToObject<String> stringConverter = InputStreamToObjectConverters.getStringConverter();
        String result = stringConverter.convertInputStreamToObject(inputStream);
        closeInputStream(inputStream);
        return result;
    }

    public Object downloadAndConvertInputStream(String url, InputStreamToObject converter){
        if(converter == null){
            Log.e(TAG, "Invalid parameters in downloadAndConvertInputStream");
            return null;
        }
        InputStream inputStream = downloadUrl(url);
        Object result = converter.convertInputStreamToObject(inputStream);
        closeInputStream(inputStream);
        return result;
    }

    public InputStream downloadAsInputStream(String url){
        return downloadUrl(url);
    }

    private InputStream downloadUrl(String urlToDownload){
        if(!isOnline()){
            return null;
        }
        InputStream inputStream = null;
        try {
            URL url = new URL(urlToDownload);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            Log.d(TAG, "The response is: " + response);
            inputStream = conn.getInputStream();
            return inputStream;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e(TAG,"Malformed url exception");
            closeInputStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG,"IOException");
            closeInputStream(inputStream);
        }
        return null;
    }

    static void closeInputStream(InputStream is) {
        if (is != null) {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG,"Failed to close input stream");
            }
        }

    }

}
