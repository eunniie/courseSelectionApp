package com.example.b07_course_selection_project.Course;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class Course {
    public String name;
    public String code;
    public List<String> preReq;
    public List<String> timeOffered;
    public List<String> uid;
    public List<String> dependent;

    //need empty constructor b/c reading from firebase crashes without it
    public Course(){
        this.name = "";
        this.code = "";
        this.preReq = new ArrayList<String>();
        this.timeOffered = new ArrayList<String>();
        this.uid = new ArrayList<String>();
        this.dependent = new ArrayList<String>();
    }

    public Course(String name, String code){
        this.name = name;
        this.code = code;
        this.preReq = new ArrayList<String>();
        this.timeOffered = new ArrayList<String>();
        this.uid = new ArrayList<String>();
        this.dependent = new ArrayList<String>();
    }

    public Course(String name, String code, List<String> timeOffered){
        this.name = name;
        this.code = code;
        this.preReq = new ArrayList<String>();
        this.timeOffered = timeOffered;
        this.uid = new ArrayList<String>();
        this.dependent = new ArrayList<String>();
    }

    public Course(String name, String code, List<String> preReq, List<String> timeOffered) {
        this.name = name;
        this.code = code;
        this.preReq = preReq;
        this.timeOffered = timeOffered;
        this.uid = new ArrayList<String>();
        this.dependent = new ArrayList<String>();
    }

    public Course(String name, String code, List<String> preReq, List<String> timeOffered, List<String> uid) {
        this.name = name;
        this.code = code;
        this.preReq = preReq;
        this.timeOffered = timeOffered;
        this.uid = uid;
        this.dependent = new ArrayList<String>();
    }

    public Course(String name, String code, List<String> preReq, List<String> timeOffered, List<String> uid, List<String> dependent) {
        this.name = name;
        this.code = code;
        this.preReq = preReq;
        this.timeOffered = timeOffered;
        this.uid = uid;
        this.dependent = dependent;
    }

    public String getName() {
        return this.name;
    }

    public String getCode() {
        return this.code;
    }

    public List<String> getPreReq() {
        return preReq;
    }

    public List<String> getTimeOffered() { return this.timeOffered; }

    public List<String> getUid() {
        return this.uid;
    }
    public List<String> getDependent(){
        return this.dependent;
    }

    public void setDependent(List<String> dependent){
        this.dependent = dependent;
    }
    public void setPreReq(List<String> preReq){
        this.preReq = preReq;
    }
    public void setTimeOffered(List<String> timeOffered){
        this.timeOffered = timeOffered;
    }
    public void setUid(List<String> uid){
        this.uid = uid;
    }

    public boolean changeName (String input) {
        if (input != null)
        {
            this.name = input;
            return true;
        }
        return false;
    }

    public boolean dependentExist(String code){
        return this.dependent.contains(code);
    }

    public void dependentAdd(String code){
        if(!this.dependentExist(code)){
            this.dependent.add(code);
            Collections.sort(this.dependent);
        }
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

    public boolean addTimeOffered(String input){
        this.timeOffered.add(input);
        return true;
    }

    //it doesn't check for the validity of the strings in input. Just checks that there's no more
    //than 3 sessions for fall, winter and summer
    public boolean changeTimeOffered (List<String> input) {

        if (input == null) {
            return false;
        }
        else if (input.size() > 3) {
            return false;
        }

        this.timeOffered = input;
        return true;
    }

    public boolean userExists(String userid){
        return uid.contains(userid);
    }

    public void addUser(String userid){
        if(!userExists(userid))
        {
            this.uid.add(userid);
        }

    }
    public String getPrereqString(){
       String result = "";
       for(String i: this.preReq){
           result += i + ", ";
       }
       if(result.isEmpty())
           return "N/A";
       return result.substring(0, result.length() - 2);
    }
    public String getSessionString(){
        String result = "";
        for(String i: this.timeOffered){
            if(i.equals("Summer")){
                result += "S";
            }
            else if(i.equals("Winter")){
                result += "W";
            }
            else{
                result += "F";
            }
            result += ", ";
        }
        return result.substring(0, result.length() - 2);
    }

}
