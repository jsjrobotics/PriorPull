package com.jsjrobotics.prioritydownloader.downloader;

import java.io.InputStream;

public interface InputStreamReceiver {
    /**
     * The caller will close the inputStream for you
     * @param stream
     */
    public void receiveInputStream(InputStream stream);
}
