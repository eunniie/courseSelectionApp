package com.example.b07_course_selection_project;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.b07_course_selection_project.databinding.ActivityStudentPanelBinding;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class Student_Panel extends AppCompatActivity {
    private ActivityStudentPanelBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStudentPanelBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Make button open activity
        binding.completed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Student_Panel.this, completedCourses.class));
            }
        });
        //make button display dialog
        binding.timeline.setOnClickListener(view -> displayAddCourses());
    }

    //To get the courses that the student wants to take in the future:
    //TODO: Get courses from database
    private String[] courses = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k"}; //The courses to display in dialog
    private boolean[] checkedCourses = new boolean[courses.length]; //Stores the checks
    private List<String> selectedCourses = new ArrayList<>(); //The courses student selects

    //Displays the list of courses for user to pick
    private void displayAddCourses() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Select the course(s) you would like to take:");

        //Stores courses selected
        dialogBuilder.setMultiChoiceItems(courses, checkedCourses,
                (dialogInterface, course, isSelected) -> {
                    if (isSelected) {
                        selectedCourses.add(courses[course]);
                    } else {
                        selectedCourses.remove(courses[course]);
                    }
                }
        );

        //Buttons go to timeline activity or cancel
        dialogBuilder.setPositiveButton("Generate", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent in = new Intent(Student_Panel.this, Timeline.class);
                startActivity(in);
            }
        });
        dialogBuilder.setNeutralButton("Cancel", (dialog, cancel) -> dialog.dismiss());

        dialogBuilder.show();
    }
}