package com.capstone.transit.trans_it;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Thomas on 4/22/2015.
 */
public class Step implements Parcelable{

    private String mDistance;
    private String mDuration;
    private String mHtmlInstructions;

    public String getDistance() {
        return mDistance;
    }

    public void setDistance(String distance) {
        mDistance = distance;
    }

    public String getDuration() {
        return mDuration;
    }

    public void setDuration(String duration) {
        mDuration = duration;
    }

    public String getHtmlInstructions() {
        return mHtmlInstructions;
    }

    public void setHtmlInstructions(String htmlInstructions) {
        mHtmlInstructions = htmlInstructions;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(mDistance);
        dest.writeString(mHtmlInstructions);
        dest.writeString(mDuration);
    }

    private Step(Parcel in){

        mDistance = in.readString();
        mHtmlInstructions = in.readString();
        mDuration = in.readString();
    }

    public Step(){}

    public static final Creator<Step> CREATOR = new Creator<Step>() {
        @Override
        public Step createFromParcel(Parcel source) {
            return new Step(source);
        }

        @Override
        public Step[] newArray(int size) {
            return new Step[size];
        }
    };
}
