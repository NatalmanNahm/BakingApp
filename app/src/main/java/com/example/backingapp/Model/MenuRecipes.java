package com.example.backingapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Menu Object or model
 */

public class MenuRecipes implements Parcelable {

    //Initializer
    private int mStepId;
    private String mStepNumber;
    private String mStepName;
    private String mStepDesc;
    private String mVideoUrl;
    private String mThumbnail;

    /**
     * Constructor for the MenuRecipes
     * @param stepName
     * @param stepNumber
     */
    public MenuRecipes(int stepId, String stepName, String stepNumber, String stepDesc, String videoUrl, String thumbnail){
        mStepId = stepId;
        mStepName = stepName;
        mStepNumber = stepNumber;
        mStepDesc = stepDesc;
        mVideoUrl = videoUrl;
        mThumbnail = thumbnail;
    }

    //Creating Parcel to be read from
    private MenuRecipes (Parcel parcel){
        mStepId = parcel.readInt();
        mStepName = parcel.readString();
        mStepNumber = parcel.readString();
        mStepDesc = parcel.readString();
        mVideoUrl = parcel.readString();
        mThumbnail = parcel.readString();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    //Writing to the parcel
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mStepId);
        dest.writeString(mStepName);
        dest.writeString(mStepNumber);
        dest.writeString(mStepDesc);
        dest.writeString(mVideoUrl);
        dest.writeString(mThumbnail);
    }

    /**
     * Creating a parcel to be used to in case we need to use it on saveInstance
     */
    public static final Creator<MenuRecipes> CREATOR = new Creator<MenuRecipes>() {
        @Override
        public MenuRecipes createFromParcel(Parcel in) {
            return new MenuRecipes(in);
        }

        @Override
        public MenuRecipes[] newArray(int size) {
            return new MenuRecipes[0];
        }
    };

    public int getmStepId() {
        return mStepId;
    }

    public String getmStepNumber() {
        return mStepNumber;
    }

    public String getmStepName() {
        return mStepName;
    }

    public String getmStepDesc() {
        return mStepDesc;
    }

    public String getmVideoUrl() {
        return mVideoUrl;
    }

    public String getmThumbnail() {
        return mThumbnail;
    }
}
