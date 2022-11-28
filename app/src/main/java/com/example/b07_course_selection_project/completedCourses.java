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

public class completedCourses extends AppCompatActivity {
    private ActivityCompletedCoursesBinding binding;
    private FirebaseAuth mAuth;
    private List<String> completedCourses = new ArrayList<>();
    private List<Course> courses =new ArrayList<>();
    private String[] code;
    private Cursor mCursor;
    private SimpleCursorAdapter mAdapter;
    ListView coursesList;
    SearchView search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCompletedCoursesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();

        binding.searchCourse.setVisibility(View.GONE);
        search = (SearchView) binding.searchCourse;

        //Set courses codes to ListView
        getCompletedCourses();
        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,completedCourses);
        coursesList.setAdapter(arrayAdapter1);

        //Make button open activity
        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.searchCourse.getVisibility() == View.GONE) {
                    startActivity(new Intent(completedCourses.this, Student_Panel.class));
                }
                else{
                    coursesList.setAdapter(arrayAdapter1);
                    binding.searchCourse.setVisibility(View.GONE);
                }
            }
        });

        //Set up the add button
        binding.addCourses.setOnClickListener((new View.OnClickListener(){
            @Override
            public void onClick(View v){
                binding.searchCourse.setVisibility(View.VISIBLE);
                mCursor = getContentResolver().query(null, null, null, null, null);
                getCourses();
                int j = 0;
                for (Course i:courses){
                    code[j] = i.getCode();
                    j++;
                }
                mAdapter = new SimpleCursorAdapter(null, android.R.layout.simple_list_item_1, mCursor,
                        code, new int[] { android.R.id.text1 }, 0);
                coursesList.setAdapter(mAdapter);
                coursesList.setOnScrollListener(new AbsListView.OnScrollListener() {

                    @Override
                    public void onScrollStateChanged(AbsListView view, int scrollState) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (imm != null) {
                            imm.hideSoftInputFromWindow(coursesList.getWindowToken(), 0);
                        }
                    }

                    @Override
                    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    }
                });

                search.setIconifiedByDefault(false);
                search.setSubmitButtonEnabled(true);
                search.onActionViewExpanded();
                search.setQueryHint("Enter course code");
                search.setBackgroundColor(0x22ff00ff);

                search.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
                    private String TAG = getClass().getSimpleName();
                    @Override
                    public boolean onQueryTextChange(String queryText) {
                        Log.d(TAG, "onQueryTextChange = " + queryText);
                        String selection = code + " LIKE '%" + queryText + "%' " + " OR "
                                + code + " LIKE '%" + queryText + "%' ";
                        mCursor = getContentResolver().query(null, null, selection, null, null);
                        mAdapter.swapCursor(mCursor);
                        return true;
                    }

                    @Override
                    public boolean onQueryTextSubmit(String queryText) {
                        Log.d(TAG, "onQueryTextSubmit = " + queryText);
                        if (search != null) {
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            if (imm != null) {
                                imm.hideSoftInputFromWindow(search.getWindowToken(), 0);                 }
                                search.clearFocus();               }
                                return true;
                    }
                });
            }
        }));
    }

    private void getCompletedCourses(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users")
                .child("Students").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("completedCoursesCode");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap: snapshot.getChildren()){
                    completedCourses.add(snap.getValue(String.class));
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