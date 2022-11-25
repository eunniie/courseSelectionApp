package com.example.b07_course_selection_project;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.example.b07_course_selection_project.MVP.LoginModel;
import com.example.b07_course_selection_project.MVP.LoginPresenter;
import com.example.b07_course_selection_project.MVP.LoginView;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(MockitoJUnitRunner.class)
public class LoginUnitTest {

    @Mock
    LoginView view;

    @Mock
    LoginModel model;

    @Test
    public void testPresenterEmailFake(){
        when(view.getEmail()).thenReturn("test");
        LoginPresenter presenter = new LoginPresenter(view, model);
        assertEquals(presenter.checkEmail(), false);
        verify(view).EmailValidError();
    }
    @Test
    public void testPresenterEmailEmpty(){
        when(view.getEmail()).thenReturn("");
        LoginPresenter presenter = new LoginPresenter(view, model);
        assertEquals(presenter.checkEmail(), false);
        verify(view).EmailError();
    }

    @Test
    public void testPresenterValidEmail(){
        when(view.getEmail()).thenReturn("test@test.com");
        LoginPresenter presenter = new LoginPresenter(view, model);
        assertEquals(presenter.checkEmail(), true);
    }

    @Test
    public void testPresenterPasswordEmpty(){
        when(view.getPassword()).thenReturn("");
        LoginPresenter presenter = new LoginPresenter(view, model);
        assertEquals(presenter.checkPassword(), false);
        verify(view).passwordError();
    }

    @Test
    public void testPresenterValidPassword(){
        when(view.getPassword()).thenReturn("passwordpassword");
        LoginPresenter presenter = new LoginPresenter(view, model);
        assertEquals(presenter.checkPassword(), true);
    }

    @Test
    public void testPresenterLogin(){
        when(view.getEmail()).thenReturn("test@testfail.com");
        when(view.getPassword()).thenReturn("passwordpassword");
        LoginPresenter presenter = new LoginPresenter(view, model);
        presenter.login();
        verify(model).login("test@testfail.com", "passwordpassword", presenter);
    }
    @Test
    public void testModelLoginError(){
        LoginPresenter presenter = new LoginPresenter(view, model);
        presenter.onLoginError();
        verify(view).onLoginError();
    }

    @Test
    public void testModelLoginSuccess(){
        LoginPresenter presenter = new LoginPresenter(view, model);
        presenter.onLoginSuccess();
        verify(view).onLoginSuccess();
    }

}