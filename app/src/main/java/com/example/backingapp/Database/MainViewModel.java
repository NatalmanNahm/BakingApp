package com.example.backingapp.Database;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.backingapp.Model.Recipe;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainViewModel extends AndroidViewModel {

    //Constant for logging
    private static final String TAG = MainViewModel.class.getSimpleName();
    private ExecutorService mExecutorService;
    private RecipesDao mRecipeDao;
    LiveData<List<Recipe>> mRecipe;

    public MainViewModel(@NonNull Application application) {
        super(application);

        //Using the loadAllFavRecipes to initialize the Movie Variable
        AppDatabase appDatabase = AppDatabase.getInstance(this.getApplication());
        Log.d(TAG, "Actively retrieving the Recipes from the DataBase");

        mRecipe = appDatabase.recipesDao().loadAllRecipeData();
        mRecipeDao = AppDatabase.getInstance(application).recipesDao();
        mExecutorService = Executors.newSingleThreadExecutor();
    }

    //Creating a getter
    public LiveData<List<Recipe>> getmRecipe() {
        return mRecipe;
    }

    public void saveFavoriteRecipe(Recipe recipe) {
        mExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                mRecipeDao.addFavRecipe(recipe);
            }
        });
    }

    public void deleteFavRecipe (Recipe recipe){
        mExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                mRecipeDao.deletefavRecipe(recipe);
            }
        });
    }
}
