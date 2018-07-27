package dawidzior.crypt0track3r.retrofit;

import java.util.List;

import dawidzior.crypt0track3r.CryptoModel;
import dawidzior.crypt0track3r.graph.CryptoDailyModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitService {

    @GET("ticker/")
    Call<List<CryptoModel>> getCoins(@Query("limit") int limit);

    @GET("histoday")
    Call<CryptoDailyModel> getHistoryList(@Query("fsym") String symbol, @Query("tsym") String currency);
}
