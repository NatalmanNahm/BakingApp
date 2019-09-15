package com.example.backingapp.widget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.widget.RemoteViews;

import com.example.backingapp.BakingActivity;
import com.example.backingapp.Database.AppDatabase;
import com.example.backingapp.MainActivity;
import com.example.backingapp.Model.Recipe;
import com.example.backingapp.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Implementation of App Widget functionality.
 */
public class BackingWidget extends AppWidgetProvider {

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        RemoteViews  views = getGridIndredientRemoteView(context);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    public static void updateRecipeWidget(Context context, AppWidgetManager appWidgetManager,
                                          int [] appWidgets){
        for (int appWidget : appWidgets){
            updateAppWidget(context, appWidgetManager, appWidget);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        UpdateWidget.startActionUpdateWidgets(context);
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

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {

        UpdateWidget.startActionUpdateWidgets(context);

        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }
}

