package com.example.backingapp.JsonUtils;

import android.text.TextUtils;
import android.util.Log;

import com.example.backingapp.ImageIdGenerator;
import com.example.backingapp.Model.Recipe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Where we get our data from json file
 */

public class JsonUtils {

    private static final String TAG = JsonUtils.class.getSimpleName();

    public static ArrayList<Recipe> extractRecipeFromJson(String json){

        //If the string Json is empty the return early
        if (TextUtils.isEmpty(json)){
            return null;
        }

        //Creating an empty ArrayList of Recipe that will hold our recipe json Data
        ArrayList<Recipe> recipes = new ArrayList<>();

        //Try to parse the Json and if the is a problem the JSONException will be Thrown.
        //It will be then catch in the catch block, so the app doesn't crash
        try {
            //Build Recipe data with the data correspondent to what we need
            JSONArray rootJson = new JSONArray(json);

            //Iterate through the array to get the data we want
            for (int i = 0; i<rootJson.length(); i++){
                JSONObject jsonObject = rootJson.getJSONObject(i);

                int id = jsonObject.getInt("id");
                String name = jsonObject.getString("name");
                int image = ImageIdGenerator.imageId(id);
                int servings =  jsonObject.getInt("servings");

                recipes.add(new Recipe(id, name, image, servings));
            }


        } catch (JSONException e) {
            //If there is a problem parsing the Json object print this message
            Log.e(TAG, "Error parsing the Movie Json object");
        }
        //return a list of Recipe Data
        return recipes;
    }

}
