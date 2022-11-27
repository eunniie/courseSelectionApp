package com.example.b07_course_selection_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.b07_course_selection_project.Course.Course;
import com.example.b07_course_selection_project.databinding.ActivityStudentPanelBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class Student_Panel extends AppCompatActivity {
    private ActivityStudentPanelBinding binding;
    protected List<String> selectedCourses = new ArrayList<>(); //The courses student selects

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStudentPanelBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Make button open activity
        binding.completed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Student_Panel.this, completedCourses.class));
            }
        });
        //make button display dialog
        subtitleChange();
        binding.timeline.setOnClickListener(view -> displayAddCourses());
        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                finish();
            }
        });
    }

    private void subtitleChange(){
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Students").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        rootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    binding.TitleSubtext.setText("Welcome back " + snapshot.child("lastname").getValue(String.class) + "!");
                }
                else{
                    binding.TitleSubtext.setText("Welcome back Student!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        binding.TitleSubtext.setText("Welcome back ");
    }

    //To get the courses that the student wants to take in the future:
    private void displayAddCourses() {
        List<String> courses = new ArrayList<>();

        //TODO: Add example courses in firebase to check if this works:
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Courses");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                courses.clear();
                //iterate through each course in courses
                for (DataSnapshot snap: snapshot.getChildren()){
                    Course c = snap.getValue(Course.class);
                    courses.add(c.getCode());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        String[] courses_array = (String[]) courses.toArray();
        //String[] courses_array = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k"};//test
        boolean[] checkedCourses = new boolean[courses_array.length]; //Stores the checks

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Select the course(s) you would like to take:");

        //Stores courses selected
        dialogBuilder.setMultiChoiceItems(courses_array, checkedCourses,
                (dialogInterface, course, isSelected) -> {
                    if (isSelected) {
                        selectedCourses.add(courses_array[course]);
                    } else {
                        selectedCourses.remove(courses_array[course]);
                    }
                }
        );

        //Buttons go to timeline activity or cancel
        dialogBuilder.setPositiveButton("Generate", (dialogInterface, i) -> {
            Intent in = new Intent(Student_Panel.this, Timeline.class);
            startActivity(in);
        });
        dialogBuilder.setNeutralButton("Cancel", (dialog, cancel) -> dialog.dismiss());

        dialogBuilder.show();
    }
}