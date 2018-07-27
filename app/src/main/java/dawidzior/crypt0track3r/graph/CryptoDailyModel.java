package dawidzior.crypt0track3r.graph;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Getter;

public class CryptoDailyModel {

    @Getter
    @SerializedName("TimeTo")
    private long time;

    @Getter
    @SerializedName("Data")
    private List<DayData> daysList;
}