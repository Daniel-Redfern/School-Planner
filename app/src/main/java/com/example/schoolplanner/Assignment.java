package com.example.schoolplanner;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import java.util.Calendar;
import java.util.Date;

public class Assignment implements Parcelable{
        //TODO: now when checking for the photo in the future we check if its no photo provided or the photo path
        private Calendar dateDue;
        private String dateDueString;
        private String assignmentName;
        private String description;
        private String currentPhotoPath;

    public Assignment(String dateDue,  String itemName, String itemDescription, String picPath){
        //date, month, year
        dateDueString = dateDue;
        String [] dates = dateDue.split("/");
        Integer day = new Integer(dates[0]);
        Integer month = new Integer(dates[1]);
        Integer year = new Integer(dates[2]);
        Calendar calendarDay = Calendar.getInstance();
        calendarDay.set(year, month, day);
        this.dateDue = calendarDay;
        assignmentName = itemName;
        description = itemDescription;
        currentPhotoPath = picPath;
    }


    protected Assignment(Parcel in) {
        dateDueString = in.readString();
        String [] dates = dateDueString.split("/");
        Integer day = new Integer(dates[0]);
        Integer month = new Integer(dates[1]);
        Integer year = new Integer(dates[2]);
        Calendar calendarDay = Calendar.getInstance();
        calendarDay.set(year, month, day);
        this.dateDue = calendarDay;
        assignmentName = in.readString();
        description = in.readString();
        currentPhotoPath = in.readString();
    }

    public static final Creator<Assignment> CREATOR = new Creator<Assignment>() {
        @Override
        public Assignment createFromParcel(Parcel in) {
            return new Assignment(in);
        }

        @Override
        public Assignment[] newArray(int size) {
            return new Assignment[size];
        }
    };

    public String getDescription() {return description;}

    public String getDateDueString(){
            return dateDueString;
        }

    public String getCurrentPhotoPath(){return currentPhotoPath;}

    /**
     * checks if we have a photo path if it does we return it, if not we return null;
     * @return
     */
    public String getPhotoPathIfExists(){
        if(!currentPhotoPath.equals("No Photo Provided")){return currentPhotoPath;}
        else{return null;}
    }

    public String getAssignmentName() {
        return assignmentName;
    }

    protected Date getDate(){
            return dateDue.getTime();
        }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(dateDueString);
        parcel.writeString(assignmentName);
        parcel.writeString(description);
        parcel.writeString(currentPhotoPath);
    }

    @Override
    public boolean equals(Object o) {
        if(o.getClass().equals(Assignment.class)){
            Assignment other = (Assignment)o;
            if(other.getAssignmentName().equals(assignmentName)){
                if(other.getDateDueString().equals(dateDueString)){
                    if(other.getDescription().equals(description)){
                        if(other.getCurrentPhotoPath().equals(currentPhotoPath)){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}