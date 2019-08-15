package com.example.backingapp;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.backingapp.Fragments.MenuRecipesFragment;

public class MenuActivity extends AppCompatActivity {

    //Initializer
    private int mId;
    private String mName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        MenuRecipesFragment menuRecipesFragment = new MenuRecipesFragment();

        //Create Intent
        Intent intent = getIntent();

        //Getting values parsed by the main activity
        if (intent != null){
            if (intent.hasExtra("id")){
                mId = intent.getIntExtra("id",1);
                mName = intent.getStringExtra("name");
                menuRecipesFragment.setmRecipeId(mId);
                menuRecipesFragment.setmRecipeName(mName);

                getSupportFragmentManager().beginTransaction().replace(R.id.menu_fragment,
                        menuRecipesFragment).commit();


            }

            Log.d("RECIPE1", Integer.toString(mId));

        }


    }


}
