package com.example.b07_course_selection_project.Users;

import com.example.b07_course_selection_project.Course.Course;

import java.util.ArrayList;

public class Student extends User{
    public ArrayList<String> completedCoursesCode;

    public Student(String firstname, String lastname, String email){
        super(firstname, lastname, email, false);
        this.completedCoursesCode = new ArrayList<String>();
    }

    public Student(String firstname, String lastname, String email, ArrayList<String> completedCoursesCode){
        super(firstname, lastname, email, false);
        this.completedCoursesCode = completedCoursesCode;
    }

    public void addCoursesCode(Course course1){
        this.completedCoursesCode.add(course1.getCourseCode());
    }

    public String[] getCoursesCode(){
        String[] arr = new String[completedCoursesCode.size()];
        return completedCoursesCode.toArray(arr);
    }

}
