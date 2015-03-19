package com.jsjrobotics.prioritydownloader.downloader;

import java.io.InputStream;

public interface InputStreamToObject<T> {
    public T convertInputStreamToObject(InputStream stream);
}
