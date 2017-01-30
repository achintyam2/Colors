package com.android.sms;


public class MyStudent {

    private String name,id, course;

    public MyStudent(String name, String id, String section) {
        this.name = name;
        this.id = id;
        this.course = section;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCourse(String course) {
        this.course = course;
    }



    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getCourse() {
        return course;
    }

}
