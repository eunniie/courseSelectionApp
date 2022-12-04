package com.example.b07_course_selection_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.example.b07_course_selection_project.Course.Course;
import com.example.b07_course_selection_project.Users.Student;
import com.example.b07_course_selection_project.databinding.ActivityCompletedCoursesBinding;
import com.example.b07_course_selection_project.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class completedCourses extends AppCompatActivity {
    private ActivityCompletedCoursesBinding binding;
    private List<String> completedCourses = new ArrayList<>();
    private List<Course> courses =new ArrayList<>();
    private List<String> code = new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCompletedCoursesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //Set courses codes to ListView
        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, completedCourses);
        arrayAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,code);

        binding.courseList.setAdapter(arrayAdapter1);
        FirebaseDatabase.getInstance().getReference().child("Users")
                .child("Students").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("completedCoursesCode").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (binding.searchCourse.getVisibility() == View.GONE) {
                    for (DataSnapshot snap : snapshot.getChildren()) {
                        completedCourses.add(snap.getValue(String.class));
                        arrayAdapter1.notifyDataSetChanged();
                    }
                    Collections.sort(completedCourses);
                    arrayAdapter1.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Log.d("COMPLETED COURSES: ", completedCourses.toString());
        binding.courseList.setAdapter((arrayAdapter1));

        //Make button open activity
        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.searchCourse.getVisibility() == View.GONE) {
                    finish();
                }
                else{
                    binding.courseList.setAdapter(arrayAdapter1);
                    binding.searchCourse.setVisibility(View.GONE);
                    binding.searchCourse.setQuery("", false);
                    binding.titleText.setText("Completed Courses");
                    binding.addCourses.setVisibility(View.VISIBLE);
                }
            }
        });

        //Set up the add button and adding course panel
        binding.addCourses.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                binding.titleText.setText("Adding Completed Courses");
                binding.searchCourse.setVisibility(View.VISIBLE);
                binding.addCourses.setVisibility((View.GONE));
                FirebaseDatabase.getInstance().getReference().child("Courses").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        code = new ArrayList<>();
                        courses = new ArrayList<>();
                        arrayAdapter2 = new ArrayAdapter<String>(completedCourses.this, android.R.layout.simple_list_item_1,code);
                        for (DataSnapshot snap : snapshot.getChildren()) {
                            courses.add(snap.getValue(Course.class));
                            code.add(snap.getValue(Course.class).getCode());
                            arrayAdapter2.notifyDataSetChanged();
                        }
                        binding.courseList.setAdapter(arrayAdapter2);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
                binding.courseList.setOnScrollListener(new AbsListView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(AbsListView view, int scrollState) {
                    }
                    @Override
                    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    }
                });

                //enable selecting course from ListView
                binding.courseList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        boolean f = true;
                        if (binding.searchCourse.getVisibility() == View.VISIBLE) {
                            String added = adapterView.getItemAtPosition(i).toString().trim();
                            if (!completedCourses.contains(added)) {
                                for (String item : courses.get(code.indexOf(added)).preReq) {
                                    if (!completedCourses.contains(item)) {
                                        f = false;
                                        break;
                                    }
                                }
                                if (f) {
                                    completedCourses.add(added);
                                    Collections.sort(completedCourses);
                                    Toast.makeText(completedCourses.this, "Adding succeed!", Toast.LENGTH_SHORT).show();
                                    FirebaseDatabase.getInstance().getReference().child("Users")
                                            .child("Students").child(FirebaseAuth.getInstance()
                                                    .getCurrentUser().getUid()).child("completedCoursesCode").setValue(completedCourses);
                                    //declaring observer below
                                    FirebaseDatabase.getInstance().getReference("Courses").child(added).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            Course temp = snapshot.getValue(Course.class);
                                            temp.addUser(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                            FirebaseDatabase.getInstance().getReference("Courses").child(added).setValue(temp);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                        }

                                    });
                                }
                                else{
                                    Toast.makeText(completedCourses.this, "Take Prerequisite!", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else{
                                Toast.makeText(completedCourses.this, "Course already added!", Toast.LENGTH_SHORT).show();
                                }

                        }
                    }
                });

                //enable search course
                binding.searchCourse.setIconifiedByDefault(false);
                binding.searchCourse.setSubmitButtonEnabled(true);
                binding.searchCourse.onActionViewExpanded();
                binding.searchCourse.setQueryHint("Enter course code");
                binding.searchCourse.setBackgroundColor(0x22ff00ff);

                binding.searchCourse.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
                    private String TAG = getClass().getSimpleName();
                    @Override
                    public boolean onQueryTextChange(String queryText) {
                        arrayAdapter2.getFilter().filter(queryText);
                        return false;
                    }

                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        Log.d(TAG, "onQueryTextSubmit = " + query);
                        arrayAdapter2.getFilter().filter(query);
                        if (code.contains(query.trim()) && (!completedCourses.contains(query))) {
                            completedCourses.add(query);
                            Collections.sort(completedCourses);
                            Toast.makeText(completedCourses.this, "Adding succeed!", Toast.LENGTH_SHORT).show();
                            FirebaseDatabase.getInstance().getReference().child("Users")
                                    .child("Students").child(FirebaseAuth.getInstance()
                                            .getCurrentUser().getUid()).child("completedCoursesCode").setValue(completedCourses);
                            return true;
                        } else {
                            if (!code.contains(query.trim())) {
                                Toast.makeText(completedCourses.this, "Course doesn't exist!", Toast.LENGTH_SHORT).show();
                                return false;
                            } else {
                                Toast.makeText(completedCourses.this, "Course already added!", Toast.LENGTH_SHORT).show();
                                return false;
                            }
                        }
                    }
                });
                binding.searchCourse.clearFocus();
            }
        });
    }
}