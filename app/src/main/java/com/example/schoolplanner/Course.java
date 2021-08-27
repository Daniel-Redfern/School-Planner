package com.example.schoolplanner;
import android.os.Parcel;
import android.os.Parcelable;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Course implements Parcelable {
    private ArrayList<Assignment> assignments = new ArrayList<Assignment>();
    String courseName;
    String instructorName;

    public Course(String courseName, String instructorName){
        this.courseName = courseName; this.instructorName = instructorName;
    }

    protected Course(Parcel in) {
        courseName = in.readString();
        instructorName = in.readString();
        in.readTypedList(assignments, Assignment.CREATOR);
    }

    public static final Creator<Course> CREATOR = new Creator<Course>() {
        @Override
        public Course createFromParcel(Parcel in) {
            return new Course(in);
        }

        @Override
        public Course[] newArray(int size) {
            return new Course[size];
        }
    };

    public String getCourseName() {
        return courseName;
    }

    public ArrayList<Assignment> getAssignments(){return assignments;}

    public String getInstructorName(){return instructorName;}

    public void addAssignment(Assignment assignment){
        assignments.add(assignment);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(courseName);
        parcel.writeString(instructorName);
        parcel.writeTypedList(assignments);

    }
}