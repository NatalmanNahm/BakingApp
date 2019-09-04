package com.example.backingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.backingapp.Database.AppDatabase;
import com.example.backingapp.Database.AppExecutors;
import com.example.backingapp.JsonUtils.NetworkUtils;
import com.example.backingapp.Model.Recipe;
import com.example.backingapp.R;

import java.util.ArrayList;
import java.util.List;

public class GridwidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new GridRemoteViewsFactory(this.getApplicationContext());
    }

}

class GridRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory{

    Context mContext;
    List<Recipe> mRecipeArray = new ArrayList<>();
    private AppDatabase mDb;
    public GridRemoteViewsFactory(Context applicationContext) {
        mContext = applicationContext;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        mDb = AppDatabase.getInstance(mContext);
        mRecipeArray = mDb.recipesDao().GetAllFavRecipe();
    }

    @Override
    public void onDestroy() {
        mRecipeArray.clear();
    }

    @Override
    public int getCount() {
        if (null == mRecipeArray) return 0;
        return mRecipeArray.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {

        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.backing_widget);
        Recipe recipe = mRecipeArray.get(position);
        int recipeId = recipe.getId();
        String name = recipe.getName();


        views.setImageViewResource(R.id.widget_icon, R.drawable.cake_widget);
        views.setTextViewText(R.id.widget_Ing_name, name);

        Bundle extras = new Bundle();
        extras.putInt("id", recipeId);
        extras.putString("RecipeName", name);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        views.setOnClickFillInIntent(R.id.widget_icon, fillInIntent);

        return views;

    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

}
