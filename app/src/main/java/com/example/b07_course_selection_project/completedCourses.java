package com.example.b07_course_selection_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;

import com.example.b07_course_selection_project.Course.Course;
import com.example.b07_course_selection_project.Users.Student;
import com.example.b07_course_selection_project.databinding.ActivityCompletedCoursesBinding;
import com.example.b07_course_selection_project.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

//TODO: move the functionality for displaying courses in the list view outside of onCreate
//TODO: implement getCourses using the same way as displaying the couress

public class completedCourses extends AppCompatActivity {
    private ActivityCompletedCoursesBinding binding;
    private FirebaseAuth mAuth;
    private List<String> completedCourses = new ArrayList<>();
    private List<Course> courses =new ArrayList<>();
    private String[] code;
    private Cursor mCursor;
    private SimpleCursorAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCompletedCoursesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //mAuth = FirebaseAuth.getInstance();
        binding.searchCourse.setVisibility(View.GONE);
        //search = (SearchView) binding.searchCourse;

        //Set courses codes to ListView
        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, completedCourses);

        binding.courseList.setAdapter(arrayAdapter1);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users")
                .child("Students").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("completedCoursesCode");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap: snapshot.getChildren()){
                    completedCourses.add(snap.getValue(String.class));
                    arrayAdapter1.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getCourses() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Courses");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren()) {
                    courses.add(snap.getValue(Course.class));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}