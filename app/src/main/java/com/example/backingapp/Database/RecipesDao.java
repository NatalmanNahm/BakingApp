package com.example.backingapp.Database;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.backingapp.Model.Recipe;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface RecipesDao {

    //For the LiveData
    @Query("SELECT * FROM RecipesTable ORDER BY id")
    LiveData<List<Recipe>> loadAllRecipeData();

    //To get all data saved
    @Query("SELECT * FROM Recipestable ORDER BY id")
    List<Recipe> GetAllFavRecipe();

    @Query("SELECT id FROM RecipesTable WHERE id = :id")
    int idSaved(int id);

    @Insert
    void addFavRecipe(Recipe favRecipe);

    @Delete
    void deletefavRecipe (Recipe deleteRecipe);

    @Query("SELECT * FROM RecipesTable WHERE id = :id")
    Recipe toBeDelete(int id);

    @Query("SELECT * FROM RecipesTable LIMIT 1")
    Recipe getAnyRecipe();

}
