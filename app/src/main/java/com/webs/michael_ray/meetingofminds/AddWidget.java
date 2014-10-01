package com.webs.michael_ray.meetingofminds;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;


/**
 * Implementation of App Widget functionality.
 */
public class AddWidget extends AppWidgetProvider {

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;

        for (int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];

            Intent intent1 = new Intent(context, Background.class);
            intent1.putExtra("category","Food");
            PendingIntent pendingIntent1 = PendingIntent.getActivity(context, 0, intent1, 0);

            Intent intent2 = new Intent(context, Background.class);
            intent2.putExtra("category","Community");
            PendingIntent pendingIntent2 = PendingIntent.getActivity(context, 0, intent2, 0);

            Intent intent3 = new Intent(context, Background.class);
            intent3.putExtra("category","Energy");
            PendingIntent pendingIntent3 = PendingIntent.getActivity(context, 0, intent3, 0);

            Intent intent4 = new Intent(context, Background.class);
            intent4.putExtra("category","Transportation");
            PendingIntent pendingIntent4 = PendingIntent.getActivity(context, 0, intent4, 0);

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.add_widget);
            views.setOnClickPendingIntent(R.id.btn_1, pendingIntent1);
            views.setOnClickPendingIntent(R.id.btn_2, pendingIntent2);
            views.setOnClickPendingIntent(R.id.btn_3, pendingIntent3);
            views.setOnClickPendingIntent(R.id.btn_4, pendingIntent4);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
}