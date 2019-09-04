package com.example.backingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.widget.RemoteViews;

import com.example.backingapp.BakingActivity;
import com.example.backingapp.Database.AppDatabase;
import com.example.backingapp.MainActivity;
import com.example.backingapp.Model.Recipe;
import com.example.backingapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of App Widget functionality.
 */
public class BackingWidget extends AppWidgetProvider {

    private static List<Recipe> recipe = new ArrayList<>();
    private static AppDatabase mDb;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
//        mDb = AppDatabase.getInstance(context);
//
//        new FetchDataFromDatabase().execute();

        RemoteViews  views = getGridIndredientRemoteView(context);
//        //look in the database to see if there is data
//        //if there is no data then open MainActivity
//        if (recipe == null){
//            views = getCakeRecipesRemoteView(context);
//        } else { // if there is then open the Ingredient
//            views = getGridIndredientRemoteView(context);
//        }


        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    private static RemoteViews getGridIndredientRemoteView(Context context){

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_grid_view);
        Intent intent = new Intent(context, GridwidgetService.class);
        views.setRemoteAdapter(R.id.widget_grid_view, intent);

        Intent appIntent = new Intent(context, BakingActivity.class);
        PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.widget_grid_view, appPendingIntent);

        views.setEmptyView(R.id.widget_grid_view, R.id.empty_view);

        return views;
    }

    private  static  RemoteViews getCakeRecipesRemoteView(Context context){

        Intent intent = new Intent(context, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.backing_widget);
        // Widgets allow click handlers to only launch pending intents
        views.setOnClickPendingIntent(R.id.widget_icon, pendingIntent);

        return views;
    }

    public static class FetchDataFromDatabase extends AsyncTask<Context, Void, List<Recipe>>{

        @Override
        protected List<Recipe> doInBackground(Context... contexts) {
            recipe = null;

            recipe = mDb.recipesDao().GetAllFavRecipe();
            return recipe;
        }

        @Override
        protected void onPostExecute(List<Recipe> recipes) {
            super.onPostExecute(recipes);
        }
    }

}

