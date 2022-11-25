package com.example.b07_course_selection_project.MVP;

import android.util.Patterns;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginModel {
    interface LoginListener{
        void EmailError();
        void EmailValidError();
        void PasswordError();
        void onLoginSuccess();
        void onLoginError();
    }
    public void login(String email, String password, LoginListener inter){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        //signing in
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    inter.onLoginSuccess();
                }else{
                    inter.onLoginError();
                }
            }
        });
    }
}
