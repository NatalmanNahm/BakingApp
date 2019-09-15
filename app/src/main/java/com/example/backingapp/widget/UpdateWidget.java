package com.example.backingapp.widget;

import android.app.IntentService;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.RemoteViews;

import com.example.backingapp.BakingActivity;
import com.example.backingapp.R;


/**
 * Service class to take care of the updating od the
 * widget whenever data changes
 */
public class UpdateWidget extends IntentService {


    public static final String ACTION_UPDATE_WIDGET =
            "com.example.android.backingapp.action.update.widget";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public UpdateWidget(){
        super("UdateWidget");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent!= null){
            final String action = intent.getAction();
            if (ACTION_UPDATE_WIDGET.equals(action)){
                handleActionUpdateCakeWidget();
            }
        }
    }

    /**
     * refresh widget to get the last update
     * @param context
     */
    public static void startActionUpdateWidgets(Context context){
        Intent intent = new Intent(context, UpdateWidget.class);
        intent.setAction(ACTION_UPDATE_WIDGET);
        context.startService(intent);
    }


    /**
     * handle teh updating of widget
     */
    private void handleActionUpdateCakeWidget(){

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appwidgets = appWidgetManager.getAppWidgetIds(new ComponentName(this, BackingWidget.class));
        appWidgetManager.notifyAppWidgetViewDataChanged(appwidgets, R.id.widget_grid_view);

        BackingWidget.updateRecipeWidget(this, appWidgetManager, appwidgets);
    }


}
