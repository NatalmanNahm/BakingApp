package com.example.backingapp.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.backingapp.Adapters.MenuRecipeAdapter;
import com.example.backingapp.BakingActivity;
import com.example.backingapp.Database.AppDatabase;
import com.example.backingapp.Database.AppExecutors;
import com.example.backingapp.ImageIdGenerator;
import com.example.backingapp.JsonUtils.NetworkUtils;
import com.example.backingapp.Model.MenuRecipes;
import com.example.backingapp.Model.Recipe;
import com.example.backingapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class MenuRecipesFragment extends Fragment implements MenuRecipeAdapter.MenuAdapterOnCLickHandler, View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String SERVING_KEY = "servings";
    private static final String IMAGE_KEY = "image";
    private static final String FAV_KEY = "isFavorite";
    private static final String IS_TWO_PANE = "is Two Pane";

    private int mRecipeId;
    private String mRecipeName;
    private RecyclerView mRecyclerView;
    private MenuRecipeAdapter mMenuAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private ArrayList<MenuRecipes> mMenuRecipe = new ArrayList<>();
    private Button mIngredientButton;
    private TextView mErrorMessage;
    private View rootView;
    private AppDatabase mDb;
    private boolean isFav;
    private int mServings;
    private int mImage;

    private boolean mIsTwoPane;
    private VideoFragment mVideoFragment;
    private IngredientFragment mIngredientFragment;
    private StepFragment mStepFragment;
    private FrameLayout mFramelayout1;
    private FrameLayout mFramelayout2;
    private ImageView mImageView;


    @Override
    public void onClick(View v) {
        if (mIsTwoPane){

            mImageView.setVisibility(View.GONE);
            mFramelayout1.setVisibility(View.VISIBLE);
            mFramelayout2.setVisibility(View.GONE);

            mIngredientFragment.setmRecipeId(mRecipeId);
            getFragmentManager().beginTransaction()
                    .replace(R.id.frameLayout1, mIngredientFragment)
                    .commit();
        } else {
            //Create a bundle to be used to parse the id to the Ingredients Fragment
            Bundle bundle = new Bundle();
            bundle.putInt("id", mRecipeId);
            bundle.putString("RecipeName", mRecipeName);

            //Attach bundle to an intent
            final Intent ingredientIntent = new Intent(getContext(), BakingActivity.class);
            ingredientIntent.putExtras(bundle);

            startActivity(ingredientIntent);
        }

    }


    public MenuRecipesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null){
            mRecipeName = savedInstanceState.getString(ARG_PARAM2);
            mRecipeId = savedInstanceState.getInt(ARG_PARAM1);
            mImage = savedInstanceState.getInt(IMAGE_KEY);
            mServings = savedInstanceState.getInt(SERVING_KEY);
            isFav = savedInstanceState.getBoolean(FAV_KEY);
            mIsTwoPane = savedInstanceState.getBoolean(IS_TWO_PANE);
        }
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

        setRetainInstance(true);

        if (savedInstanceState != null){
            mRecipeName = savedInstanceState.getString(ARG_PARAM2);
            mRecipeId = savedInstanceState.getInt(ARG_PARAM1);
            mImage = savedInstanceState.getInt(IMAGE_KEY);
            mServings = savedInstanceState.getInt(SERVING_KEY);
            isFav = savedInstanceState.getBoolean(FAV_KEY);
            mIsTwoPane = savedInstanceState.getBoolean(IS_TWO_PANE);
        }

        this.setHasOptionsMenu(true);

        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_menu_recipes, container, false);

        //Initialize member variable for the data base
        mDb = AppDatabase.getInstance(getContext());

        //Getting reference of the recyclerView so we can set adapter to it
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.menu_recyclerView);

        //get the reference to the button
        mIngredientButton = (Button) rootView.findViewById(R.id.ingredients_button);

        mIngredientButton.setText(mRecipeName + " " + getString(R.string.ingredients_button_text));

        //Binding Framelayouts and ImageView to their views
        mFramelayout1 = (FrameLayout) getActivity().findViewById(R.id.frameLayout1);
        mFramelayout2 = (FrameLayout) getActivity().findViewById(R.id.frameLayout2);
        mImageView = (ImageView) getActivity().findViewById(R.id.justThumb);

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

        if (mIsTwoPane){
            //Get instance of the Ingredient fragment
            mIngredientFragment = new IngredientFragment();

            mImageView.setVisibility(View.GONE);
            mFramelayout1.setVisibility(View.VISIBLE);
            mFramelayout2.setVisibility(View.GONE);

            mIngredientFragment.setmRecipeId(mRecipeId);
            getFragmentManager().beginTransaction()
                    .replace(R.id.frameLayout1, mIngredientFragment)
                    .commit();

        }

            mIngredientButton.setOnClickListener(this);




        getActivity().supportInvalidateOptionsMenu();

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

    public void setmRecyclerView(RecyclerView mRecyclerView) {
        this.mRecyclerView = mRecyclerView;
    }

    public void setFav(boolean fav) {
        isFav = fav;
    }

    public void setmServings(int servings) {
        this.mServings = servings;
    }

    public void setmImage(int mImage) {
        this.mImage = mImage;
    }

    public void setmIsTwoPane(boolean mIsTwoPane) {
        this.mIsTwoPane = mIsTwoPane;
    }

    @Override
    public void onClick(int id, String description, String videoLink, String thumbnail) {

        if (mIsTwoPane){

            //get instance of fragments
            mVideoFragment = new VideoFragment();
            mStepFragment = new StepFragment();

            //if there is a video then show it
            if (videoLink != null && !videoLink.isEmpty()){

                mFramelayout1.setVisibility(View.VISIBLE);
                mImageView.setVisibility(View.GONE);
                mVideoFragment.setmVideoLink(videoLink);
                getFragmentManager().beginTransaction()
                        .replace(R.id.frameLayout1, mVideoFragment)
                        .commit();

                mFramelayout2.setVisibility(View.VISIBLE);
                mStepFragment.setmStepDescription(description);
                getFragmentManager().beginTransaction()
                        .replace(R.id.frameLayout2, mStepFragment)
                        .commit();

            //if the Thumbnail image is available then use that instead;
            } else if(thumbnail != null && !thumbnail.isEmpty()){

                mFramelayout1.setVisibility(View.GONE);
                mImageView.setVisibility(View.VISIBLE);
                Picasso.get()
                        .load(thumbnail)
                        .placeholder(mImage)
                        .error(mImage)
                        .into(mImageView);

                mFramelayout2.setVisibility(View.VISIBLE);
                mStepFragment.setmStepDescription(description);
                getFragmentManager().beginTransaction()
                        .replace(R.id.frameLayout2, mStepFragment)
                        .commit();

            //That means that there is no thumbnail then we use the default image instead
            } else {

                mFramelayout1.setVisibility(View.GONE);
                mImageView.setVisibility(View.VISIBLE);
                Picasso.get()
                        .load(mImage)
                        .placeholder(mImage)
                        .error(mImage)
                        .into(mImageView);

                mFramelayout2.setVisibility(View.VISIBLE);
                mStepFragment.setmStepDescription(description);
                getFragmentManager().beginTransaction()
                        .replace(R.id.frameLayout2, mStepFragment)
                        .commit();
            }




        } else {
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


    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(ARG_PARAM1, mRecipeId);
        outState.putString(ARG_PARAM2, mRecipeName);
        outState.putInt(IMAGE_KEY, mImage);
        outState.putInt(SERVING_KEY, mServings);
        outState.putBoolean(FAV_KEY, isFav);
        outState.putBoolean(IS_TWO_PANE, mIsTwoPane);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.fav_option:
                if (!isFav){
                    item.setIcon(R.drawable.ic_favorite_black_24dp);
                    isFav = true;

                    final Recipe recipe = new
                            Recipe(mRecipeId, mRecipeName, mImage, mServings, isFav);

                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            Recipe savedRecipe = mDb.recipesDao().toBeDelete(mRecipeId);
                            int savedId = mDb.recipesDao().idSaved(mRecipeId);

                            if (savedId == 0){
                                item.setIcon(R.drawable.ic_favorite_black_24dp);
                                mDb.recipesDao().addFavRecipe(recipe);
                            } else if (savedId == mRecipeId){
                                item.setIcon(R.drawable.ic_favorite_border_black_24dp);
                                mDb.recipesDao().deletefavRecipe(savedRecipe);
//                                getActivity().finish();
                            }else {
                                item.setIcon(R.drawable.ic_favorite_black_24dp);
                                mDb.recipesDao().addFavRecipe(recipe);
                            }
                        }
                    });
                } else {
                    item.setIcon(R.drawable.ic_favorite_border_black_24dp);
                    isFav = false;

                    AppExecutors.getInstance().diskIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            Recipe savedRecipe = mDb.recipesDao().toBeDelete(mRecipeId);
                            mDb.recipesDao().deletefavRecipe(savedRecipe);
//                            getActivity().finish();
                        }
                    });

                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fav_menu, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem favItem = menu.findItem(R.id.fav_option);

        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                int savedId = mDb.recipesDao().idSaved(mRecipeId);

                if (savedId == mRecipeId){
                    favItem.setIcon(R.drawable.ic_favorite_black_24dp);
                } else {
                    favItem.setIcon(R.drawable.ic_favorite_border_black_24dp);
                }
            }
        });
    }
}
