package com.example.b07_course_selection_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.b07_course_selection_project.databinding.ActivityStudentPanelBinding;
import com.example.b07_course_selection_project.databinding.ActivityTimelineBinding;

public class Timeline extends AppCompatActivity {
    private ActivityTimelineBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTimelineBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Make button open activity
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Timeline.this, Student_Panel.class));
            }
        });
    }
}