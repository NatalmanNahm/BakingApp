package com.example.backingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.backingapp.Fragments.IngredientFragment;
import com.example.backingapp.Fragments.StepFragment;
import com.example.backingapp.Fragments.VideoFragment;

public class BakingActivity extends AppCompatActivity {

    private int mRecipeId;
    private String mDescription;
    private String mVideoLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backing);

        //Get an instance of the Fragment needed
        IngredientFragment ingredientFragment = new IngredientFragment();
        StepFragment stepFragment = new StepFragment();
        VideoFragment videoFragment = new VideoFragment();

        //Create Intent
        Intent intent = getIntent();

        //Getting values parsed by the main activity
        if (intent != null) {
            if (intent.hasExtra("id")) {
                mRecipeId = intent.getIntExtra("id", 1);
                ingredientFragment.setmRecipeId(mRecipeId);

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameLayout1, ingredientFragment)
                        .commit();
            } else if (intent.hasExtra("desc")){
                mDescription = intent.getStringExtra("desc");
                mVideoLink = intent.getStringExtra("video");
                videoFragment.setmVideoLink(mVideoLink);
                stepFragment.setmStepDescription(mDescription);

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameLayout1, videoFragment)
                        .commit();

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameLayout2, stepFragment)
                        .commit();

            }

        }
    }

}
