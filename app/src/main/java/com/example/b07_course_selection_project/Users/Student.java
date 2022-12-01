package com.example.b07_course_selection_project.Users;

import com.example.b07_course_selection_project.Course.Course;
import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Student extends User{
    public List<String> completedCoursesCode;

    public Student(){
        super("", "", "", false);
        this.completedCoursesCode = new ArrayList<String>();
    }
    public Student(String firstname, String lastname, String email){
        super(firstname, lastname, email, false);
        this.completedCoursesCode = new ArrayList<String>();
    }

    public Student(String firstname, String lastname, String email, ArrayList<String> completedCoursesCode){
        super(firstname, lastname, email, false);
        this.completedCoursesCode = completedCoursesCode;
    }

    public void addCoursesCode(Course course1){
        this.completedCoursesCode.add(course1.getCode());
    }

    public void removeCourse(String i){
        this.completedCoursesCode.remove(i);
    }

    public void addCourse(String i){
        this.completedCoursesCode.add(i);
    }

    public void sortCourse(){
        Collections.sort(this.completedCoursesCode);
    }

    @Exclude
    public List<String> getCoursesCode(){
        return this.completedCoursesCode;
    }

    @Exclude
    public String getcompletedCoursesCode(){
        return completedCoursesCode.toString();
    }

}
