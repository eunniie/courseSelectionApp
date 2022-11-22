package com.example.b07_course_selection_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.b07_course_selection_project.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //binding to the specific activity
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        mAuth = FirebaseAuth.getInstance();
        setContentView(view);
        binding.login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                signIn();
            }
        });

        //button stuff
        binding.registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //using register button to go to register screen
                startActivity(new Intent(MainActivity.this, Register.class));
            }
        });
    }

    private void signIn(){
        String email = binding.email.getText().toString().trim();
        String password = binding.password.getText().toString().trim();

        //checking validity of input
        if(email.isEmpty()){
            binding.email.setError("Email is required!");
            binding.email.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.email.setError("Please provide valid email!");
            binding.email.requestFocus();
            return;
        }
        if(password.isEmpty()){
            binding.password.setError("Password is required!");
            binding.password.requestFocus();
            return;
        }

        //signing in
        binding.loading.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    // startActivity(new Intent(MainActivity.this, //user panel here));
                    Toast.makeText(MainActivity.this, "Successfully logged in!", Toast.LENGTH_LONG).show();
                    binding.loading.setVisibility(View.GONE);
                }else{
                    Toast.makeText(MainActivity.this, "Failed to login! Please check credentials!", Toast.LENGTH_LONG).show();
                    binding.loading.setVisibility(View.GONE);
                }
            }
        });
    }

}