package dawidzior.crypt0track3r.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.List;

import dawidzior.crypt0track3r.model.CryptoModel;
import dawidzior.crypt0track3r.CryptoModelRepository;
import dawidzior.crypt0track3r.R;
import dawidzior.crypt0track3r.retrofit.RetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CryptoWidgetProvider extends AppWidgetProvider {

    private static final String TAG = CryptoWidgetProvider.class.getSimpleName();

    private static final String ACTION_NEXT_CRYPTO = "ACTION_NEXT_CRYPTO";

    private final String DOLLAR = "$";

    private static int cryptoNumber = 0;

    private RemoteViews remoteViews;

    private static List<CryptoModel> cryptoModelList;

    @Override
    public void onReceive(Context context, Intent widgetIntent) {
        super.onReceive(context, widgetIntent);
        final String action = widgetIntent.getAction();

        if (ACTION_NEXT_CRYPTO.equals(action)) {
            cryptoNumber++;
            if (cryptoNumber == CryptoModelRepository.REQUESTED_COINS) cryptoNumber = 0;

            updateTextViews(context);
        }
    }

    @Override
    public void onUpdate(final Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        RetrofitService retrofitService = CryptoModelRepository.getRetrofitInstance();

        Call<List<CryptoModel>> call = retrofitService.getCoins(CryptoModelRepository.REQUESTED_COINS);
        call.enqueue(new Callback<List<CryptoModel>>() {
            @Override
            public void onResponse(Call<List<CryptoModel>> call, Response<List<CryptoModel>> response) {
                cryptoModelList = response.body();
                updateTextViews(context);
            }

            @Override
            public void onFailure(Call<List<CryptoModel>> call, Throwable t) {
                Log.d(TAG, "Failure response: " + t.getMessage());
            }
        });

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    private void updateTextViews(Context context) {
        remoteViews = new RemoteViews(context.getPackageName(), R.layout.crypto_widget_provider);

        if (cryptoModelList != null) {
            remoteViews.setTextViewText(R.id.coin_name, cryptoModelList.get(cryptoNumber).getName());
            remoteViews.setTextViewText(R.id.coin_value, cryptoModelList.get(cryptoNumber).getPriceUsd() + DOLLAR);

            Intent intent = new Intent(context, CryptoWidgetProvider.class);
            intent.setAction(ACTION_NEXT_CRYPTO);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            remoteViews.setOnClickPendingIntent(R.id.widget_container, pendingIntent);
        } else {
            remoteViews.setTextViewText(R.id.coin_name, context.getString(R.string.parsing_data));
            remoteViews.setTextViewText(R.id.coin_value, context.getString(R.string.from_internet));
        }

        // Notify the widget that the list view needs to be updated.
        final AppWidgetManager mgr = AppWidgetManager.getInstance(context);
        final ComponentName cn = new ComponentName(context, CryptoWidgetProvider.class);
        mgr.updateAppWidget(cn, remoteViews);
    }
}
