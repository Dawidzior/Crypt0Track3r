package dawidzior.crypt0track3r.list;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

//import androidx.navigation.Navigation;
import dawidzior.crypt0track3r.App;
import dawidzior.crypt0track3r.R;
import dawidzior.crypt0track3r.details.DetailsActivity;
import dawidzior.crypt0track3r.model.CryptoModel;

public class CryptoItemsListAdapter extends RecyclerView.Adapter<CryptoItemsListAdapter.ViewHolder> {

    private static final String PERCENTAGE = "%";
    private static final String MINUS = "-";

    // Icons provided by https://github.com/atomiclabs/cryptocurrency-icons
    private static final String ICONS_LINK = "http://res.cloudinary.com/dnpeuogpf/image/upload/cryptos/";

    private final List<CryptoModel> cryptoCurrencies;

    public CryptoItemsListAdapter(List<CryptoModel> items) {
        cryptoCurrencies = items;
    }

    @NonNull
    @Override
    public CryptoItemsListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.crypto_list_item, parent, false);
        return new CryptoItemsListAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final CryptoItemsListAdapter.ViewHolder holder, int position) {
        CryptoModel crypto = cryptoCurrencies.get(position);

        Picasso.get().load(ICONS_LINK + crypto.getSymbol().toLowerCase() + ".png")
                .placeholder(R.drawable.ic_cloud_download).error(R.drawable.ic_error).into(holder.coinIcon);

        holder.coinSymbol.setText(crypto.getSymbol());
        holder.coinName.setText(crypto.getName());
        holder.priceUsdText.setText(crypto.getPriceUsd());
        holder.percentChange1HText.setText(crypto.getPercentChange1H() + PERCENTAGE);
        setPercentageColor(holder.percentChange1HText);
        holder.percentChange24HText.setText(crypto.getPercentChange24H() + PERCENTAGE);
        setPercentageColor(holder.percentChange24HText);
        holder.percentChange7DText.setText(crypto.getPercentChange7D() + PERCENTAGE);
        setPercentageColor(holder.percentChange7DText);

//    TODO temporary switched off till stable release of the architecture component.
//        holder.itemView.setOnClickListener(
//                Navigation.createNavigateOnClickListener(R.id.action_list_to_details, createBundle(crypto)));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent details = new Intent(App.getContext(), DetailsActivity.class);
                details.putExtras(createBundle(crypto));
                App.getContext().startActivity(details);
            }
        });

    }

    @NonNull
    private Bundle createBundle(CryptoModel crypto) {
        Bundle bundle = new Bundle();
        bundle.putString("name", crypto.getName());
        bundle.putString("symbol", crypto.getSymbol());
        bundle.putString("priceUsd", crypto.getPriceUsd());
        bundle.putString("percentChange1H", crypto.getPercentChange1H() + PERCENTAGE);
        bundle.putString("percentChange24H", crypto.getPercentChange24H() + PERCENTAGE);
        bundle.putString("percentChange7D", crypto.getPercentChange7D() + PERCENTAGE);
        bundle.putString("totalSupply", crypto.getTotalSupply());
        bundle.putString("maxSupply", crypto.getMaxSupply());
        bundle.putString("volume24H", crypto.getVolume24H());
        bundle.putString("marketCap", crypto.getMarketCap());
        return bundle;
    }

    @Override
    public int getItemCount() {
        return cryptoCurrencies.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView coinIcon;
        final TextView coinSymbol;
        final TextView coinName;
        final TextView priceUsdText;
        final TextView percentChange1HText;
        final TextView percentChange24HText;
        final TextView percentChange7DText;

        ViewHolder(View view) {
            super(view);
            coinIcon = view.findViewById(R.id.coinIcon);
            coinSymbol = view.findViewById(R.id.coinSymbol);
            coinName = view.findViewById(R.id.coinName);
            priceUsdText = view.findViewById(R.id.priceUsdText);
            percentChange1HText = view.findViewById(R.id.percentChange1HText);
            percentChange24HText = view.findViewById(R.id.percentChange24HText);
            percentChange7DText = view.findViewById(R.id.percentChange7DText);
        }
    }

    private static void setPercentageColor(TextView textView) {
        if (String.valueOf(textView.getText().charAt(0)).equals(MINUS)) {
            textView.setTextColor(App.getContext().getResources().getColor(R.color.lightRed));
        } else {
            textView.setTextColor(App.getContext().getResources().getColor(R.color.lightGreen));
        }
    }
}
