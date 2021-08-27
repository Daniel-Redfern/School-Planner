package com.example.schoolplanner;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import java.net.CookieHandler;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainPage extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    ArrayList<Course> courses = new ArrayList<>();
    ArrayList<Assignment> assignments = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        Button menuButton = findViewById(R.id.btn_show);
        courses = Helper.getCourses(MainPage.this);
        assignments = Helper.getAssignmentsFromCourses(courses);
        Helper.updateAssignmentsScroll(MainPage.this, assignments, courses);
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            showPopup(findViewById(R.id.btn_show));
            }
        });



    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch(item.getItemId()){
            case R.id.item1:
                setUpOverLayout(courses, false);
                return true;

            case R.id.item2:
                setUpOverLayout(courses, true);
                return true;

            case R.id.item3:
                return true;

            case R.id.sort_by_date_due:
                Toast.makeText(this, "Sorting By Date Due", Toast.LENGTH_LONG).show();
                sortByDate();
                return true;

            case R.id.sort_by_period:
                Toast.makeText(this, "Sorting By Course", Toast.LENGTH_LONG).show();
                sortByCourse();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.example_menu);
        popup.show();
    }

    private void sortByCourse(){
        for(int i = 0; i < courses.size(); i++){
            Collections.sort(courses.get(i).getAssignments(), new Comparator<Assignment>() {
                @Override
                public int compare(Assignment a1, Assignment a2) {
                    return (a1.getDate()).compareTo(a2.getDate());
                }
            });
        }
        assignments = Helper.getAssignmentsFromCourses(courses);
        Helper.updateAssignmentsScroll(MainPage.this, assignments, courses);
    }

    private void sortByDate(){
        Collections.sort(assignments, new Comparator<Assignment>() {
            @Override
            public int compare(Assignment a1, Assignment a2) {
                return a1.getDate().compareTo(a2.getDate());
            }
        });
        Helper.updateAssignmentsScroll(MainPage.this, assignments, courses);
    }

    private void startAddAssignmentWithCourse(Course c){
        Intent intent = new Intent(MainPage.this, AddAssignment.class);
        intent.putExtra("data", c);
        startActivity(intent);
    }

    private void startViewCourseWithCourse(Course c){
        Intent intent = new Intent(MainPage.this, ViewCourse.class);
        intent.putExtra("data", c);
        startActivity(intent);
    }


    private void setUpOverLayout(ArrayList<Course> courses, boolean use){
        /** if use is true its for add assignment, if its false its for view course**/
        final boolean a = use;
        RelativeLayout mainLayout = findViewById(R.id.main_page_back_layout);
        final Button bb = findViewById(R.id.btn_show);
        final ArrayList<Course> coursesList = courses;
        ViewGroup.LayoutParams paramsMatchParent = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        final ScrollView scrollView = new ScrollView(getApplicationContext());
        scrollView.setLayoutParams(paramsMatchParent);
        LinearLayout coverLayout = new LinearLayout(getApplicationContext());
        coverLayout.setLayoutParams(paramsMatchParent);
        Drawable backgroundImage = getResources().getDrawable(R.drawable.opaque_background_black);
        scrollView.setBackground(backgroundImage);
        coverLayout.setOrientation(LinearLayout.VERTICAL);
        bb.setVisibility(View.GONE);

        /**basically we have 2 cases where we need to display all of our courses so we have it do the same with the exception of where the buttons go to**/
        for(int i = 0; i < courses.size(); i++){
            final int j = i;
            Button b = Helper.createButton(courses.get(i).getCourseName(), MainPage.this);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(a){
                    startAddAssignmentWithCourse(coursesList.get(j));
                    }else{
                    startViewCourseWithCourse(coursesList.get(j));
                    }
                }
                });
            coverLayout.addView(b);
        }
        if(!a){
            /**If Were Viewing Courses We Want To Give Them An Option To Add A Course**/
            Button addButton = Helper.createButton("Add Course", MainPage.this);
            addButton.setTextColor(Color.parseColor("#FFFF00"));
            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(MainPage.this, CreateCourse.class));
                }});
            coverLayout.addView(addButton);
        }
        /**We Also Want To Give Them A Back Button In Case They Hit The Back Arrow And Go To The Page Where The Menu Is Still Up**/
        Button c = Helper.createButton("Back", MainPage.this);
        c.setTextColor(Color.parseColor("#FF0000"));
        final ArrayList<Course> courses1 = courses;
        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollView.setVisibility(View.GONE);
                Helper.updateAssignmentsScroll(MainPage.this, assignments, courses1);
                bb.setVisibility(View.VISIBLE);

            }
        });
        coverLayout.addView(c);
        /**now that the buttons are added into the inner linear layout we add it to the scroll and the scroll to the main page**/
        scrollView.addView(coverLayout);
        mainLayout.addView(scrollView);
    }
}