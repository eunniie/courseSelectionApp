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
    private List<String> willComplete = new ArrayList<>();
    private List<String> timeline= new ArrayList<String>();


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
                finish();
            }
        });
        binding.gen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                }
            }
        });
        binding.restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected.clear();
                completedCourses.clear();
                timeline.clear();
                willComplete.clear();
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
                //If there are no courses, or user has completed all available courses
                if (courses.isEmpty()){
                    new AlertDialog.Builder(Timeline.this).setMessage
                                    ("There are no courses that you have not already taken, please contact an Admin.")
                            .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    finish();
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


        binding.timeline.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        binding.timeline.setAdapter(arrayAdapter);
        //Gets the courses that student wants to take
        binding.timeline.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //If selected already,
                if(selected.contains((String) adapterView.getItemAtPosition(i))){
                    Toast.makeText(Timeline.this, "You've already selected this course", Toast.LENGTH_SHORT).show();
                    binding.timeline.setItemChecked(i, true);

                }
                //If not selected yet, check if prereq fufilled and add to selected based on that
                else{
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Courses").child((String) adapterView.getItemAtPosition(i));
                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            List<String> pre = new ArrayList<>();
                            Course c = dataSnapshot.getValue(Course.class);
                            for (String course: c.getPreReq()){
                               if (!completedCourses.contains(course) && !willComplete.contains(course)) pre.add(course);
                            }
                            //If preReqs haven't been taken, Display error
                            if (!pre.isEmpty()){
                                binding.timeline.setItemChecked(i, false);
                                completedCourses.remove((String) adapterView.getItemAtPosition(i));
                                AlertDialog.Builder dialog = new AlertDialog.Builder(Timeline.this);
                                dialog.setMessage("You have not completed all of the prerequisites of this course. " +
                                        "Please select your missing prerequisites. " +
                                        "The prerequisites of this course are:  " + pre.toString() +".");
                                dialog.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                });
                                dialog.show();
                            //If preReqs have been taken, add to selected
                            }else{
                                selected.add((String) adapterView.getItemAtPosition(i));
                                willComplete.add((String) adapterView.getItemAtPosition(i));
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });

                }
                stop(courses, arrayAdapter);
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
        String text = "Here is your generated timeline";
        binding.titleSubtext.setText(text);
    }

    //selected courses is ordered from no prereq to most prereq

    private void genTimeline(){
        for (String course: selected){
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Courses").child(course);
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    int year = 2022;
                    String sem = "";

                    Course c = dataSnapshot.getValue(Course.class);
                    //if all prereqs are completed already
                    if (c.getPreReq().isEmpty()||completedCourses.containsAll(c.getPreReq())){
                        if (c.getTimeOffered().contains("Fall")){
                            combineTimeline("Fall", year, c.getCode());
                        }else if (c.getTimeOffered().contains("Winter")){
                            year+=1;
                            combineTimeline("Winter", year, c.getCode());
                        }else{
                            year+=1;
                            combineTimeline("Summer", year, c.getCode());
                        }

                    //if prereqs are in timeline
                    }else{
                        //Gets the year and semester of the most recent prereq that is taken
                        for (String pre: c.getPreReq()){
                            if (willComplete.contains(pre)){
                                for (String time: timeline){
                                    if (time.contains(pre)){
                                        String info [] = time.split(" ");
                                        if (Integer.parseInt(info[1].substring(0,4))>year || sem == ""){
                                            sem = info[0];
                                        }else{
                                            if (sem.compareTo(info[0])<0) sem = info[0];
                                        }
                                        if (year<Integer.parseInt(info[1].substring(0,4))) year = Integer.parseInt(info[1].substring(0,4));
                                    }
                                }
                            }
                        }

                        //Adds course to timeline based on when recent prereq is taken
                       if (sem.compareTo("Summer")==0){
                           if(c.getTimeOffered().contains("Fall")){
                               combineTimeline("Fall", year, c.getCode());
                           }else if (c.getTimeOffered().contains("Winter")){
                               combineTimeline("Winter", year+1, c.getCode());
                           }else{
                               combineTimeline("Summer", year+1, c.getCode());
                           }
                       }else if (sem.compareTo("Winter") ==0 ){
                            if(c.getTimeOffered().contains("Summer")){
                                combineTimeline("Summer", year, c.getCode());
                            }else if (c.getTimeOffered().contains("Fall")){
                                combineTimeline("Fall", year, c.getCode());
                            }else{
                                combineTimeline("Winter", year+1, c.getCode());
                            }
                       }else {
                           if(c.getTimeOffered().contains("Winter")){
                               combineTimeline("Winter", year+1, c.getCode());
                           }else if (c.getTimeOffered().contains("Summer")){
                               combineTimeline("Summer", year+1, c.getCode());
                           }else{
                               combineTimeline("Fall", year+1, c.getCode());
                           }
                       }
                    }
                    showTimeline(timeline);
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
    }

    private void combineTimeline(String sem, int year, String course){
        for (String semester: timeline){
            String[] info = semester.split(" ");
            if (info[0].equals(sem) && info[1].equals(year+":") ){
                timeline.set(timeline.indexOf(semester), semester + " " + course);
                return;
            }
        }
        timeline.add(sem +" " +year +": " +course);
    }

    //TODO: Sort timeline by Semester and year
}