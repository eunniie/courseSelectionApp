package com.example.b07_course_selection_project.MVP;

import java.util.regex.Pattern;

public class LoginPresenter implements LoginModel.LoginListener {
    private LoginView loginView;
    private LoginModel model;
    public static final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );
    public LoginPresenter(LoginView loginView, LoginModel inter){
        this.loginView = loginView;
        this.model = inter;
    }
    @Override
    public void EmailError() {
        if(loginView != null)
            loginView.EmailError();
    }

    @Override
    public void EmailValidError(){
        if(loginView != null)
            loginView.EmailValidError();
    }

    @Override
    public void PasswordError() {
        if(loginView != null)
            loginView.passwordError();
    }

    @Override
    public void onLoginSuccess() {
        if(model != null)
            loginView.onLoginSuccess();
    }

    @Override
    public void onLoginError() {
        if(model != null)
            loginView.onLoginError();
    }

    public boolean checkEmail(){
        String check = loginView.getEmail();
        if(check.isEmpty()){
            EmailError();
            return false;
        }
        if(!EMAIL_ADDRESS_PATTERN.matcher(check).matches()){
            EmailValidError();
            return false;
        }
        return true;
    }

    public boolean checkPassword(){
        String check = loginView.getPassword();
        if(check.isEmpty()){
            PasswordError();
            return false;
        }
        return true;
    }

    public void login(){
        if(loginView != null && checkEmail() && checkPassword()){
            model.login(loginView.getEmail(), loginView.getPassword(), this);
        }
    }
}
