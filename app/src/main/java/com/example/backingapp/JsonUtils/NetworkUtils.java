package com.example.backingapp.JsonUtils;

import android.net.Uri;
import android.util.Log;

import com.example.backingapp.Model.MenuRecipes;
import com.example.backingapp.Model.Recipe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * These utilities will be used to read from the recipes link provided.
 */

public class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    //Base Url to be used to get the recipes
    private static final String RECIPES_URL = "https://d17h27t6h515a5.cloudfront.net";

    //Parameter to be used to query data from the Recipes links
    //Will be append onto the base URL
    private static String TOPHER = "topher";
    private static String YEAR = "2017";
    private static String MONTH = "May";
    private static String BAKING = "59121517_baking";
    private static String CLOSING_PARAM = "baking.json";

    /**
     * Build URL used to communicate with the recipes API
     * @return
     */
    public static URL buldUrl (){
        //Build the URL with the query parameter
        Uri uriBuilder = Uri.parse(RECIPES_URL).buildUpon()
                .appendPath(TOPHER)
                .appendPath(YEAR)
                .appendPath(MONTH)
                .appendPath(BAKING)
                .appendPath(CLOSING_PARAM)
                .build();

        URL url = null;

        try {
            url = new URL(uriBuilder.toString());

        } catch (MalformedURLException e) {
            Log.e(TAG, "URL malConstructed");
        }
        return url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {

        String jsonResponse = "";

        //if the url is null the return early
        if (url == null){
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        //Trying to get connection the server and if the status is 200
        //That means the connection was a success and then we can retrieve data needed
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /*milliseconds*/);
            urlConnection.setConnectTimeout(15000 /*milliseconds*/);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(TAG, "MakeHTTPRequest: Problem retrieving data from JSON result ", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream (InputStream inputStream) throws IOException{
        StringBuilder output = new StringBuilder();
        if (inputStream != null){
            InputStreamReader inputStreamReader =
                    new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();

            while (line != null){
                output.append(line);
                line = bufferedReader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Query the data and return a list of recipes data
     * @return
     */
    public static ArrayList<Recipe> fetchRecipeData(){
        //Create a Url Object
        URL url = buldUrl();

        //Perform HTTP request to the url and return JSON response back
        String jsonREsponse = null;

        try {
            jsonREsponse = getResponseFromHttpUrl(url);

        }catch (IOException e) {
            Log.e(TAG, "Problem making the HTTP request.", e);
        }

        //Extract data needed from the json response
        ArrayList<Recipe> recipes = JsonUtils.extractRecipeFromJson(jsonREsponse);

        return recipes;
    }

    /**
     * Query the data and return a list of MenuRecipe Data
     * @return
     */
    public static ArrayList<MenuRecipes> fetchMenuRecipeData(int id){
        //Create a Url Object
        URL url = buldUrl();

        //Perform HTTP request to the url and return JSON response back
        String jsonREsponse = null;

        try {
            jsonREsponse = getResponseFromHttpUrl(url);

        }catch (IOException e) {
            Log.e(TAG, "Problem making the HTTP request.", e);
        }

        //Extract data needed from the json response
        ArrayList<MenuRecipes> menuRecipes = JsonUtils.extractMenuRecipeJson(jsonREsponse, id);
        return menuRecipes;
    }

}
