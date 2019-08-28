package com.example.backingapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Creating a model or object of Ingredient
 */

public class Ingredient implements Parcelable {

    //Initializer
    private int mId;
    private String mIngredient;
    private String mQuantity;
    private String mMeasure;

    /**
     * Constructor for the Ingredient object
     * @param id
     * @param ingredient
     * @param quantity
     * @param measure
     */
    public Ingredient(int id, String ingredient, String quantity, String measure){
        mId = id;
        mIngredient = ingredient;
        mQuantity = quantity;
        mMeasure = measure;
    }

    protected Ingredient(Parcel in) {
        mId = in.readInt();
        mIngredient = in.readString();
        mQuantity = in.readString();
        mMeasure = in.readString();
    }


    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

    //Creatng Getters
    public int getmId() {
        return mId;
    }

    public String getmIngredient() {
        return mIngredient;
    }

    public String getmQuantity() {
        return mQuantity;
    }

    public String getmMeasure() {
        return mMeasure;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mIngredient);
        dest.writeString(mQuantity);
        dest.writeString(mMeasure);
    }
}
