package com.example.b07_course_selection_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.b07_course_selection_project.databinding.ActivityCompletedCoursesBinding;
import com.example.b07_course_selection_project.databinding.ActivityMainBinding;

public class completedCourses extends AppCompatActivity {
    private ActivityCompletedCoursesBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCompletedCoursesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Make button open activity
        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(completedCourses.this, Student_Panel.class));
            }
        });
    }
}