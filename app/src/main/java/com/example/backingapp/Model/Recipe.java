package com.example.backingapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Recipes of model or object
 */

public class Recipe implements Parcelable {
    //Initializer
    private int mId;
    private String mName;
    private int mImage;
    private int mServings;

    /**
     * Constructor for the Recipe object
     * @param id
     * @param name
     * @param image
     */
    public Recipe (int id, String name, int image, int servings){
        mId = id;
        mName = name;
        mImage = image;
        mServings = servings;
    }

    //Creating Parcel to be read from
    private Recipe (Parcel parcel){
        mId = parcel.readInt();
        mName = parcel.readString();
        mImage = parcel.readInt();
        mServings = parcel.readInt();
    }
    @Override
    public int describeContents() {
        return 0;
    }

    //Writing to the parcel
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mName);
        dest.writeInt(mImage);
        dest.writeInt(mServings);
    }

    /**
     * Creating a parcel to be used to in case we need to use it on saveInstance
     */
    public final Parcelable.Creator<Recipe> CREATOR = new Parcelable.Creator<Recipe>(){

        @Override
        public Recipe createFromParcel(Parcel source) {
            return new Recipe(source);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[0];
        }
    };

    public int getmId() {
        return mId;
    }

    public String getmName() {
        return mName;
    }

    public int getmImage() {
        return mImage;
    }

    public int getmServings() {
        return mServings;
    }
}
