package com.example.b07_course_selection_project.Users;

import com.example.b07_course_selection_project.Course.Course;

import java.util.ArrayList;

public class Student {
    protected String email;
    protected ArrayList<String> completedCoursesCode = new ArrayList<>();

    public void addCoursesCode(Course course1){
        this.completedCoursesCode.add(course1.getCourseCode());
    }

    public String[] getCoursesCode(){
        String[] arr = new String[completedCoursesCode.size()];
        return completedCoursesCode.toArray(arr);
    }

}
