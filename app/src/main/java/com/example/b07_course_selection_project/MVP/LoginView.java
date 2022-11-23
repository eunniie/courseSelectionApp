package com.example.b07_course_selection_project.MVP;

public interface LoginView {
    void EmailError();
    void EmailValidError();
    void passwordError();
    void showLoading();
    void hideLoading();
    void onLoginSuccess();
    void onLoginError();
}
