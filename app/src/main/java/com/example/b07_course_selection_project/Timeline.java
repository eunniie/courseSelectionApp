package com.example.b07_course_selection_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.b07_course_selection_project.Course.Course;
import com.example.b07_course_selection_project.Users.Student;
import com.example.b07_course_selection_project.databinding.ActivityTimelineBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Timeline extends AppCompatActivity {
    private ActivityTimelineBinding binding;
    private List<String> selected = new ArrayList<>();
    private List<String> completedCourses = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTimelineBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getCompletedCourses();
        getSelection();

        //Make button open activity
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Timeline.this, Student_Panel.class));
            }
        });
        binding.restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected.clear();
                completedCourses.clear();
                binding.gen.setVisibility(View.VISIBLE);
                String text = "Select the course(s) you'd like to take:";
                binding.titleSubtext.setText(text);
                getCompletedCourses();
                getSelection();
            }
        });
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

    private void getSelection(){
        selected.clear();

        List<String> courses = new ArrayList<>();
        ArrayAdapter<String> arrayAdapter = new
                ArrayAdapter<String>(Timeline.this, android.R.layout.simple_list_item_multiple_choice,
                courses);

       DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Courses");
        //displays list of courses they can take (Excluding courses already taken)
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap: snapshot.getChildren()){
                    Course c = snap.getValue(Course.class);
                    if (!completedCourses.contains(c.getCode())){
                        courses.add(c.getCode());
                    }
                }
                if (courses.isEmpty()){
                    new AlertDialog.Builder(Timeline.this).setMessage
                                    ("There are no courses that you have not already taken, please contact an Admin.")
                            .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    startActivity(new Intent(Timeline.this, Student_Panel.class));
                                }
                            }).show();
                    return;
                }
                arrayAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //Gets the courses that student wants to take
        binding.timeline.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        binding.timeline.setAdapter(arrayAdapter);
        binding.timeline.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //If already selected, delete from list of selected
                if(selected.contains((String) adapterView.getItemAtPosition(i))){
                    selected.remove((String) adapterView.getItemAtPosition(i));
                    stop(courses, arrayAdapter);

                }
                //If not selected yet, add to list of selected
                else{
                    selected.add((String) adapterView.getItemAtPosition(i));
                    stop(courses, arrayAdapter);
                }

                //Get preReq of selected course
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Courses").child((String) adapterView.getItemAtPosition(i));
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<String> pre = new ArrayList<>();
                        Course c = dataSnapshot.getValue(Course.class);
                        pre.addAll(c.getPreReq());

                        //If preReqs haven't been taken, Display error, remove from selected
                        if (!completedCourses.containsAll(pre)){
                            binding.timeline.setItemChecked(i, false);
                            selected.remove((String) adapterView.getItemAtPosition(i));
                            AlertDialog.Builder dialog = new AlertDialog.Builder(Timeline.this);
                            dialog.setMessage("You have not completed all of the prerequisites of this course. " +
                                    "The prerequisites of this course are:  " + pre.toString() +".");
                            dialog.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                            dialog.show();
                        }else{
                            Toast.makeText(Timeline.this, "Added " + c.getCode(), Toast.LENGTH_SHORT);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

            }
        });
    }

    private void stop(List<String> courses, ArrayAdapter<String> arrayAdapter){
        binding.gen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selected.isEmpty()){
                    AlertDialog.Builder dialog = new AlertDialog.Builder(Timeline.this);
                    dialog.setMessage("Please select at least one course");
                    dialog.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    dialog.show();
                    return;
                }
                courses.clear();
                arrayAdapter.notifyDataSetChanged();
                binding.timeline.setChoiceMode(AbsListView.CHOICE_MODE_NONE);
                binding.gen.setVisibility(View.INVISIBLE);
                binding.timeline.setAdapter(arrayAdapter);
                genTimeline();
            }
        });
    }
    private void showTimeline(List<String> timeline){
        ArrayAdapter<String> adapter = new
                ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, timeline);
        binding.timeline.setAdapter(adapter);
        String text = "Here is your generate timeline";
        binding.titleSubtext.setText(text);
    }

    private void genTimeline(){
        List<String> listItems= new ArrayList<String>();
        for (String course: selected){
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Courses").child(course);
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    Course c = dataSnapshot.getValue(Course.class);

                    if (c.getTimeOffered().contains("Fall")){
                        listItems.add("Fall 2022: " + c.getCode());
                    }else if (c.getTimeOffered().contains("Winter")){
                        listItems.add("Winter 2023: " + c.getCode());
                    }else{
                        listItems.add("Summer 2023: " + c.getCode());
                    }
                    Collections.sort(listItems);
                    showTimeline(listItems);
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }

    }
}