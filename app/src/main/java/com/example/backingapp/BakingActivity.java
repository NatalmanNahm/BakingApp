package com.example.backingapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.example.backingapp.Fragments.IngredientFragment;
import com.example.backingapp.Fragments.StepFragment;
import com.example.backingapp.Fragments.VideoFragment;

public class BakingActivity extends AppCompatActivity {

    private int mRecipeId;
    private String mDescription;
    private String mVideoLink;
    private int mId;
    private int[] mIdArray;
    private String[] mDescArray;
    private String[] mLinkArray;
    private RelativeLayout mRelativeLayout;
    FloatingActionButton nextButton;
    FloatingActionButton previousButton;
    Toolbar mToolbar;
    private FrameLayout mFramelayou1;
    private FrameLayout mFramelayout2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backing);

        //Get an instance of the Fragment needed
        IngredientFragment ingredientFragment = new IngredientFragment();
        StepFragment stepFragment = new StepFragment();
        VideoFragment videoFragment = new VideoFragment();

        mRelativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        previousButton = (FloatingActionButton) findViewById(R.id.previous_Button);
        nextButton = (FloatingActionButton) findViewById(R.id.nextButton);
        mToolbar = (Toolbar) findViewById(R.id.baking_toolbar);
        mFramelayou1 = (FrameLayout) findViewById(R.id.frameLayout1);
        mFramelayout2 = (FrameLayout) findViewById(R.id.frameLayout2);

        //Create Intent
        Intent intent = getIntent();
        getSupportActionBar().hide();

        //Create Ingredients when the widget is clicked
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int id = extras.getInt("WidRecipeId");
            ingredientFragment.setmRecipeId(id);

            Log.d("WIDGET", Integer.toString(id));

            mRelativeLayout.setVisibility(View.GONE);
            mFramelayou1.setVisibility(View.VISIBLE);
            mFramelayout2.setVisibility(View.GONE);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameLayout1, ingredientFragment)
                    .commit();
        }

        //Getting values parsed by the main activity
        if (intent != null) {
            if (intent.hasExtra("id")) {
                mRecipeId = intent.getIntExtra("id", 1);
                ingredientFragment.setmRecipeId(mRecipeId);

                mRelativeLayout.setVisibility(View.GONE);

                mFramelayou1.setVisibility(View.VISIBLE);
                mFramelayout2.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameLayout1, ingredientFragment)
                        .commit();
            } else if (intent.hasExtra("desc")){

                //Getting data parse to the activity
                mId = intent.getIntExtra("StepId", 0);
                mDescription = intent.getStringExtra("desc");
                mVideoLink = intent.getStringExtra("video");
                mIdArray = intent.getIntArrayExtra("idArray");
                mDescArray = intent.getStringArrayExtra("descArray");
                mLinkArray = intent.getStringArrayExtra("linkArray");

                //Landscape setting
                if (findViewById(R.id.landscape) != null){
                    displayOnLandscape(stepFragment, videoFragment);

                }
                //Portrait Setting
                else {
                    dissplayOnPotrait(stepFragment, videoFragment);
                }

                hideNextButton(mId);
                hidePreviousButton(mId);


            }

        }
    }


    public static Intent createLaunchIngredientFragment(Context context, int recipeId){

        Intent intent = new Intent(context, BakingActivity.class);
        IngredientFragment ingredientFragment = new IngredientFragment();
        ingredientFragment.setmRecipeId(recipeId);

        return intent;
    }

    /**
     * Method ensures that we Go to the next step with
     * the right data when button next is clicked
     * @param view
     */
    public void nextStep(View view) {

        VideoFragment videoFragment = new VideoFragment();
        StepFragment stepFragment = new StepFragment();
        for (int i = 0; i < mIdArray.length-1; i++){
            if (mIdArray[i] == mId){

                int next = i + 1;
                Log.d("NEXT", Integer.toString(next));
                //Setting the next data
                mId = mIdArray[next];
                mVideoLink = mLinkArray[next];
                mDescription = mDescArray[next];

                break;
            }
        }

        //Landscape setting
        if (findViewById(R.id.landscape) != null){
            displayOnLandscape(stepFragment, videoFragment);

        }
        //Portrait Setting
        else {
           dissplayOnPotrait(stepFragment, videoFragment);
        }

        hideNextButton(mId);
        hidePreviousButton(mId);
    }

    /**
     * Method that ensures that we go to the previous
     * step when the button is clicked with the right data
     * @param view
     */
    public void previousStep(View view) {

        VideoFragment videoFragment = new VideoFragment();
        StepFragment stepFragment = new StepFragment();
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

                break;

            }
        }

        //Landscape setting
        if (findViewById(R.id.landscape) != null){
            displayOnLandscape(stepFragment, videoFragment);

        }
        //Portrait Setting
        else {
            dissplayOnPotrait(stepFragment, videoFragment);
        }

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
        stepFragment.setmStepDescription(description);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout2, stepFragment)
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
    private void displayOnLandscape(StepFragment stepFragment, VideoFragment videoFragment){
        //Hide toolbar
        mToolbar.setVisibility(View.GONE);
        //hide the Next and Previous Button too
        mRelativeLayout.setVisibility(View.GONE);
        //change to next Video if there is one
        if (mVideoLink != null && !mVideoLink.isEmpty()){
            mFramelayou1.setVisibility(View.VISIBLE);
            startVideoFragment(videoFragment, mVideoLink);

            mFramelayout2.setVisibility(View.GONE);

        } else {
            mFramelayout2.setVisibility(View.VISIBLE);
            mFramelayou1.setVisibility(View.GONE);
            //change to next description
            startDescFragment(stepFragment, mDescription);
        }
    }

    /**
     * Helper method to display data on a Portrait setting
     * @param stepFragment
     * @param videoFragment
     */
    private void dissplayOnPotrait(StepFragment stepFragment, VideoFragment videoFragment){
        //Showing Toolbar
        mToolbar.setVisibility(View.VISIBLE);
        //Show the nex and previous button
        mRelativeLayout.setVisibility(View.VISIBLE);
        //change to next Video if there is one
        if (mVideoLink != null && !mVideoLink.isEmpty()){
            mFramelayou1.setVisibility(View.VISIBLE);
            startVideoFragment(videoFragment, mVideoLink);
        } else {
            mFramelayou1.setVisibility(View.GONE);
        }

        mFramelayout2.setVisibility(View.VISIBLE);
        //change to next description
        startDescFragment(stepFragment, mDescription);
    }
}
