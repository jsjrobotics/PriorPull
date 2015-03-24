package com.jsjrobotics.prioritydownloader;

/**
 * Priorities of a DownloadRequest
 * Urgent spawns a thread
 * Other priorities will be downloaded in order according
 * to their ordinal number, lowest first
 *
 */
public enum Priorities {
    URGENT,
    HIGH,
    MEDIUM,
    LOW

}
