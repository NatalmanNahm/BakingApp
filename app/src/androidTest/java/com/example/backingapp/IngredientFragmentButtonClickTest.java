package com.example.backingapp;

import android.support.annotation.NonNull;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.backingapp.Fragments.IngredientFragment;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItem;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.core.internal.deps.dagger.internal.Preconditions.checkNotNull;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class IngredientFragmentButtonClickTest {

    private static final String MEASURE = "G";

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    /**
     * open up bakingActivity with some data by performing a click on the MenuActivity recyclerview
     */
    @Before
    public void clickStepItem_OpenMenuActivity(){
        //Perform click on the position 1 of the recyclerview
        onView(withId(R.id.recipe_recyclerView)).perform(actionOnItemAtPosition(1, click()));

    }

    public static Matcher<View> atPosition(final int position, @NonNull final Matcher<View> itemMatcher) {
        checkNotNull(itemMatcher);
        return new BoundedMatcher<View, RecyclerView>(RecyclerView.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("has item at position " + position + ": ");
                itemMatcher.describeTo(description);
            }

            @Override
            protected boolean matchesSafely(final RecyclerView view) {
                RecyclerView.ViewHolder viewHolder = view.findViewHolderForAdapterPosition(position);
                if (viewHolder == null) {
                    // has no item on such position
                    return false;
                }
                return itemMatcher.matches(viewHolder.itemView);
            }
        };
    }


    @Test
    public void clickIngredientButton_OpenIngredients(){
        //click on Ingredient Button
        onView(withId(R.id.ingredients_button)).perform(click());

//        IngredientFragment ingredientFragment = new IngredientFragment();
//        mActivityTestRule.getActivity().getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.frameLayout1, ingredientFragment)
//                .commit();

        //See if the text matches with the text at that position
        onView(withId(R.id.ingredients_recyclerView)).check(matches(atPosition(0, hasDescendant(withText(MEASURE)))));
    }
}
