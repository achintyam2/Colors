package com.android.sms;

/**
 * Created by Achintya on 18-01-2017.
 */

public class MyThread {

    private String threadId, message, time, name;

    MyThread(){}

    public MyThread(String threadId, String message, String time, String name) {
        this.threadId = threadId;
        this.message = message;
        this.time = time;
        this.name = name;
    }

    public String getThreadId() {
        return threadId;
    }

    public String getMessage() {
        return message;
    }

    public String getTime() {
        return time;
    }

    public String getName() {
        return name;
    }
}
