package com.example.b07_course_selection_project;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import android.widget.EditText;

import com.example.b07_course_selection_project.MVP.LoginModel;
import com.example.b07_course_selection_project.MVP.LoginPresenter;
import com.example.b07_course_selection_project.MVP.LoginView;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(MockitoJUnitRunner.class)
public class ExampleUnitTest {

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
}