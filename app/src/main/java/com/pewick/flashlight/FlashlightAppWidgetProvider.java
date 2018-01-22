package com.pewick.flashlight;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

/**
 * Created by Chris on 1/15/2018.
 */

public class FlashlightAppWidgetProvider extends AppWidgetProvider {
    private static final String TAG = "WidgetProvider";

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds){
        updateWidget(context, appWidgetManager, false);
    }

    public static void updateWidget(Context context, AppWidgetManager appWidgetManager, boolean isLightOn){
        Log.i(TAG, "updateWidget, light: "+isLightOn);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, FlashlightAppWidgetProvider.class));
        final int N = appWidgetIds.length;
        Log.i(TAG, "N: "+N);

        // Perform this loop procedure for each App Widget that belongs to this provider
        for(int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];

            Intent intent = new Intent(context, FlashlightService.class);
            PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);

            if(isLightOn) {
                views.setImageViewResource(R.id.widget_button, R.drawable.flash_off_24dp);
            } else{
                views.setImageViewResource(R.id.widget_button, R.drawable.flash_on_24dp);
            }

            views.setOnClickPendingIntent(R.id.widget_button, pendingIntent);

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
}
