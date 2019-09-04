package com.example.backingapp;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;

@RunWith(AndroidJUnit4.class)
public class MainActivityScreenTest {

    public static final String BUTTON_INGREDIENT_TEXT = "BROWNIES INGREDIENTS";

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);


    /**
     * Click on item position 1 to see if it opens the MenuActivity with the right data
     */
    @Test
    public void clickRecipeItem_OpenMenuActivity(){
        //Perform click on the position 1 of the recyclerview
        onView(withId(R.id.recipe_recyclerView)).perform(actionOnItemAtPosition(1, click()));

        //Check if the MenuActivity opens up with the right data
        onView(withId(R.id.ingredients_button)).check(matches(withText(BUTTON_INGREDIENT_TEXT)));
    }
}
