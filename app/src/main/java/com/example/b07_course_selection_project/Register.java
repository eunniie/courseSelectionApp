package com.example.b07_course_selection_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.b07_course_selection_project.databinding.ActivityMainBinding;
import com.example.b07_course_selection_project.databinding.ActivityRegisterBinding;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {
    private ActivityRegisterBinding binding;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        mAuth = FirebaseAuth.getInstance();
        setContentView(binding.getRoot());

        //home button stuff
        binding.registerHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //using home button to go back to login screen
                startActivity(new Intent(Register.this, MainActivity.class));
            }
        });
    }
}