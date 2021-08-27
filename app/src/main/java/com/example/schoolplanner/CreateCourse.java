package com.example.schoolplanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class CreateCourse extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_course);
        Button addAnotherButton = findViewById(R.id.add_another_course);
        Button doneButton = findViewById(R.id.create_course_done);
        Button backButton = findViewById(R.id.cc_back_button);
        final EditText courseName = findViewById(R.id.course_name_edit_text);
        final EditText instructorName = findViewById(R.id.instructor_name_edit_text);

        addAnotherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((!courseName.getText().toString().equals(""))&&(!instructorName.getText().toString().equals(""))) {
                    Course c = new Course(courseName.getText().toString(), instructorName.getText().toString());
                    resetFields(courseName, instructorName);
                    Helper.setupCourseFileFile(c, CreateCourse.this); //this also does a first save
                }else{
                    Toast.makeText(CreateCourse.this, "Make Sure All Fields Are Filled In Before Continuing", Toast.LENGTH_LONG).show();}
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((!courseName.getText().toString().equals(""))&&(!instructorName.getText().toString().equals(""))) {
                Course c = new Course(courseName.getText().toString(), instructorName.getText().toString());
                Helper.setupCourseFileFile(c, CreateCourse.this);
                startActivity(new Intent(CreateCourse.this, MainPage.class));
                }else{
                    Toast.makeText(CreateCourse.this, "Make Sure All Boxes Are Filled In Before Continuing", Toast.LENGTH_LONG).show();}
            }
        });

        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Boolean hasdata = Helper.checkForSavedData(CreateCourse.this);
                if(hasdata) {
                    startActivity(new Intent(CreateCourse.this, MainPage.class));
                }else{
                    Toast.makeText(CreateCourse.this, "You Must Have Courses Before You Can Go To The Main Page", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void resetFields(EditText name, EditText instructor){
        name.setHint("Enter Course Name Here");
        name.setText("");
        instructor.setText("");
        instructor.setHint("Enter Instructor Name Here");
    }
}