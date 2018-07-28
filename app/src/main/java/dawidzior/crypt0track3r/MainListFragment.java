package dawidzior.crypt0track3r;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainListFragment extends Fragment {

    private List<CryptoModel> cryptoModelList = new ArrayList<>();

    private CryptoListViewModel viewModel;

    private FirebaseJobDispatcher dispatcher;

    private static int REFRESH_TIMEOUT_IN_SECONDS = 300;

    @BindView(R.id.crypto_list)
    RecyclerView cryptoRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_list, container, false);
        ButterKnife.bind(this, view);

        AdView adView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        adView.loadAd(adRequest);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel = ViewModelProviders.of(this).get(CryptoListViewModel.class);
        viewModel.init();
        viewModel.getCryptos().observe(this, this::setCryptosList);

        dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(getActivity()));
        Job autoUpdateJob = dispatcher.newJobBuilder()
                .setService(AutoUpdateJobService.class)
                .setTag(AutoUpdateJobService.class.getSimpleName())
                .setRecurring(true)
                .setLifetime(Lifetime.UNTIL_NEXT_BOOT)
                // According to API documentation, data is refreshed every 5 minutes.
                .setTrigger(Trigger.executionWindow(REFRESH_TIMEOUT_IN_SECONDS, REFRESH_TIMEOUT_IN_SECONDS + 60))
                .setReplaceCurrent(true)
                .setRetryStrategy(RetryStrategy.DEFAULT_LINEAR)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .build();

        dispatcher.mustSchedule(autoUpdateJob);
    }

    @Override
    public void onPause() {
        super.onPause();
        dispatcher.cancel(AutoUpdateJobService.class.getSimpleName());
    }

    private void setCryptosList(List<CryptoModel> cryptos) {
        if (cryptos != null) {
            cryptoRecyclerView
                    .setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            cryptoRecyclerView.setAdapter(new CryptoItemsListAdapter(cryptos));
        }
    }
}
