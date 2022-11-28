package com.example.b07_course_selection_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.b07_course_selection_project.Course.Course;
import com.example.b07_course_selection_project.databinding.ActivityStudentPanelBinding;
import com.example.b07_course_selection_project.databinding.ActivityTimelineBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Timeline extends AppCompatActivity {
    private ActivityTimelineBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTimelineBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ArrayList<String> selected = getSelection();

        //Make button open activity
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(Timeline.this, Student_Panel.class));
            }
        });
    }

    private ArrayList<String> getSelection(){
        ArrayList<String> courses = new ArrayList<>();
        ArrayList<String> selectedCourses = new ArrayList<>();
        ArrayAdapter<String> arrayAdapter = new
                ArrayAdapter<String>(Timeline.this, android.R.layout.simple_list_item_multiple_choice,
                courses);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Courses");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                courses.clear();
                for (DataSnapshot snap: snapshot.getChildren()){
                    Course c = snap.getValue(Course.class);
                    courses.add(c.getCode());
                }
                arrayAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        binding.timeline.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        binding.timeline.setAdapter(arrayAdapter);
        binding.timeline.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(selectedCourses.contains((String) adapterView.getItemAtPosition(i))){
                    selectedCourses.remove((String) adapterView.getItemAtPosition(i));
                    Toast.makeText(Timeline.this, "Deleted:" +(String) adapterView.getItemAtPosition(i), Toast.LENGTH_SHORT).show();
                }else{
                    selectedCourses.add((String) adapterView.getItemAtPosition(i));
                    Toast.makeText(Timeline.this, "Added:" +(String) adapterView.getItemAtPosition(i), Toast.LENGTH_SHORT).show();
                }

            }
        });
        return selectedCourses;
    }
}