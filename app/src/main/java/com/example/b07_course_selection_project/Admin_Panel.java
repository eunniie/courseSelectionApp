package com.example.b07_course_selection_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.b07_course_selection_project.databinding.ActivityAdminPanelBinding;

public class Admin_Panel extends AppCompatActivity {

    ActivityAdminPanelBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminPanelBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.addCoursesAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Admin_Panel.this, Admin_add.class));
            }
        });
    }
}