package com.example.backingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.backingapp.Fragments.IngredientFragment;

public class BakingActivity extends AppCompatActivity {

    private int mRecipeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backing);

        //Get an instance of the IngredientFragment
        IngredientFragment ingredientFragment = new IngredientFragment();

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
            }

        }
    }

}
