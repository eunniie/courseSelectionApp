package com.example.b07_course_selection_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.b07_course_selection_project.databinding.ActivityAdminAddBinding;
import com.example.b07_course_selection_project.databinding.ActivityRegisterBinding;

public class Admin_add extends AppCompatActivity {
    ActivityAdminAddBinding binding;
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
    }
    private void addCourse(){

    }
}