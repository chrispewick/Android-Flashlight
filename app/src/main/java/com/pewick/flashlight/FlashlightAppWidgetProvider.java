package com.pewick.flashlight;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

/**
 * Created by Chris on 1/15/2018.
 */

public class FlashlightAppWidgetProvider extends AppWidgetProvider {

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds){
        final int N = appWidgetIds.length;

        // Perform this loop procedure for each App Widget that belongs to this provider
        //Note: perhaps this can allow multiple widget for this app. However, may complicate too much to be worth that
        // Also, perhaps having mulitple widgets would cause the ilight to turn on/off for each widget. So test thoroughly if using
        for (int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];

            // Create an Intent to launch ExampleActivity
            //Maybe need a "turn on" and "turn off" service
            //Or, check within the service if the light is on? Would still need to handle updating the widget view though
            //Perhaps handle with a callback to this, or another handler from the service?
            Intent intent = new Intent(context, ExampleActivity.class); //TODO: write and connect service here
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);


            // Get the layout for the App Widget and attach an on-click listener
            // to the button
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
            views.setOnClickPendingIntent(R.id.widget_button, pendingIntent);

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
}
