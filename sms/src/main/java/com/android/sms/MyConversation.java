package com.android.sms;


public class MyConversation {

    private String threadId, message, time, name;

    public MyConversation(String threadId, String message, String time, String name) {
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
