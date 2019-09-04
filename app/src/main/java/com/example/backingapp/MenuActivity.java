package com.example.backingapp;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.backingapp.Adapters.MenuRecipeAdapter;
import com.example.backingapp.Adapters.RecipeAdapter;
import com.example.backingapp.Fragments.IngredientFragment;
import com.example.backingapp.Fragments.MenuRecipesFragment;
import com.example.backingapp.Fragments.VideoFragment;

public class MenuActivity extends AppCompatActivity {

    //Initializer
    private int mId;
    private String mName;
    private Toolbar mMenuToolBar;
    private int mImage;
    private int mServings;
    private boolean isFav;

    private Toolbar mStepToolbar;
    private boolean isTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //Setting Toolbar configuration
        mMenuToolBar = (Toolbar) findViewById(R.id.menu_toolbar);
        setSupportActionBar(mMenuToolBar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //Instance of the MenuRecipeFragment
        MenuRecipesFragment menuRecipesFragment = new MenuRecipesFragment();

        //Create Intent
        Intent intent = getIntent();

        //Getting values parsed by the main activity
        if (intent != null){
            if (intent.hasExtra("id")){
                mId = intent.getIntExtra("id",1);
                mName = intent.getStringExtra("name");
                mImage = intent.getIntExtra("image", 0);
                mServings = intent.getIntExtra("servings", 0);
                isFav = intent.getBooleanExtra("isFav", false);

            }

        }

        actionBar.setTitle(mName);

        if (findViewById(R.id.doublePane_menu) != null){

            //This mean it is two pane
            isTwoPane = true;

            mStepToolbar = (Toolbar) findViewById(R.id.baking_toolbar);
            mStepToolbar.setTitle(mName + " " + getString(R.string.step_toolbar_title));

        } else {
            isTwoPane = false;
        }

        menuRecipesFragment.setmRecipeId(mId);
        menuRecipesFragment.setmRecipeName(mName);
        menuRecipesFragment.setmImage(mImage);
        menuRecipesFragment.setmServings(mServings);
        menuRecipesFragment.setFav(isFav);
        menuRecipesFragment.setmIsTwoPane(isTwoPane);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.menu_fragment, menuRecipesFragment)
                .commit();

    }
}
