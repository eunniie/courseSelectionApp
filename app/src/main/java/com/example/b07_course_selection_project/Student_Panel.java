package com.example.b07_course_selection_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Student_Panel extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_panel);

        Button add_courses = findViewById(R.id.add);
        Button completed_courses = findViewById(R.id.completed);

        //Make button open activity
        completed_courses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Student_Panel.this, completedCourses.class));
            }
        });
    }
}