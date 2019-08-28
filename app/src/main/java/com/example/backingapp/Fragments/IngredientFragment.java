package com.example.backingapp.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.backingapp.Adapters.IngredientAdapter;
import com.example.backingapp.JsonUtils.NetworkUtils;
import com.example.backingapp.Model.Ingredient;
import com.example.backingapp.R;

import java.util.ArrayList;


public class IngredientFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters
    private static final String ARG_PARAM1 = "param1";
    private int mRecipeId;
    private View rootView;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private TextView mErrorMessage;
    private IngredientAdapter mIngredientAdapter;
    private ArrayList<Ingredient> mIngredient;
    private static final String INGREDIENTS_ARRAYLIST_LIST = "Ingredients";
    private Parcelable mSavedLinearlayoutLayoutManager;


    public IngredientFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (savedInstanceState != null){
            mRecipeId = savedInstanceState.getInt(ARG_PARAM1);
        }

        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_ingredient, container, false);

        //Getting reference of the recyclerView so we can set adapter to it
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.ingredients_recyclerView);

        //used to display the the error message and hide it when needed
        mErrorMessage = (TextView) rootView.findViewById(R.id.error_message);

        //Creating the Linear Layout Manager.
        mLinearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mIngredientAdapter = new IngredientAdapter(getContext(), mIngredient);
        mRecyclerView.setAdapter(mIngredientAdapter);

        if (savedInstanceState != null){
            mSavedLinearlayoutLayoutManager = savedInstanceState.getParcelable(INGREDIENTS_ARRAYLIST_LIST);
            mLinearLayoutManager.onRestoreInstanceState(mSavedLinearlayoutLayoutManager);
        }

        new FetchIngredientTask().execute();

        return rootView;
    }

    private void showIngredientDataView() {
        /* First, make sure the error is invisible */
        mErrorMessage.setVisibility(View.INVISIBLE);
        /* Then, make sure the ingredients data is visible */
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showIngredientErrorMessage() {
        /* First, hide the currently visible data */
        mRecyclerView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mErrorMessage.setVisibility(View.VISIBLE);
    }

    //AsyncTack for to get Ingredient Data
    public class FetchIngredientTask extends AsyncTask<String, Void, ArrayList<Ingredient>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<Ingredient> doInBackground(String... strings) {
            //Fetch Ingredient data
            mIngredient = NetworkUtils.fetchIngredientData(mRecipeId);
            return mIngredient;
        }

        @Override
        protected void onPostExecute(ArrayList<Ingredient> ingredients) {
            if (ingredients!= null && !ingredients.isEmpty()){
                //Show Ingredients
                showIngredientDataView();
                mIngredientAdapter.setmIngredient(ingredients);
                mLinearLayoutManager.onRestoreInstanceState(mSavedLinearlayoutLayoutManager);
            } else {
                showIngredientErrorMessage();
            }
        }
    }

    public void setmRecipeId(int mRecipeId) {
        this.mRecipeId = mRecipeId;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(ARG_PARAM1, mRecipeId);
        outState.putParcelable(INGREDIENTS_ARRAYLIST_LIST, mLinearLayoutManager.onSaveInstanceState());

        super.onSaveInstanceState(outState);
    }
}
