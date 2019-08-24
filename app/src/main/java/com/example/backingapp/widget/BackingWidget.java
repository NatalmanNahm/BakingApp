package com.example.backingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.example.backingapp.BakingActivity;
import com.example.backingapp.MainActivity;
import com.example.backingapp.Model.Recipe;
import com.example.backingapp.R;

import java.util.ArrayList;

/**
 * Implementation of App Widget functionality.
 */
public class BackingWidget extends AppWidgetProvider {

    private static ArrayList<Recipe> mRecipes;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        RemoteViews views = getGridIndredientRemoteView(context);

//        // Create an Intent to launch MainActivity when clicked
//        Intent intent = new Intent(context, BakingActivity.class);
//        intent.putExtra("WidRecipeId", 2);
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        // Construct the RemoteViews object
//        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.backing_widget);
//        // Widgets allow click handlers to only launch pending intents
//        views.setOnClickPendingIntent(R.id.widget_icon, pendingIntent);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

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
//        intent.putParcelableArrayListExtra("ArrayList", mRecipes);
//        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        views.setRemoteAdapter(R.id.widget_grid_view, intent);

        Intent appIntent = new Intent(context, BakingActivity.class);
        PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setPendingIntentTemplate(R.id.widget_grid_view, appPendingIntent);

        views.setEmptyView(R.id.widget_grid_view, R.id.empty_view);

        return views;
    }

//    public static void setRecipes(ArrayList<Recipe> recipes){
//        mRecipes = recipes;
//    }
//
//    @Override
//    public void onReceive(Context context, Intent intent) {
//
//        if (!intent.getParcelableArrayListExtra("ArrayList").isEmpty()){
//            mRecipes = intent.getParcelableArrayListExtra("ArrayList");
//        }
//        super.onReceive(context, intent);
//    }
}

