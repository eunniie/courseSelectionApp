package com.example.b07_course_selection_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.b07_course_selection_project.MVP.LoginModel;
import com.example.b07_course_selection_project.MVP.LoginPresenter;
import com.example.b07_course_selection_project.MVP.LoginView;
import com.example.b07_course_selection_project.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements LoginView {
    private ActivityMainBinding binding;
    private FirebaseAuth mAuth;
    private LoginPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //binding to the specific activity
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        mAuth = FirebaseAuth.getInstance();
        setContentView(view);
        presenter = new LoginPresenter(this, new LoginModel());
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
        showLoading();
        presenter.login();
    }

    @Override
    public void displayError(android.widget.EditText i, String message){
        i.setError(message);
        i.requestFocus();
    }

    @Override
    public String getEmail(){
        return binding.email.getText().toString().trim();
    }

    @Override
    public String getPassword(){
        return binding.password.getText().toString().trim();
    }

    @Override
    public void EmailError() {
        hideLoading();
        displayError(binding.email, "Email is required!");
    }
    @Override
    public void EmailValidError(){
        hideLoading();
        displayError(binding.email, "Please provide valid email!");
    }

    @Override
    public void passwordError() {
        hideLoading();
        displayError(binding.password, "Password is required!");
    }

    @Override
    public void showLoading() {
        binding.loading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        binding.loading.setVisibility(View.GONE);
    }

    @Override
    public void createText(String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLoginSuccess() {
        hideLoading();
        createText("Successfully logged-in!");
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Admins").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    //admin panel
                }
                else{
                    startActivity(new Intent(MainActivity.this, Student_Panel.class));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                return;
            }
        });
        binding.email.setText("");
        binding.password.setText("");
    }

    @Override
    public void onLoginError() {
        hideLoading();
        createText("Email and/or Password is incorrect!");
    }
}