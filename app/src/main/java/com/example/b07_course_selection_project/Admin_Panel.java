package com.example.b07_course_selection_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SearchView;

import com.example.b07_course_selection_project.Course.Course;
import com.example.b07_course_selection_project.databinding.ActivityAdminPanelBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Admin_Panel extends AppCompatActivity {

    private List<Course> courses;
    private CourseAdapter adapter;
    ActivityAdminPanelBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminPanelBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.searchCourse.setIconifiedByDefault(false);
        binding.searchCourse.setSubmitButtonEnabled(true);
        binding.searchCourse.onActionViewExpanded();
        binding.searchCourse.setQueryHint("Enter course code");
        fetchCourses();
        binding.addCoursesAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Admin_Panel.this, Admin_add.class));
            }
        });
        binding.adminLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                finish();
            }
        });
        binding.searchCourse.clearFocus();
    }
    @Override
    protected void onResume(){
        super.onResume();
        fetchCourses();
    }

    private void fetchCourses(){
        FirebaseDatabase.getInstance().getReference("Courses").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                courses = new ArrayList<Course>();
                for(DataSnapshot i: snapshot.getChildren()){
                    courses.add(i.getValue(Course.class));
                }
                if(!courses.isEmpty()) {
                    adapter = new CourseAdapter(Admin_Panel.this, courses);
                    binding.courseList.setAdapter(adapter);
                    binding.courseList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Intent intent = new Intent(Admin_Panel.this, Admin_change_info.class);
                            intent.putExtra("passed", (Serializable) adapterView.getItemAtPosition(i));
                            startActivity(intent);
                        }
                    });
                    initSearch();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                return;
            }
        });
    }
    private void initSearch(){
        binding.searchCourse.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                adapter.getFilter().filter(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                return false;
            }
        });
    }

}