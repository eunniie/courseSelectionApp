package com.example.b07_course_selection_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.b07_course_selection_project.Users.Admin;
import com.example.b07_course_selection_project.Users.Student;
import com.example.b07_course_selection_project.Users.User;
import com.example.b07_course_selection_project.databinding.ActivityMainBinding;
import com.example.b07_course_selection_project.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {
    private ActivityRegisterBinding binding;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        mAuth = FirebaseAuth.getInstance();
        setContentView(binding.getRoot());
        binding.registerInfo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                register();
            }
        });
        //home button stuff
        binding.registerHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //using home button to go back to login screen
                startActivity(new Intent(Register.this, MainActivity.class));
            }
        });
    }
    private void register(){
        String email = binding.emailRegister.getText().toString().trim();
        String password = binding.passwordRegister.getText().toString().trim();
        String firstname = binding.FirstnameInput.getText().toString().trim();
        String lastname = binding.LastnameInput.getText().toString().trim();
        String confPassword = binding.confPassword.getText().toString().trim();

        if(firstname.isEmpty()){
            binding.FirstnameInput.setError("Firstname is required!");
            binding.FirstnameInput.requestFocus();
            return;
        }
        if(lastname.isEmpty()){
            binding.LastnameInput.setError("Lastname is required!");
            binding.LastnameInput.requestFocus();
            return;
        }
        if(email.isEmpty()){
            binding.emailRegister.setError("Email is required!");
            binding.emailRegister.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.emailRegister.setError("Please provide valid email!");
            binding.emailRegister.requestFocus();
            return;
        }
        if(password.isEmpty()){
            binding.passwordRegister.setError("Password is required!");
            binding.passwordRegister.requestFocus();
            return;
        }
        if(password.length() < 6){
            binding.passwordRegister.setError("Password has to have atleast 6 characters!");
            binding.passwordRegister.requestFocus();
            return;
        }
        if(!confPassword.equals(password)) {
            binding.confPassword.setError("Password does not match!");
            binding.confPassword.requestFocus();
            return;
        }

        binding.loading.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            User user = new Student(firstname, lastname, email);
                            FirebaseDatabase.getInstance().getReference("Users").child("Students")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(Register.this, "Successfully Registered", Toast.LENGTH_LONG).show();
                                                binding.loading.setVisibility(View.GONE);
                                            }
                                            else{
                                                Toast.makeText(Register.this, "Register failed! Try again!", Toast.LENGTH_LONG).show();
                                                binding.loading.setVisibility(View.GONE);
                                            }
                                        }
                                    });
                        }else{
                            Toast.makeText(Register.this, "Register failed! Try again!", Toast.LENGTH_LONG).show();
                            binding.loading.setVisibility(View.GONE);
                        }
                    }
                });
    }
}