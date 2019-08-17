package com.example.backingapp;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.backingapp.Adapters.RecipeAdapter;
import com.example.backingapp.Fragments.MenuRecipesFragment;
import com.example.backingapp.JsonUtils.NetworkUtils;
import com.example.backingapp.Model.Recipe;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements RecipeAdapter.RecipeAdapterOnClickHandler{


    //Initializer
    private RecyclerView mRecyclerView;
    private RecipeAdapter mRecipeAdapter;
    private ArrayList<Recipe> mRecipes = new ArrayList<>();

    private GridLayoutManager mGridLayoutManager;
    private Parcelable mSavedGridLayoutManager;

    private static String KEY_INSTANCE_SAVED_POSITION = "recipe_position";

    private TextView mErrorMessage;
    private ProgressBar mLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Getting reference of the recyclerView so we can set adapter to it
        mRecyclerView = (RecyclerView) findViewById(R.id.recipe_recyclerView);

        //used to display the the error message and hide it when needed
        mErrorMessage = (TextView) findViewById(R.id.error_message);

        //creating a progress bar that let the user know that there data is been loaded
        mLoading = (ProgressBar) findViewById(R.id.loading_circle);

        //Creating gridView where we add the recipes
        mGridLayoutManager =
                new GridLayoutManager(this, calculateNoOfColumns(this),
                        GridLayoutManager.VERTICAL, false);

        //Set GridView to the Reyclerview
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        //Create Adapter
        mRecipeAdapter = new RecipeAdapter(getApplicationContext(), mRecipes,this );
        //Set Adapter to the RecyclerView
        mRecyclerView.setAdapter(mRecipeAdapter);

        if (savedInstanceState != null){
            mSavedGridLayoutManager = savedInstanceState.getParcelable(KEY_INSTANCE_SAVED_POSITION);
            mGridLayoutManager.onRestoreInstanceState(mSavedGridLayoutManager);
        }
        loadRecipeData();

    }

    /**
     * helper method to Load data we get from Json data
     */
    private void loadRecipeData() {
        showRecipeDataView();
        new Fetchrecipes().execute();

    }

    /**
     * helper method to hide the error message
     * and show Recipes data when it is available
     */
    private void showRecipeDataView() {
        /* First, make sure the error is invisible */
        mErrorMessage.setVisibility(View.INVISIBLE);
        /* Then, make sure the Recipes data is visible */
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * Helper method to show error message
     * and hide recycle view when Recipes data is not available
     */
    private void showErrorMessage() {
        /* First, hide the currently visible data */
        mRecyclerView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mErrorMessage.setVisibility(View.VISIBLE);
    }

    public class Fetchrecipes extends AsyncTask<String, Void, ArrayList<Recipe>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoading.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<Recipe> doInBackground(String... strings) {
            //Get the Recipes data and populate it to the user
            mRecipes = NetworkUtils.fetchRecipeData();
            return mRecipes;
        }

        @Override
        protected void onPostExecute(ArrayList<Recipe> recipes) {
            //Hiding the progress bar
            mLoading.setVisibility(View.INVISIBLE);
            if (recipes != null && !recipes.isEmpty()){
                //Displaying the Recipe Card
                showRecipeDataView();
                mRecipeAdapter.setRecipeData(recipes);
                mGridLayoutManager.onRestoreInstanceState(mSavedGridLayoutManager);
            }else {
                showErrorMessage();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadRecipeData();
    }

    @Override
    public void onCLick(int id, String name) {
        Context context = this;
        Class destinationClass = MenuActivity.class;

        //Creating Intent
        Intent intent =  new Intent(context, destinationClass);
        //Parsing id to the MenuActivity
        intent.putExtra("id", id);
        intent.putExtra("name", name);

        startActivity(intent);
    }

    /**
     * Helper method to get the number of column to be displayed in the GridView
     * @param context
     * @return noOfColumns
     */
    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int scalingFactor = 1000;
        int noOfColumns = (int) (dpWidth / scalingFactor);
        if(noOfColumns < 1){
            noOfColumns = 1;
        }

        return noOfColumns;
    }

    /**
     * getting back to where we left overriding the onSaveInstanceState
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(KEY_INSTANCE_SAVED_POSITION, mGridLayoutManager.onSaveInstanceState());

    }
}
