package com.example.b07_course_selection_project;

import android.widget.Button;

public class Presenter implements PresenterInterface {

    public void onButtonClick(Button button) {
        if (button.getId() == R.id.login)
        {
            //logIn();
        }

        else if(button.getId() == R.id.register_button) {
            //goes to register screen
        }
    }
}
