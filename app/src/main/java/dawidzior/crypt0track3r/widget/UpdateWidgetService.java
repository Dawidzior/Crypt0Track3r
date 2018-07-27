package dawidzior.crypt0track3r.widget;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;

public class UpdateWidgetService extends JobIntentService {

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        int[] allWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);

        Intent clickIntent = new Intent(this.getApplicationContext(), CryptoWidgetProvider.class);
        clickIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);
        stopSelf();
    }
}