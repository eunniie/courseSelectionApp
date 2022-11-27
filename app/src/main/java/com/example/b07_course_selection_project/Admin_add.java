package com.example.b07_course_selection_project;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.example.b07_course_selection_project.databinding.ActivityAdminAddBinding;
import com.example.b07_course_selection_project.databinding.ActivityRegisterBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Admin_add extends AppCompatActivity {
    ActivityAdminAddBinding binding;
    boolean[] selectedSession;
    List<Integer> sessionList = new ArrayList<Integer>();
    String[] sessionArray = {"Fall", "Winter", "Summer"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.addCourses.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                addCourse();
            }
        });
        selectedSession = new boolean[sessionArray.length];
        binding.sessionselector.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                showSelector();
            }
        });
    }
    private void showSelector(){
        //create alertdialog
        AlertDialog.Builder builder = new AlertDialog.Builder(
                Admin_add.this
        );
        builder.setTitle("Select Sessions");
        builder.setCancelable(false); //this ignores the back button
        //create a multichoice set
        builder.setMultiChoiceItems(sessionArray, selectedSession, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                if(b){
                    //if clicked then we add into the selected list
                    sessionList.add(i);
                    Collections.sort(sessionList);
                }else{
                    //if not we remove
                    sessionList.remove(Integer.valueOf(i));
                }
            }
        });
        //ok selected
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //make string for display
                StringBuilder stringBuilder = new StringBuilder();
                for(int j = 0; j < sessionList.size(); j++){
                    stringBuilder.append(sessionArray[sessionList.get(j)]);
                    if(j != sessionList.size() - 1){
                        stringBuilder.append(", ");
                    }
                }
                //sets the text
                binding.sessionselector.setText(stringBuilder.toString());
            }
        });
        //cancel button in the selector
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss(); //exits menu
            }
        });
        //clear button in the selector
        builder.setNeutralButton("Clear", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                for(int j = 0; j < selectedSession.length; j++){
                    selectedSession[j] = false;
                    sessionList.clear();
                    binding.sessionselector.setText("");
                }
            }
        });
        builder.show(); //show the menu
    }

    private void addCourse(){
        String coursetype = binding.coursecode.getText().toString().trim();
        String coursenum = binding.coursenum.getText().toString().trim();
        if(coursenum.length() == 1){
            coursenum = "0" + coursenum;
        }
        String level = binding.courselevel.getText().toString().trim();
        List<String> sessions = fetchSession();
    }
    private boolean checkCourseType(String coursetype){
        if(coursetype.isEmpty()){
            binding.coursecode.setError("Course code cannot be empty!");
            binding.coursecode.requestFocus();
            return false;
        }
        if(coursetype.length() > 3){
            binding.coursecode.setError("Course code can only be 3 characters!");
            binding.coursecode.requestFocus();
            return false;
        }
        return true;
    }
    private boolean checkCourseNum(String coursenum){
        if(coursenum.isEmpty()){
            binding.coursenum.setError("Course number cannot be empty!");
            binding.coursenum.requestFocus();
            return false;
        }
        if(coursenum.length() > 2){
            binding.coursenum.setError("Course number should have only two characters!");
            binding.coursenum.requestFocus();
            return false;
        }
        return true;
    }
    private boolean checkLevel(String level){
        if(level.isEmpty()){
            binding.courselevel.setError("Level cannot be empty!");
            binding.courselevel.requestFocus();
            return false;
        }
        if(level.length() > 1){
            binding.courselevel.setError("Level should be single character!");
            binding.courselevel.requestFocus();
            return false;
        }
        return true;
    }
    private boolean checkSessions(List<String> sessions){
        if(sessions.isEmpty()) {
            binding.sessionselector.setError("Please choose atleast one session!");
            binding.sessionselector.requestFocus();
            return false;
        }
        return true;
    }
    private List<String> fetchSession(){
        List<String> result = new ArrayList<String>();
        for(int j = 0; j < sessionList.size(); j++){
            result.add(sessionArray[sessionList.get(j)]);
        }
        return result;
    }

    //TODO Make Prerequisite button for getting prerequisite of the course
}