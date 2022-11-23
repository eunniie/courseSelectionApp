package com.example.b07_course_selection_project;

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
        String email = binding.email.getText().toString().trim();
        String password = binding.password.getText().toString().trim();

        showLoading();
        presenter.validateCredential(email, password);
    }

    @Override
    public void EmailError() {
        hideLoading();
        binding.email.setError("Email is required!");
        binding.email.requestFocus();
    }
    @Override
    public void EmailValidError(){
        hideLoading();
        binding.email.setError("Please provide valid email!");
        binding.email.requestFocus();
    }

    @Override
    public void passwordError() {
        hideLoading();
        binding.password.setError("Password is required!");
        binding.password.requestFocus();
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
    public void onLoginSuccess() {
        hideLoading();
        Toast.makeText(this, "Successfully logged-in!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLoginError() {
        hideLoading();
        Toast.makeText(this, "Email and/or Password is incorrect!", Toast.LENGTH_LONG).show();
    }
}