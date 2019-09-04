package com.example.backingapp.Model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/**
 * Recipes of model or object
 */
@Entity(tableName = "RecipesTable")
public class Recipe implements Parcelable {
    //Initializer
    @PrimaryKey
    @NonNull
    private int id;
    private String name;
    private int image;
    private int servings;
    private boolean isFav;

    /**
     * Constructor for the Recipe object
     * @param id
     * @param name
     * @param image
     */
    public Recipe (int id, String name, int image, int servings, boolean isFav){
        this.id = id;
        this.name = name;
        this.image = image;
        this.servings = servings;
        this.isFav = isFav;
    }

    //Creating Parcel to be read from
    private Recipe (Parcel parcel){
        id = parcel.readInt();
        name = parcel.readString();
        image = parcel.readInt();
        servings = parcel.readInt();
    }
    @Override
    public int describeContents() {
        return 0;
    }

    //Writing to the parcel
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeInt(image);
        dest.writeInt(servings);
    }

    /**
     * Creating a parcel to be used to in case we need to use it on saveInstance
     */
    @Ignore
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

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getImage() {
        return image;
    }

    public int getServings() {
        return servings;
    }

    public boolean isFav() {
        return isFav;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public void setFav(boolean fav) {
        isFav = fav;
    }
}
