package com.example.schoolplanner;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Helper {
    private static final String SAVE_FILE_NAMES_FILE = "filenamesgohere.txt";
    //This is a class that is made for all the helper methods i made over the time

    /**
     * creates a button with the text given
     * @param buttonText the text for the button
     * @param activity the activity this is done from
     * @return returns a button with the text given
     */
    protected static Button createButton(String buttonText, Activity activity){
        Button button = new Button(activity.getApplicationContext());
        button.setText(buttonText);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(20, 10, 20, 10);
        button.setLayoutParams(params);
        button.setGravity(Gravity.CENTER);
        button.setTextColor(Color.parseColor("#FFFDC7"));
        button.setBackgroundResource(R.drawable.custom_button_background);
        return button;
    }

    /**
     * creates a text view with text from a string
     * @param text the text for the text view
     * @param activity the activity this is being done from
     * @return a text view with the text from the given
     */
    protected static TextView createTextView(String text, Activity activity){
        TextView tv = new TextView(activity.getApplicationContext());
        tv.setText(text);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(20, 5, 20, 5);
        tv.setLayoutParams(params);
        tv.setGravity(Gravity.CENTER);
        Typeface tf = ResourcesCompat.getFont(activity, R.font.fancyfont);
        tv.setTypeface(tf);
        return tv;
    }

    /**
     * updates the main page scroll view with the current assignments
     * @param activity the activity this is being done from (main page)
     * @param assignments an arraylist of all the assignments
     * @param courses an arraylist of all the courses
     */
    protected static void updateAssignmentsScroll(Activity activity, ArrayList<Assignment> assignments, ArrayList<Course> courses){
        if(assignments.size()!=0){
            ScrollView scrollView = activity.findViewById(R.id.scroll_view_work_items);
            scrollView.removeAllViews();
            LinearLayout mainLayout = new LinearLayout(activity.getApplicationContext());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0,0, 0, 150);
            mainLayout.setLayoutParams(lp);
            mainLayout.setOrientation(LinearLayout.VERTICAL);
            final ArrayList<Course> cl = courses;
            final Activity a1 = activity;
            for(int i = 0; i < assignments.size(); i++){
                final Assignment currentAssignment = assignments.get(i);
                Button button = createButton(currentAssignment.getAssignmentName(), activity);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(a1, ViewAssignment.class);
                        // make sure these line up on the other side
                        intent.putExtra("courses", cl);
                        intent.putExtra("assignment", currentAssignment);
                        a1.startActivity(intent);
                    }});
                mainLayout.addView(button);
            }
            scrollView.addView(mainLayout);
        }
        updateTopTextView(activity, assignments);
    }

    /**
     * updates the top text view from main page
     * @param activity the activity this is being done from (main page)
     * @param assignments an arraylist of all the assignments
     */
    protected static void updateTopTextView(Activity activity, ArrayList<Assignment> assignments){
        TextView topTextView = activity.findViewById(R.id.top_text_view_Current_Work_Items);
        if(assignments.size()==0){
            topTextView.setText("No Assignments Due");
        }
        else if(assignments.size()==1){
            topTextView.setText("1 Assignment Due");
        }
        else{
            topTextView.setText(assignments.size()+ " Assignments Due");
        }
    }

    /**
     * turns a course into an encoded string
     * @param c course to be turned into a string
     * @return encoded string from course
     */
    private static String courseToString(Course c){
        String courseString = "";
        courseString += c.getCourseName() + "~" + c.getInstructorName() + "~";
        ArrayList<Assignment> assignments = c.getAssignments();
        for(int i = 0; i < assignments.size(); i++){
            courseString+= assignmentToString(assignments.get(i)) + "~";
        }
        return  courseString;
        //Order of string - name~instructor~courseNo~Assignments
    }

    /**
     * turns an assignment into an encoded string
     * @param a assignment to be turned into a string
     * @return encoded string from assignment
     */
    private static String assignmentToString(Assignment a){
        String retStr = "";
        retStr += a.getDateDueString() + "`" + a.getAssignmentName() + "`" + a.getDescription() + "`" + a.getCurrentPhotoPath();
        //order of string: date, name, disc, then photo path if applicable
        return retStr;
    }

    /**
     * turns an encoded string into an assignment
     * @param s encoded string
     * @return assignment from encoded string
     */
    private static Assignment stringToAssignment(String s){
        String [] cParts = s.split("`");
        Assignment a = new Assignment(cParts[0], cParts[1], cParts[2], cParts[3]);
        return a;
    }

    /**
     * turns a encoded string into a course
     * @param s encoded string
     * @return course from encoded string
     */
    private static Course stringToCourse(String s){
        String [] cParts = s.split("~");
        Course c = new Course(cParts[0], cParts[1]);
        for(int i = 0; i < cParts.length -2; i++){
            c.addAssignment(stringToAssignment(cParts[i+2]));
        }
        return c;
    }

    /**
     * reads a string of data from a specified file
     * @param fileName the name of the file the data is being read from
     * @param activity the current activity the method is being called from
     * @return the string of data from the file
     */
    private static String readDataFromFile(String fileName, Activity activity){
        FileInputStream fis = null;
        String text = "";
        try {
            //opening the file up and preparing to read it out
            fis = activity.openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            //building the string back up from the file
            while((text = br.readLine())!=null){
                sb.append(text).append("");
            }
            text = sb.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullPointerException e){
                e.printStackTrace();
            }
        }
    return text;
    }

    /**
     * writes a string of data to a specified file
     * @param data the data as a string
     * @param fileName name of file the data is being saved to
     * @param activity the current activity this is being done from
     */
    private static void writeDataToFile(String data, String fileName, Activity activity){
        FileOutputStream fos = null;
        try {
            //starting to open up the file to save data
            fos = activity.openFileOutput(fileName, activity.MODE_PRIVATE);
            fos.write(data.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            if(fos != null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * sets up the a file for a course
     * @param c the course were setting up the file from
     * @param activity the activity this is being done from
     */
    protected static void setupCourseFileFile(Course c, Activity activity){
        //taking the course name and making it a file name
        String courseFileName = getFileNameFromCourse(c);
        //creating an empty file for the course
        writeDataToFile("", courseFileName, activity);
        String dataSoFar = readDataFromFile(SAVE_FILE_NAMES_FILE, activity);
        //putting that file name into the course file
        writeDataToFile(dataSoFar + courseFileName + "=", SAVE_FILE_NAMES_FILE, activity);
        saveCourse(c, activity);
    }

    /**
     * gets an arraylist of all the courses from saved data
     * @param activity the activity this is being done from
     * @return an arraylist of all the courses
     */
    protected static ArrayList<Course> getCourses(Activity activity){
        ArrayList<Course> courses = new ArrayList<Course>();
        String courseFileStrings = readDataFromFile(SAVE_FILE_NAMES_FILE, activity);
        if(!courseFileStrings.equals("")) {
            String [] fileNames = courseFileStrings.split("=");
            for (int i = 0; i < fileNames.length; i++) {
                String data = readDataFromFile(fileNames[i], activity);
                Course c = stringToCourse(data);
                courses.add(c);
            }
        }
        return courses;
    }

    /**
     * checks if there is any saved data
     * @param activity activity were currently in
     * @return returns true if there is saved data, if not it returns false
     */
    protected static boolean checkForSavedData(Activity activity) {
        boolean retBool = false;
        FileInputStream fis = null;
        try {
            //opening the file up and preparing to read it out
            fis = activity.openFileInput(SAVE_FILE_NAMES_FILE);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            //building the string back up from the file
            if((br.readLine())!=null){
                retBool = true;
            }
            else{
                retBool = false;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullPointerException e){
                e.printStackTrace();
            }
        }
        return retBool;
    }

    /**
     * saves the data of a course
     * @param c the course being saved
     * @param activity the activity this is being done from
     */
    protected static void saveCourse(Course c, Activity activity){
        String data = courseToString(c);
        writeDataToFile(data, getFileNameFromCourse(c), activity);
    }

    /**
     * remove the course from saved data
     * @param c the course we want to remove
     * @param activity activity this is being done in
     */
    //TODO: remove file/ actually delete file
    protected static void removeCourseFromData(Course c, Activity activity){
        String fileName = getFileNameFromCourse(c);
        String fileNamesString = readDataFromFile(SAVE_FILE_NAMES_FILE, activity);
        //I'm gonna be completely honest I don't know if these next two lines do anything
        File file = new File(fileName);
        file.delete();
        String [] parts = fileNamesString.split("=");
        String newStr = "";
        for(int i = 0; i < parts.length; i++){
            if(!parts[i].equals(fileName)){
                newStr += parts[i] + "=";
            }
        }
        writeDataToFile(newStr, SAVE_FILE_NAMES_FILE, activity);
    }

    /**
     * removes an assignment from a course
     * @param courses all of the courses because we need to find which course its in, then remove it
     * @param assignment assignment were looking to remove
     * @param activity the activity this is being done from since we need it
     */
    protected static void removeAssignmentFromData(ArrayList<Course> courses, Assignment assignment, Activity activity){
        for(int i = 0; i < courses.size(); i++){
            String s = assignment.getPhotoPathIfExists();
            if(s!=null){
                File f = new File(assignment.getCurrentPhotoPath());
                f.delete();
            }
            ArrayList<Assignment> assignments = courses.get(i).getAssignments();
            Boolean a = assignments.remove(assignment);
            if(a){
                Helper.saveCourse(courses.get(i), activity);
                return;
            }
        }
    }

    /**
     * gets the name of the file the data for the course is saved in
     * @param c course that were getting the name of the file from
     * @return the file name for the course
     */
    private static String getFileNameFromCourse(Course c){
        String fileName = c.getCourseName() + ".txt";
        return fileName;
    }

    /**
     * creates an arraylist of all the assignments from all of the courses
     * @param c arraylist of all the courses
     * @return an arraylist of all the assignments
     */
    protected static ArrayList<Assignment> getAssignmentsFromCourses(ArrayList<Course> c){
        ArrayList<Assignment> retList = new ArrayList<>();
        for(int i = 0; i < c.size(); i++){
            ArrayList<Assignment> assignments = c.get(i).getAssignments();
            for(int j = 0; j < assignments.size(); j++){
                retList.add(assignments.get(j));
            }
        }
        return retList;
    }

    /**
     * clears all saved data used for testing and debugging
     * @param activity any activity, it just needs this to write and read from files
     */
    protected static void clearSavedData(Activity activity){
        writeDataToFile("", SAVE_FILE_NAMES_FILE, activity);
    }
}


