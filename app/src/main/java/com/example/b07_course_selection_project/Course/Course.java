package com.example.b07_course_selection_project.Course;

import java.util.ArrayList;

//TODO: change changeTimeOffered appropriately based on the system of time we are using!

public class Course {
    protected String name;
    protected String code;
    protected ArrayList<String> preReq = new ArrayList<>();
    protected String timeOffered;

    public Course(String name, String code, ArrayList<String> preReq, String timeOffered) {
        this.name = name;
        this.code = code;
        this.preReq = preReq;
        this.timeOffered = timeOffered;
    }

    public String getName() {
        return this.name;
    }

    public String getCourseCode() {
        return this.code;
    }

    public Course [] getPreReq() {
        Course [] arr = new Course[preReq.size()];
        arr = preReq.toArray(arr); //making sure the array has the size and is of the same type

        return arr;
    }

    public String getTimeOffered() {
        return this.timeOffered;
    }

    public boolean changeName (String input) {
        if (input != null)
        {
            this.name = input;
            return true;
        }
        return false;
    }

    public boolean changeCourseCode (String input) {
        if (input != null)
        {
            this.code = input;
            return true;
        }
        return false;
    }

    public boolean deleteOnePreReq (Course input) {
        int index = this.preReq.indexOf(input);

        if (index != -1 && this.preReq.size() != 0)
        {
            this.preReq.remove(index);
            return true;
        }
        return false;
    }

    public boolean changeTimeOffered (String input) {
        if (input.equalsIgnoreCase("winter")) {
            this.timeOffered = input;
            return true;
        }

        else if (input.equalsIgnoreCase("summer")) {
            this.timeOffered = input;
            return true;
        }

        else if (input.equalsIgnoreCase("fall")) {
            this.timeOffered = input;
            return true;
        }
        return false;
    }
}
