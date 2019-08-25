package com.example.backingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.backingapp.ImageIdGenerator;
import com.example.backingapp.JsonUtils.NetworkUtils;
import com.example.backingapp.Model.Recipe;
import com.example.backingapp.R;

import java.util.ArrayList;

public class GridwidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new GridRemoteViewsFactory(this.getApplicationContext());
    }

}

class GridRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory{

    Context mContext;
    ArrayList<Recipe> mRecipeArray = new ArrayList<>();
    public GridRemoteViewsFactory(Context applicationContext) {
        mContext = applicationContext;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
        mRecipeArray = NetworkUtils.fetchRecipeData();
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

        mRecipeArray = NetworkUtils.fetchRecipeData();

        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.backing_widget);
        Recipe recipe = mRecipeArray.get(position);
        int recipeId = recipe.getmId();
        String name = recipe.getmName();


        views.setImageViewResource(R.id.widget_icon, R.drawable.cake_widget);
        views.setTextViewText(R.id.widget_Ing_name, name);

        Bundle extras = new Bundle();
        extras.putInt("WidRecipeId", recipeId);
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
