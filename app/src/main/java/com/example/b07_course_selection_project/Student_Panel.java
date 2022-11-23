package com.example.b07_course_selection_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.b07_course_selection_project.databinding.ActivityMainBinding;
import com.example.b07_course_selection_project.databinding.ActivityStudentPanelBinding;

public class Student_Panel extends AppCompatActivity {
    private ActivityStudentPanelBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStudentPanelBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Button add_courses = findViewById(R.id.add); //use binding.add as reference to the button instead

        //Make button open activity
        binding.completed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Student_Panel.this, completedCourses.class));
            }
        });
    }
}