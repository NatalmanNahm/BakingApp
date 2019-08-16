package com.example.backingapp.Model;

/**
 * Creating a model or object of Ingredient
 */

public class Ingredient {

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
}
