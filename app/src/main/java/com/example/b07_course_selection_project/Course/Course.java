package com.example.b07_course_selection_project.Course;

import java.util.ArrayList;


public class Course {
    protected String name;
    protected String code;
    protected ArrayList<String> preReq = new ArrayList<>();
    protected String [] timeOffered;

    public Course(String name, String code, ArrayList<String> preReq, String [] timeOffered) {
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

    public String [] getTimeOffered() {
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

    //checks if input exists before deleting
    public boolean deleteOnePreReq (String input) {
        int index = this.preReq.indexOf(input);

        if (index != -1 && this.preReq.size() != 0)
        {
            this.preReq.remove(index);
            return true;
        }
        return false;
    }

    public boolean addOnePreReq (String input) {
        if (this.preReq.contains(input)) {
            return false;
        }
        else
        {
            this.preReq.add(input);
            return true;
        }
    }

    //it doesn't check for the validity of the strings in input. Just checks that there's no more
    //than 3 sessions for fall, winter and summer
    public boolean changeTimeOffered (String [] input) {

        if (input == null) {
            return false;
        }
        else if (input.length > 3) {
            return false;
        }

        this.timeOffered = input;
        return true;
    }
}
