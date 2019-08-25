package com.example.backingapp.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.view.menu.MenuAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.backingapp.Adapters.MenuRecipeAdapter;
import com.example.backingapp.BakingActivity;
import com.example.backingapp.JsonUtils.NetworkUtils;
import com.example.backingapp.MainActivity;
import com.example.backingapp.MenuActivity;
import com.example.backingapp.Model.MenuRecipes;
import com.example.backingapp.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class MenuRecipesFragment extends Fragment implements MenuRecipeAdapter.MenuAdapterOnCLickHandler, View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private int mRecipeId;
    private String mRecipeName;
    private RecyclerView mRecyclerView;
    private MenuRecipeAdapter mMenuAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private ArrayList<MenuRecipes> mMenuRecipe = new ArrayList<>();
    private Button mIngredientButton;
    private TextView mErrorMessage;
    private View rootView;



    @Override
    public void onClick(View v) {

        //Create a bundle to be used to parse the id to the Ingredients Fragment
        Bundle bundle = new Bundle();
        bundle.putInt("id", mRecipeId);
        bundle.putString("RecipeName", mRecipeName);

        //Attach bundle to an intent
        final Intent ingredientIntent = new Intent(getContext(), BakingActivity.class);
        ingredientIntent.putExtras(bundle);

        startActivity(ingredientIntent);
    }


    public MenuRecipesFragment() {
        // Required empty public constructor
    }

    /**
     * Inflate layout with data we get back from the async task
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (savedInstanceState != null){
            mRecipeName = savedInstanceState.getString(ARG_PARAM2);
            mRecipeId = savedInstanceState.getInt(ARG_PARAM1);
        }

        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_menu_recipes, container, false);


        //Getting reference of the recyclerView so we can set adapter to it
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.menu_recyclerView);

        //get the reference to the button
        mIngredientButton = (Button) rootView.findViewById(R.id.ingredients_button);

        mIngredientButton.setText(mRecipeName + " Ingredients");

        //used to display the the error message and hide it when needed
        mErrorMessage = (TextView) rootView.findViewById(R.id.error_message);

        //Creating the Linear Layout Manager.
        mLinearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mMenuAdapter = new MenuRecipeAdapter(getContext(), mMenuRecipe, this);
        mRecyclerView.setAdapter(mMenuAdapter);

        new FetchMenuRecipeTask().execute();

        mIngredientButton = rootView.findViewById(R.id.ingredients_button);
        mIngredientButton.setOnClickListener(this);

        //Return RootView
        return rootView;
    }

    /**
     * helper method to hide the error message
     * and show Menu data when it is available
     */
    private void showMenuDataView() {
        /* First, make sure the error is invisible */
        mErrorMessage.setVisibility(View.INVISIBLE);
        /* Then, make sure the menu data is visible */
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * Helper method to show error message
     * and hide recycle view when Menu data is not available
     */
    private void showErrorMessage() {
        /* First, hide the currently visible data */
        mRecyclerView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mErrorMessage.setVisibility(View.VISIBLE);
    }

    //AsyncTack for to get Menu Data
    public class FetchMenuRecipeTask extends AsyncTask<String, Void, ArrayList<MenuRecipes>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<MenuRecipes> doInBackground(String... strings) {
            //Get the Recipes data and populate it to the user
            mMenuRecipe = NetworkUtils.fetchMenuRecipeData(mRecipeId);
            return mMenuRecipe;
        }

        @Override
        protected void onPostExecute(ArrayList<MenuRecipes> menuRecipes) {
            if (menuRecipes != null && !menuRecipes.isEmpty()){
                //Show the menu Card
                showMenuDataView();
                mMenuAdapter.setMenuRecipeData(menuRecipes);
            } else {
                showErrorMessage();
            }
        }
    }

    public void setmRecipeId(int mRecipeId) {
        this.mRecipeId = mRecipeId;
    }

    public void setmRecipeName(String mRecipeName) {
        this.mRecipeName = mRecipeName;
    }

    @Override
    public void onClick(int id, String description, String videoLink, String thumbnail) {

        //Getting Context and Targeted Activity
        Context context = getContext();
        Class destinationClass = BakingActivity.class;

        //Initialize all array to parse to the Baking Activity
        int[] idArray = new int[mMenuRecipe.size()];
        String[] descArray = new String[mMenuRecipe.size()];
        String[] linkArray = new String[mMenuRecipe.size()];
        String[] thumbnailArray = new String[mMenuRecipe.size()];

        //Iterate through the ArrayList and put their respective data
        //in their Array
        for (int i = 0; i<mMenuRecipe.size(); i++){
            MenuRecipes menuRecipes = mMenuRecipe.get(i);

            idArray[i] = menuRecipes.getmStepId();
            descArray[i] = menuRecipes.getmStepDesc();
            linkArray[i] = menuRecipes.getmVideoUrl();
            thumbnailArray[i] = menuRecipes.getmThumbnail();

        }
        //Creating a bundle that to be used to send data to the Baking activity
        Bundle bundle = new Bundle();
        bundle.putInt("StepId", id);
        bundle.putString("desc", description);
        bundle.putString("video", videoLink);
        bundle.putString("thumbnail", thumbnail);
        bundle.putString("RecipeName",mRecipeName);
        bundle.putInt("idRecipe", mRecipeId);
        bundle.putIntArray("idArray", idArray);
        bundle.putStringArray("descArray", descArray);
        bundle.putStringArray("linkArray", linkArray);
        bundle.putStringArray("thumbnailArray", thumbnailArray);

        //Creating Intent and attach it to an Activity
        Intent intent =  new Intent(context, destinationClass);
        //parsing all data to the activity
        intent.putExtras(bundle);
        startActivity(intent);

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(ARG_PARAM1, mRecipeId);
        outState.putString(ARG_PARAM2, mRecipeName);

        super.onSaveInstanceState(outState);
    }
}
