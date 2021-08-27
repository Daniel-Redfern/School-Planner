package com.example.schoolplanner;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class DisplayImage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ArrayList<Course> courses = getIntent().getParcelableArrayListExtra("courses");
        final Assignment assignment = getIntent().getParcelableExtra("assignment");
        setContentView(R.layout.activity_display_image);
        ImageView iv = findViewById(R.id.saved_image_goes_here);
        Button backButton = findViewById(R.id.display_image_back_button);
        File imgFile = new File(assignment.getCurrentPhotoPath());
        if(imgFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            iv.setImageBitmap(myBitmap);
        }else{
            Toast.makeText(DisplayImage.this, "Well I Either Fucked Up Or You Shouldn't Be Seeing This", Toast.LENGTH_LONG).show();
        }


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DisplayImage.this, ViewAssignment.class);
                i.putExtra("courses", courses);
                i.putExtra("assignment", assignment);
                startActivity(i);
            }
        });
    }

}
