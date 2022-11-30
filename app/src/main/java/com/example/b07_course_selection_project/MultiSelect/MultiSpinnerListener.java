package com.example.b07_course_selection_project.MultiSelect;
//Code from https://github.com/pratikbutani/MultiSelectSpinner
//Since Android Security prevented implementing the above github
import java.util.List;

public interface MultiSpinnerListener {
    void onItemsSelected(List<KeyPairBoolData> selectedItems);
}