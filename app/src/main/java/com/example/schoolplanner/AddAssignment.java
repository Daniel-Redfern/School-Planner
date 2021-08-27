package com.example.schoolplanner;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddAssignment extends AppCompatActivity {
    //TODO: DO PHOTO STUFF

    Course currentCourse;
    String currentPhotoPath = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_assignment);
        /** SETTING UP BACK END THINGS UP**/
        currentCourse = getIntent().getParcelableExtra("data");
        Button doneButton = findViewById(R.id.add_item_save_button);
        Button imageButton = findViewById(R.id.add_image_button);
        Button addAnotherAssignmentButton = findViewById(R.id.add_another_assignment_button);
        Button backButton = findViewById(R.id.back_button);
        final EditText assignmentDateDueET = findViewById(R.id.date_due_edit_text);
        final EditText assignmentNameET = findViewById(R.id.assignment_name_edit_text);
        final EditText assignmentDescriptionET = findViewById(R.id.assignment_description_edit_text);

        //TODO:
        assignmentDateDueET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String dateString = assignmentDateDueET.getText().toString();
                if(dateString.length()==10) {//check for a valid and complete date
                    String defaultAssignmentName = currentCourse.getCourseName() + " Homework: " + dateString.substring(0, 5);
                    assignmentNameET.setText(defaultAssignmentName);
                }
            }
            });

        //TODO: IMAGE BUTTON STUFF - Done
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
        dispatchTakePictureIntent();
            }});


        //TODO: DONE BUTTON STUFF - Done
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            createAssignment(assignmentDateDueET, assignmentNameET, assignmentDescriptionET);
            startActivity(new Intent(AddAssignment.this, MainPage.class));
            }
        });

        //TODO: ADD ANOTHER BUTTON STUFF - Done
        addAnotherAssignmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean foo = createAssignment(assignmentDateDueET, assignmentNameET, assignmentDescriptionET);
                if(foo){
                    resetFeilds(assignmentDateDueET, assignmentNameET, assignmentDescriptionET);
                }
            }
        });

        //TODO: BACK BUTTON STUFF - Done
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             startActivity(new Intent(AddAssignment.this, MainPage.class));
            }
        });




    }
/**------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------**/
    //Helper Methods
    private boolean createAssignment(final EditText dueDate, final EditText assName, final EditText description){
        String date = dueDate.getText().toString();
        String name = assName.getText().toString();
        String disc = description.getText().toString();
        Date today = java.util.Calendar.getInstance().getTime();
        if(date.length()==0){
            Toast.makeText(AddAssignment.this, "Make Sure To Enter a Date", Toast.LENGTH_LONG).show();
        }else {
            if(date.length()!=10){
                Toast.makeText(AddAssignment.this, "Make Sure That You Enter The Full Date, MM/DD/YYYY", Toast.LENGTH_LONG).show();
            }
            else {
                Date dayDue = null;
                try {
                    SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
                    dayDue = format.parse(date);
                } catch (Exception e) {
                    Toast.makeText(AddAssignment.this, "You Should Not Be Getting This, If You Do Though, Please Make Sure The Date Is Input Correctly", Toast.LENGTH_LONG).show();
                }
                if(dayDue!=null){
                    if(dayDue.equals(today) || dayDue.before(today)){
                        Toast.makeText(AddAssignment.this, "The Date Assignments Are Due Must Be In The Future", Toast.LENGTH_LONG).show();
                    }
                    else{
                        Calendar c = Calendar.getInstance();
                        c.add(Calendar.YEAR, 1);
                        Date tooFarAhead = c.getTime();
                        if(tooFarAhead.before(dayDue)){
                            Toast.makeText(AddAssignment.this, "Assignments Shouldn't Be Due More Than A Year Out, Check If You Entered The Correct Year", Toast.LENGTH_LONG).show();
                        }
                        else{
                            if(disc.equals("")){disc = "No Description Provided";}
                            if(currentPhotoPath==null){currentPhotoPath="No Photo Provided";}
                            if(!name.equals("")){
                            Assignment a = new Assignment(date, name, disc, currentPhotoPath);
                            currentCourse.addAssignment(a);
                            Helper.saveCourse(currentCourse, AddAssignment.this);
                            currentPhotoPath = null;
                            return true;
                            }else{
                                Toast.makeText(AddAssignment.this, "Make Sure That You Have An Assignment Name", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private void resetFeilds(EditText date, EditText name, EditText disc){
        date.setText("");
        name.setText("");
        disc.setText("");
        date.setHint("MM/DD/YYYY");
        name.setHint("Enter Name Here");
        disc.setHint("(Not Required)");
    }

/**------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------**/
    //Photo Saving Stuff
    final static int REQUEST_IMAGE_CAPTURE = 1;

    /**
     * A method for taking and saving a photo to the camera roll
     */
    private void dispatchTakePictureIntent(){
        //creating a new intent for taking a photo
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //checking that there is actually a photo taking device on the phone
        if(takePictureIntent.resolveActivity(getPackageManager())!=null){
            // creating the file where the photo should go
            File photoFile = null;
            try{
                photoFile = createImageFile();
            }catch(IOException e){
                Toast.makeText(AddAssignment.this, "Something Went Wrong Somewhere, Honestly I Got No Clue...", Toast.LENGTH_LONG).show();
            }
            //if photoFile was sucessfully created
            if(photoFile!=null){
                Uri photoURI = FileProvider.getUriForFile(this, "com.example.android.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
            galleryAddPic();
        }

    }


    private File createImageFile()throws IOException{
        //creating an image file name
        String timeStamp = new SimpleDateFormat("yyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName, //prefix
                ".jpg", // suffix
                storageDir //directory
        );
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void galleryAddPic(){
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent
        );
    }

}
