package com.example.backingapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.backingapp.Adapters.RecipeAdapter;
import com.example.backingapp.Database.AppDatabase;
import com.example.backingapp.Database.AppExecutors;
import com.example.backingapp.Database.MainViewModel;
import com.example.backingapp.IdlingResource.SimpleIdlingResource;
import com.example.backingapp.JsonUtils.NetworkUtils;
import com.example.backingapp.Model.Recipe;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecipeAdapter.RecipeAdapterOnClickHandler{


    //Initializer
    private RecyclerView mRecyclerView;
    private RecipeAdapter mRecipeAdapter;
    private List<Recipe> mRecipes = new ArrayList<>();

    private GridLayoutManager mGridLayoutManager;
    private Parcelable mSavedGridLayoutManager;

    private static String KEY_INSTANCE_SAVED_POSITION = "recipe_position";
    public static final String PREF_NAME = "ArrayListRecipe";

    private TextView mErrorMessage;
    private ProgressBar mLoading;
    private Toolbar mMainToolBar;

    private boolean isFav;
    private int mId;
    private String mName;
    private int mImage;
    private int mServings;
    private AppDatabase mDb;

    private MainViewModel mViewModel;

    // The Idling Resource which will be null in production.
    @Nullable
    private SimpleIdlingResource mIdlingResource;

    // ArrayList of Tea objects via the callback.
    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Setting things for actionBar
        mMainToolBar = (Toolbar) findViewById(R.id.recipe_toolbar);
        setSupportActionBar(mMainToolBar);

        //Getting reference of the recyclerView so we can set adapter to it
        mRecyclerView = (RecyclerView) findViewById(R.id.recipe_recyclerView);

        //used to display the the error message and hide it when needed
        mErrorMessage = (TextView) findViewById(R.id.error_message);

        //creating a progress bar that let the user know that there data is been loaded
        mLoading = (ProgressBar) findViewById(R.id.loading_circle);

        //Getting reference to the ImageButton

        //Creating gridView where we add the recipes
        if (findViewById(R.id.main_sw600) != null){
            mGridLayoutManager =
                    new GridLayoutManager(this, 2,
                            GridLayoutManager.HORIZONTAL, false);
        } else {
            mGridLayoutManager =
                    new GridLayoutManager(this, calculateNoOfColumns(this),
                            GridLayoutManager.VERTICAL, false);
        }
        //Initialize member variable for the data base
        mDb = AppDatabase.getInstance(getApplicationContext());
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        //Set GridView to the Reyclerview
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        //Create Adapter
        mRecipeAdapter = new RecipeAdapter(getApplicationContext(), mRecipes,this);
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

        setUpViewModel();
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


    public class Fetchrecipes extends AsyncTask<String, Void, List<Recipe>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoading.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Recipe> doInBackground(String... strings) {
            //Get the Recipes data and populate it to the user
            mRecipes = NetworkUtils.fetchRecipeData();
            return mRecipes;
        }

        @Override
        protected void onPostExecute(List<Recipe> recipes) {
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

    /**
     * This is to handle item clciked
     * whenever the user clicked on an item it opens up
     * its menu of recipes
     * @param id
     * @param name
     */
    @Override
    public void onCLick(int id, String name, int image, int servings, boolean isFav) {
        Context context = this;
        Class destinationClass = MenuActivity.class;

        //Creating Intent
        Intent intent =  new Intent(context, destinationClass);
        //Parsing id to the MenuActivity
        intent.putExtra("id", id);
        intent.putExtra("name", name);
        intent.putExtra("image", image);
        intent.putExtra("servings", servings);
        intent.putExtra("isFav", isFav);

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

    public static int calculateNoOfColumnsLandscape(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int scalingFactor = 200;
        int noOfColumns = (int) (dpWidth / scalingFactor);
        if(noOfColumns < 2){
            noOfColumns = 2;
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

    /**
     * Getting the data live whenever we get back to the mainActivity
     */
    private void setUpViewModel(){
        MainViewModel mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        mainViewModel.getmRecipe().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {
                mRecipeAdapter.setRecipeData(mRecipes);
            }
        });
    }
}
