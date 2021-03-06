package com.example.backingapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.backingapp.Fragments.IngredientFragment;
import com.example.backingapp.Fragments.StepFragment;
import com.example.backingapp.Fragments.VideoFragment;
import com.squareup.picasso.Picasso;

public class BakingActivity extends AppCompatActivity{

    private int mRecipeId;
    private String mRecipeName;
    private String mDescription;
    private String mVideoLink;
    private String mThumbnail;
    private int mId;
    private int mImage;
    private int[] mIdArray;
    private String[] mDescArray;
    private String[] mLinkArray;
    private String[] mThumbnailArray;
    private RelativeLayout mRelativeLayout;

    private FloatingActionButton nextButton;
    private FloatingActionButton previousButton;
    private Toolbar mToolbar;

    private FrameLayout mFramelayou1;
    private FrameLayout mFramelayout2;
    private FrameLayout mFrameLayoutIngredient;
    private ImageView mImageView;

    private IngredientFragment mIngredientFragment;
    private StepFragment mStepFragment;
    private VideoFragment mVideoFragment;

    private static final String VIDEO_FRAG = "Video Fragment";
    private static final String INGREDIENT_FRAG = "Ingredient Fragment";
    private static final String STEP_FRAGMENT = "Step Fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backing);

        //ToolBar configuration
        mToolbar = (Toolbar) findViewById(R.id.baking_toolbar);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();

        mRelativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        previousButton = (FloatingActionButton) findViewById(R.id.previous_Button);
        nextButton = (FloatingActionButton) findViewById(R.id.nextButton);
        mFramelayou1 = (FrameLayout) findViewById(R.id.frameLayout1);
        mFramelayout2 = (FrameLayout) findViewById(R.id.frameLayout2);
        mFrameLayoutIngredient = (FrameLayout) findViewById(R.id.frameLayout_Ingredient);
        mImageView = (ImageView) findViewById(R.id.justThumb);


        //If nothing is in the saveInstance bundle then create a new instance of the fragments
        //else just use the saveed ones.
        if (savedInstanceState == null ){
            mStepFragment = new StepFragment();
            mVideoFragment = new VideoFragment();
            mIngredientFragment = new IngredientFragment();
        } else {
            mStepFragment = (StepFragment) getSupportFragmentManager()
                    .getFragment(savedInstanceState, STEP_FRAGMENT);

            mIngredientFragment = (IngredientFragment) getSupportFragmentManager()
                    .getFragment(savedInstanceState, INGREDIENT_FRAG);

            mVideoFragment = (VideoFragment) getSupportFragmentManager()
                    .getFragment(savedInstanceState, VIDEO_FRAG);
        }


        //Create Intent
        Intent intent = getIntent();

        //Getting values parsed by the main activity
        if (intent != null) {
            if (intent.hasExtra("id")) {

                mRecipeId = intent.getIntExtra("id", 1);
                mRecipeName = intent.getStringExtra("RecipeName");
                mVideoLink = "";

                mToolbar.setVisibility(View.VISIBLE);
                mFrameLayoutIngredient.setVisibility(View.VISIBLE);
                mImageView.setVisibility(View.GONE);
                actionBar.setTitle(mRecipeName + " " + getString(R.string.ingredients_button_text));
                mRelativeLayout.setVisibility(View.GONE);
                mFramelayou1.setVisibility(View.GONE);
                mFramelayout2.setVisibility(View.GONE);

                startIngredientFragment(mIngredientFragment, mRecipeId);
                startDescFragment(mStepFragment, mDescription);
                startVideoFragment(mVideoFragment, mVideoLink);


            } else if (intent.hasExtra("desc")){

                //Getting data parse to the activity
                mRecipeName = intent.getStringExtra("RecipeName");

                if(savedInstanceState != null){
                    mId = savedInstanceState.getInt("StepId");
                    mVideoLink = savedInstanceState.getString("video");
                } else {
                    mId = intent.getIntExtra("StepId", 0);
                    mVideoLink = intent.getStringExtra("video");
                }
                mRecipeId = intent.getIntExtra("idRecipe", 1);
                mImage = ImageIdGenerator.imageId(mRecipeId);
                mDescription = intent.getStringExtra("desc");
                mThumbnail = intent.getStringExtra("thumbnail");

                mIdArray = intent.getIntArrayExtra("idArray");
                mDescArray = intent.getStringArrayExtra("descArray");
                mLinkArray = intent.getStringArrayExtra("linkArray");
                mThumbnailArray = intent.getStringArrayExtra("thumbnailArray");

                if (mId == 0){
                    actionBar.setTitle(mRecipeName + " " + getString(R.string.step_intro));
                } else {
                    actionBar.setTitle(mRecipeName + " " +
                            getString(R.string.step_title) + " " + (mId + 1));
                }

                //Landscape setting
                if (findViewById(R.id.landscape) != null){
                    displayOnLandscape(mStepFragment, mVideoFragment, mIngredientFragment);
                }
                //Portrait Setting
                else {
                    displayOnPotrait(mStepFragment, mVideoFragment, mIngredientFragment);
                }

                hideNextButton(mId);
                hidePreviousButton(mId);

            }

            actionBar.setDisplayHomeAsUpEnabled(true);

        }
    }

    /**
     * Method ensures that we Go to the next step with
     * the right data when button next is clicked
     * @param view
     */
    public void nextStep(View view) {

        mVideoFragment = new VideoFragment();
        mStepFragment = new StepFragment();
        for (int i = 0; i < mIdArray.length-1; i++){
            if (mIdArray[i] == mId){

                int next = i + 1;
                Log.d("NEXT", Integer.toString(next));
                //Setting the next data
                mId = mIdArray[next];
                mVideoLink = mLinkArray[next];
                mDescription = mDescArray[next];
                mThumbnail = mThumbnailArray[next];

                break;
            }
        }

        displayOnPotrait(mStepFragment, mVideoFragment, mIngredientFragment);

        hideNextButton(mId);
        hidePreviousButton(mId);
    }

    /**
     * Method that ensures that we go to the previous
     * step when the button is clicked with the right data
     * @param view
     */
    public void previousStep(View view) {

        mVideoFragment = new VideoFragment();
        mStepFragment = new StepFragment();
        for (int i = 0; i<mIdArray.length; i++){
            if (mIdArray[i] == mId){

                //Making sure we do not go to previous when there is no more
                //data to go back to;
                if (i == 0){
                    return;
                }

                int previous = i - 1;
                Log.d("NEXT", Integer.toString(previous));
                mId = mIdArray[previous];
                mVideoLink = mLinkArray[previous];
                mDescription = mDescArray[previous];
                mThumbnail = mThumbnailArray[previous];

                break;

            }
        }

        displayOnPotrait(mStepFragment, mVideoFragment, mIngredientFragment);

        hideNextButton(mId);
        hidePreviousButton(mId);
    }

    /**
     * Helper method to inflate the VideoFragment with its data
     * @param videoFragment
     * @param video
     */
    private void startVideoFragment(VideoFragment videoFragment, String video){
        videoFragment.setmVideoLink(video);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout1, videoFragment)
                .commit();
    }

    /**
     * Helper method to inflate StepFragment with its data
     * @param stepFragment
     * @param description
     */
    private void startDescFragment(StepFragment stepFragment, String description){
        stepFragment.setmDescription(description);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout2, stepFragment)
                .commit();
    }

    /**
     * helper method to inflate Ingredient Fragment with its data
     * @param ingredientFragment
     * @param id
     */
    private void startIngredientFragment(IngredientFragment ingredientFragment, int id){
        ingredientFragment.setmRecipeId(id);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout_Ingredient, ingredientFragment)
                .commit();
    }

    /**
     * Make sure the button next only shows when needed
     * @param id
     */
    @SuppressLint("RestrictedApi")
    private void hideNextButton(int id){
        if (id == mIdArray.length-1){
            nextButton.setVisibility(View.INVISIBLE);
        } else {
            nextButton.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Make sure the button Previous only shows when needed
     * @param id
     */
    @SuppressLint("RestrictedApi")
    private void hidePreviousButton(int id){
        if (id == 0){
            previousButton.setVisibility(View.INVISIBLE);
        } else {
            previousButton.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Helper method to display data on a landscape setting
     * @param stepFragment
     * @param videoFragment
     */
    private void displayOnLandscape(StepFragment stepFragment, VideoFragment videoFragment, IngredientFragment ingredientFragment){
        //Hide toolbar
        mToolbar.setVisibility(View.GONE);
        //hide the Next and Previous Button too
        startIngredientFragment(ingredientFragment, mRecipeId);
        mFrameLayoutIngredient.setVisibility(View.GONE);
        mRelativeLayout.setVisibility(View.GONE);
        mImageView.setVisibility(View.GONE);

        //Start Fragments
        startDescFragment(stepFragment, mDescription);
        startVideoFragment(videoFragment, mVideoLink);
        //change to next Video if there is one
        if (mVideoLink != null && !mVideoLink.isEmpty()){
            mFramelayou1.setVisibility(View.VISIBLE);
            mFramelayout2.setVisibility(View.GONE);

        } else {
            mFramelayout2.setVisibility(View.VISIBLE);
            mFramelayou1.setVisibility(View.GONE);
        }
        Log.d("DESC", mDescription);
    }

    /**
     * Helper method to display data on a Portrait setting
     * @param stepFragment
     * @param videoFragment
     */
    private void displayOnPotrait(StepFragment stepFragment, VideoFragment videoFragment, IngredientFragment ingredientFragment){
        //Showing Toolbar
        mToolbar.setVisibility(View.VISIBLE);
        //Show the nex and previous button
        mRelativeLayout.setVisibility(View.VISIBLE);
        startIngredientFragment(ingredientFragment, mRecipeId);
        mFrameLayoutIngredient.setVisibility(View.GONE);
        //Start Video Fragment
        startVideoFragment(videoFragment, mVideoLink);

        //change to next Video if there is one
        if (mVideoLink != null && !mVideoLink.isEmpty()) {
            mFramelayou1.setVisibility(View.VISIBLE);
            mImageView.setVisibility(View.GONE);

        } else if(mThumbnail != null && !mThumbnail.isEmpty()){
            mFramelayou1.setVisibility(View.GONE);
            mImageView.setVisibility(View.VISIBLE);
            Picasso.get()
                    .load(mThumbnail)
                    .placeholder(mImage)
                    .error(mImage)
                    .into(mImageView);
        } else {
            mFramelayou1.setVisibility(View.GONE);
            mImageView.setVisibility(View.VISIBLE);
            Picasso.get()
                    .load(mImage)
                    .placeholder(mImage)
                    .error(mImage)
                    .into(mImageView);
        }

        mFramelayout2.setVisibility(View.VISIBLE);
        //change to next description
        startDescFragment(stepFragment, mDescription);
        Log.d("DESC", mDescription);
    }


    /**
     * Help to get back to MenuActivity since we are using Fragment to populate it
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                super.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        getSupportFragmentManager().putFragment(outState, INGREDIENT_FRAG, mIngredientFragment);
        getSupportFragmentManager().putFragment(outState, STEP_FRAGMENT, mStepFragment);
        getSupportFragmentManager().putFragment(outState, VIDEO_FRAG, mVideoFragment);
        outState.putInt("StepId", mId);
        outState.putString("video", mVideoLink);
    }
}
