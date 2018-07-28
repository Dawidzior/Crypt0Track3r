package dawidzior.crypt0track3r.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class CryptoModel {

    @PrimaryKey
    @NonNull
    private String id;

    private String name;

    private String symbol;

    @SerializedName("price_usd")
    private String priceUsd;

    @SerializedName("percent_change_1h")
    private String percentChange1H;

    @SerializedName("percent_change_24h")
    private String percentChange24H;

    @SerializedName("percent_change_7d")
    private String percentChange7D;

    @SerializedName("total_supply")
    private String totalSupply;

    @SerializedName("max_supply")
    private String maxSupply;

    @SerializedName("24h_volume_usd")
    private String volume24H;

    @SerializedName("market_cap_usd")
    private String marketCap;

    @SerializedName("last_updated")
    private String lastUpdated;
}
