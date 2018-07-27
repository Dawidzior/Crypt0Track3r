package dawidzior.crypt0track3r;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dawidzior.crypt0track3r.graph.CryptoDailyModel;
import dawidzior.crypt0track3r.graph.DayData;
import dawidzior.crypt0track3r.retrofit.RetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailsFragment extends Fragment {

    public static final String GRAPH_API_URL = "https://min-api.cryptocompare.com/data/";

    private final String USD = "USD";

    private final int ANIM_TIME = 500;

    private String cryptoCode = "";

    private Tracker tracker;

    @BindView(R.id.chart)
    CandleStickChart chartView;

    @BindView(R.id.coinSymbol)
    TextView coinSymbol;

    @BindView(R.id.coinName)
    TextView coinName;

    @BindView(R.id.priceUsdText)
    TextView priceUsdText;

    @BindView(R.id.percentChange1HText)
    TextView percentChange1HText;

    @BindView(R.id.percentChange24HText)
    TextView percentChange24HText;

    @BindView(R.id.percentChange7DText)
    TextView percentChange7DText;

    @BindView(R.id.totalSupplyText)
    TextView totalSupplyText;

    @BindView(R.id.maxSupplyText)
    TextView maxSupplyText;

    @BindView(R.id.volume24HText)
    TextView volume24HText;

    @BindView(R.id.marketCapText)
    TextView marketCapText;

    public DetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        App application = (App) getActivity().getApplication();
        tracker = application.getDefaultTracker();
    }

    @Override
    public void onResume() {
        super.onResume();
        tracker.setScreenName("Detailed coin~" + coinName.getText());
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        ButterKnife.bind(this, view);

        coinName.setText(getString(R.string.name) + getArguments().getString("name"));
        cryptoCode = getArguments().getString("symbol");
        coinSymbol.setText(getString(R.string.symbol) + cryptoCode);
        priceUsdText.setText(getString(R.string.usd_price) + getArguments().getString("priceUsd"));
        percentChange1HText.setText(getString(R.string.hour_change_1) + getArguments().getString("percentChange1H"));
        percentChange24HText.setText(getString(R.string.hour_change_24) + getArguments().getString("percentChange24H"));
        percentChange7DText.setText(getString(R.string.days_change_7) + getArguments().getString("percentChange7D"));
        totalSupplyText.setText(getString(R.string.total_supply) + getArguments().getString("totalSupply"));
        maxSupplyText.setText(getString(R.string.max_supply) + getArguments().getString("maxSupply"));
        volume24HText.setText(getString(R.string.volume) + getArguments().getString("volume24H"));
        marketCapText.setText(getString(R.string.market_cap) + getArguments().getString("marketCap"));

        Legend legend = chartView.getLegend();
        legend.setEnabled(false);

        Description description = chartView.getDescription();
        description.setTextColor(getResources().getColor(android.R.color.tertiary_text_dark));
        description.setTextSize(18f);
        description.setText(getString(R.string.last_30_days));
        chartView.setDescription(description);
        chartView.setTouchEnabled(false);
        chartView.animateY(ANIM_TIME, Easing.EasingOption.Linear);
        chartView.getXAxis().setDrawLabels(false);
        chartView.setNoDataText(getString(R.string.loading_data));
        chartView.setNoDataTextColor(getResources().getColor(android.R.color.tertiary_text_dark));

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getMonthlyCryptoData();
    }

    private void getMonthlyCryptoData() {
        RetrofitService retrofit = new Retrofit.Builder().baseUrl(GRAPH_API_URL)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .build().create(RetrofitService.class);

        if (!cryptoCode.isEmpty()) {
            ConnectivityManager cm =
                    (ConnectivityManager) App.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            assert cm != null;
            NetworkInfo ni = cm.getActiveNetworkInfo();
            if (ni != null && ni.isConnected()) {
                Call<CryptoDailyModel> retrofitResultCallback = retrofit.getHistoryList(cryptoCode, USD);
                retrofitResultCallback.enqueue(new Callback<CryptoDailyModel>() {
                    @Override
                    public void onResponse(Call<CryptoDailyModel> call, Response<CryptoDailyModel> response) {
                        CryptoDailyModel cryptos = response.body();
                        List<CandleEntry> candleEntryList = new ArrayList<>();

                        int index = 0;
                        for (DayData dayData : cryptos.getDaysList()) {
                            candleEntryList
                                    .add(new CandleEntry(index, dayData.getHigh(), dayData.getLow(), dayData.getOpen(),
                                            dayData.getClose()));
                            index++;
                        }

                        CandleDataSet candleDataSet =
                                new CandleDataSet(candleEntryList, getString(R.string.last_30_days));
                        candleDataSet.setColor(Color.GRAY);
                        candleDataSet.setShadowColor(Color.DKGRAY);
                        candleDataSet.setShadowWidth(1f);
                        candleDataSet.setDecreasingColor(getResources().getColor(R.color.lightRed));
                        candleDataSet.setDecreasingPaintStyle(Paint.Style.FILL);
                        candleDataSet.setIncreasingColor(getResources().getColor(R.color.lightGreen));
                        candleDataSet.setIncreasingPaintStyle(Paint.Style.FILL);
                        candleDataSet.setNeutralColor(Color.BLUE);
                        candleDataSet.setValueTextColor(Color.RED);
                        candleDataSet.setValueTextSize(0); //hide
                        CandleData candleData = new CandleData(candleDataSet);

                        chartView.setData(candleData);
                        chartView.invalidate();
                    }

                    @Override
                    public void onFailure(Call<CryptoDailyModel> call, Throwable t) {
                        Log.d(DetailsFragment.class.getSimpleName(), "Failure response: " + t.getMessage());
                    }
                });
            }
        } else {
            Toast.makeText(App.getContext(), R.string.connection_error, Toast.LENGTH_LONG).show();
        }
    }
}
