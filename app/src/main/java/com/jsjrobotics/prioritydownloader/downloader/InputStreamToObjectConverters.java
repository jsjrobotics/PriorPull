package com.jsjrobotics.prioritydownloader.downloader;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

public class InputStreamToObjectConverters {
    private static final String TAG = "Stream2ObjConverters";

    /**
     * Convert an input stream to a string
     * @return the string represented by the inputstream or null
     */
    public static InputStreamToObject<String> getStringConverter(){
        InputStreamToObject<String> stringConverter = new InputStreamToObject<String>() {
            @Override
            public String convertInputStreamToObject(InputStream stream) {
                StringBuilder builder = new StringBuilder();
                try {
                    Reader reader = new InputStreamReader(stream, "UTF-8");
                    char[] buffer = new char[30];
                    int bytesRead = reader.read(buffer);
                    while ( bytesRead != -1) {
                        builder.append(buffer,0,bytesRead);
                        bytesRead = reader.read(buffer);
                    }
                    return builder.toString();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Log.e(TAG, "Unsupported Encoding exception");
                    return null;
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, "IOException");
                    return null;
                }
            }
        };
        return stringConverter;
    }
}
