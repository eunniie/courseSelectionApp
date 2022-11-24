package com.example.b07_course_selection_project.MVP;

public interface LoginView {
    String getEmail();
    String getPassword();
    void displayError(android.widget.EditText i, String message);
    void createText(String message);
    void EmailError();
    void EmailValidError();
    void passwordError();
    void showLoading();
    void hideLoading();
    void onLoginSuccess();
    void onLoginError();
}
