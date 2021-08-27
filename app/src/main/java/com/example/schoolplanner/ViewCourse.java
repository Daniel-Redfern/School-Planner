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

import java.util.ArrayList;

public class ViewCourse extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Course course = getIntent().getParcelableExtra("data");
        setContentView(R.layout.activity_view_course);
        Button saveChangesButton = findViewById(R.id.view_course_save_changes);
        Button backButton = findViewById(R.id.view_course_back_button);
        Button removeButton = findViewById(R.id.view_course_remove_button);
        final EditText etName = findViewById(R.id.view_course_course_name_edit_text);
        final EditText etInstructor = findViewById(R.id.view_course_instructor_name_edit_text);
        etName.setText(course.getCourseName());
        etInstructor.setText(course.getInstructorName());

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ViewCourse.this, MainPage.class));
            }
        });

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Helper.removeCourseFromData(course, ViewCourse.this);
                startActivity(new Intent(ViewCourse.this, MainPage.class));
            }
        });

        saveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Helper.removeCourseFromData(course, ViewCourse.this);
                Course c = new Course(etName.getText().toString(), etInstructor.getText().toString());
                Helper.setupCourseFileFile(c, ViewCourse.this);
                Helper.saveCourse(c, ViewCourse.this);
                startActivity(new Intent(ViewCourse.this, MainPage.class));
            }
        });


    }

}
