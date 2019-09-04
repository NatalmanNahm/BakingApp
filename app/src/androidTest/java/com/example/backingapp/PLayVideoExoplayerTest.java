package com.example.backingapp;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class PLayVideoExoplayerTest {

    @Rule
    public ActivityTestRule<MainActivity> mMainACtivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    private IdlingResource mIdlingResource;

    // Registers any resource that needs to be synchronized with Espresso before the test is run.
    @Before
    public void registerIdlingResources(){
        mIdlingResource = mMainACtivityTestRule.getActivity().getIdlingResource();

        // To prove that the test fails, omit this call:
        Espresso.registerIdlingResources(mIdlingResource);
    }

    @Test
    public void voidPlayTestVideo(){
        //Perform click on the position 1 of the recyclerview
        onView(withId(R.id.recipe_recyclerView)).perform(actionOnItemAtPosition(1, click()));

        //Click on item at position 0
        onView(withId(R.id.menu_recyclerView)).perform(actionOnItemAtPosition(0, click()));

        //Try to play Video
        onView(withId(R.id.video_exo_player)).perform(click());
    }

    // Remember to unregister resources when not needed to avoid malfunction.
    @After
    public void unRegisterIdlingResources(){
        if (mIdlingResource != null){
            Espresso.unregisterIdlingResources(mIdlingResource);
        }
    }

}
