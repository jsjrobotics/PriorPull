package com.jsjrobotics.prioritydownloader;

import com.jsjrobotics.prioritydownloader.downloader.DownloadRequest;

/**
 * A comparator for download requests
 */
public class DownloadRequestComparator implements java.util.Comparator<DownloadRequest> {


    @Override
    public int compare(DownloadRequest lhs, DownloadRequest rhs) {
        Priorities leftPriority = lhs.getPriority();
        Priorities rightPriority = rhs.getPriority();
        if(leftPriority.ordinal() < rightPriority.ordinal()){
            return -1;
        }
        else if(leftPriority.ordinal() == rightPriority.ordinal()){
            return 0;
        }
        else {
            return 1;
        }
    }
}
