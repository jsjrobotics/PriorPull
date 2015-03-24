package com.jsjrobotics.prioritydownloader.downloader;

import java.io.InputStream;

/**
 * Converts an inputStream into an object
 */
public interface InputStreamReceiver {
    /**
     * The caller will close the inputStream after this function returns
     * @param stream
     */
    public void receiveInputStream(InputStream stream);
}
