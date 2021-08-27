package com.example.schoolplanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ViewAssignment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ArrayList<Course> courses = getIntent().getParcelableArrayListExtra("courses");
        final Assignment assignment = getIntent().getParcelableExtra("assignment");
        setContentView(R.layout.activity_view_assignment);
        Helper.createButton("Remove Assignment", this);
        Button backButton = findViewById(R.id.view_assignment_back_button);
        Button removeButton = findViewById(R.id.view_assignment_remove_button);
        TextView title = findViewById(R.id.view_assignment_Assignment_Name_Goes_Here);
        TextView date = findViewById(R.id.view_assignment_Date_Due_Goes_Here);
        TextView desc = findViewById(R.id.view_assignment_Description_Goes_Here);
        title.setText(assignment.getAssignmentName());
        date.setText(assignment.getDateDueString());
        desc.setText(assignment.getDescription());
        RelativeLayout rl = findViewById(R.id.put_Photo_Stuff_Here);
        if(assignment.getCurrentPhotoPath().equals("No Photo Provided")){
            TextView tv = Helper.createTextView("No Photo Provided", this);
            rl.addView(tv);
        }else{
            Button b = Helper.createButton("View Image", this);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO: figure out how to resume activity so we don't have to pass this all back and forth
                    Intent i = new Intent(ViewAssignment.this, DisplayImage.class);
                    i.putExtra("courses", courses);
                    i.putExtra("assignment", assignment);
                    startActivity(i);
                }});
            rl.addView(b);
        }

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Helper.removeAssignmentFromData(courses, assignment, ViewAssignment.this);
                startActivity(new Intent(ViewAssignment.this, MainPage.class));
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ViewAssignment.this, MainPage.class));
            }
        });
    }
}