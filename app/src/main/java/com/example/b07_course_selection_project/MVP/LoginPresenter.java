package com.example.b07_course_selection_project.MVP;

public class LoginPresenter implements LoginModel.LoginListener {
    private LoginView loginView;
    private LoginModel inter;
    public LoginPresenter(LoginView loginView, LoginModel inter){
        this.loginView = loginView;
        this.inter = inter;
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
        if(inter != null)
            loginView.onLoginSuccess();
    }

    @Override
    public void onLoginError() {
        if(inter != null)
            loginView.onLoginError();
    }
    public void validateCredential(String email, String password){
        if(loginView != null)
            inter.login(email, password, this);
    }
}
