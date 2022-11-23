package com.example.b07_course_selection_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.b07_course_selection_project.Users.Student;
import com.example.b07_course_selection_project.databinding.ActivityCompletedCoursesBinding;
import com.example.b07_course_selection_project.databinding.ActivityMainBinding;

public class completedCourses extends AppCompatActivity {
    private ActivityCompletedCoursesBinding binding;
    ListView coursesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCompletedCoursesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Set courses codes to ListView
        coursesList = (ListView) binding.completedCourses;
        String[] arr = {"courses"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,arr);
        coursesList.setAdapter(arrayAdapter);

        //Make button open activity
        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(completedCourses.this, Student_Panel.class));
            }
        });
    }
}