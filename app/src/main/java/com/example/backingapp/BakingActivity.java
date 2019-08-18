package com.example.backingapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

        //Create Intent
        Intent intent = getIntent();

        //Getting values parsed by the main activity
        if (intent != null) {
            if (intent.hasExtra("id")) {
                mRecipeId = intent.getIntExtra("id", 1);
                ingredientFragment.setmRecipeId(mRecipeId);

                mRelativeLayout.setVisibility(View.GONE);

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

                mRelativeLayout.setVisibility(View.VISIBLE);

                //change to next Video if there is one
                if (mVideoLink != null && !mVideoLink.isEmpty()){
                    startVideoFragment(videoFragment, mVideoLink);
                }

                //change to next description
                startDescFragment(stepFragment, mDescription);

                hideNextButton(mId);
                hidePreviousButton(mId);

            }

        }
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

                //change to next Video if there is one
                if (mVideoLink != null && !mVideoLink.isEmpty()){
                    startVideoFragment(videoFragment, mVideoLink);
                } else {
                    getSupportFragmentManager().beginTransaction()
                            .detach(getSupportFragmentManager().findFragmentById(R.id.frameLayout1))
                            .commit();
                }

                //change to next description
                startDescFragment(stepFragment, mDescription);

                hideNextButton(mId);
                hidePreviousButton(mId);

            }
        }
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

                //change to next Video if there is one
                if (mVideoLink != null && !mVideoLink.isEmpty()){
                    startVideoFragment(videoFragment, mVideoLink);
                } else {
                    getSupportFragmentManager().beginTransaction()
                            .detach(getSupportFragmentManager().findFragmentById(R.id.frameLayout1))
                            .commit();
                }

                //change to next description
                startDescFragment(stepFragment, mDescription);

                hideNextButton(mId);
                hidePreviousButton(mId);

            }
        }
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


}
