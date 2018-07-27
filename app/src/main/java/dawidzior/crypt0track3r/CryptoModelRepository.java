package dawidzior.crypt0track3r;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import dawidzior.crypt0track3r.database.CryptosModelDao;
import dawidzior.crypt0track3r.retrofit.RetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CryptoModelRepository {

    public static final String API_URL = "https://api.coinmarketcap.com/v1/";
    public static final int REQUESTED_COINS = 10;
    private static CryptosModelDao cryptosModelDao;
    private static Executor executor = Executors.newSingleThreadExecutor();
    private static CryptoModelRepository INSTANCE_REPO = null;
    private static RetrofitService INSTANCE_RETROFIT = null;
    private static NetworkInfo ni;

    private CryptoModelRepository(CryptosModelDao cryptosModelDao) {
        CryptoModelRepository.cryptosModelDao = cryptosModelDao;

        ConnectivityManager cm = (ConnectivityManager) App.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        ni = cm.getActiveNetworkInfo();
    }

    public static CryptoModelRepository getCryptoRepoInstance(CryptosModelDao cryptosModelDao) {
        if (INSTANCE_REPO == null) {
            INSTANCE_REPO = new CryptoModelRepository(cryptosModelDao);
        }
        return INSTANCE_REPO;
    }

    public static RetrofitService getRetrofitInstance() {
        if (INSTANCE_RETROFIT == null) {
            INSTANCE_RETROFIT = new Retrofit.Builder().baseUrl(CryptoModelRepository.API_URL)
                    .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                    .build().create(RetrofitService.class);
        }
        return INSTANCE_RETROFIT;
    }

    public LiveData<List<CryptoModel>> getCryptoModels() {
        return cryptosModelDao.load(); // return a LiveData directly from the database.
    }

    public static void refreshCryptos() {

        if (ni != null && ni.isConnected()) {
            executor.execute(() -> {
                Call<List<CryptoModel>> retrofitResultCallback =
                        getRetrofitInstance().getCoins(CryptoModelRepository.REQUESTED_COINS);
                retrofitResultCallback.enqueue(new Callback<List<CryptoModel>>() {
                    @Override
                    public void onResponse(Call<List<CryptoModel>> call, Response<List<CryptoModel>> response) {
                        executor.execute(() ->
                        {
                            List<CryptoModel> cryptos = response.body();
                            cryptosModelDao.save(cryptos);
                        });
                    }

                    @Override
                    public void onFailure(Call<List<CryptoModel>> call, Throwable t) {
                        Log.d(CryptoModelRepository.class.getSimpleName(), "Failure response: " + t.getMessage());
                    }
                });
            });
        } else {
            Toast.makeText(App.getContext(), R.string.connection_error, Toast.LENGTH_LONG).show();
        }
    }
}
