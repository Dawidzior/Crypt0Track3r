package dawidzior.crypt0track3r.model;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

import dawidzior.crypt0track3r.CryptoModelRepository;
import dawidzior.crypt0track3r.database.CryptoModelDatabase;

public class CryptoListViewModel extends AndroidViewModel {

    private LiveData<List<CryptoModel>> cryptosModels;
    private CryptoModelRepository cryptoModelRepository;

    public CryptoListViewModel(Application application) {
        this(application, (CryptoModelRepository
                .getCryptoRepoInstance(CryptoModelDatabase.getInstance(application).cryptoModelDao())));
    }

    public CryptoListViewModel(Application application, CryptoModelRepository cryptoModelRepository) {
        super(application);
        this.cryptoModelRepository = cryptoModelRepository;
    }

    public void init() {
        //Force refresh from Interet.
        CryptoModelRepository.refreshCryptos();
        cryptosModels = cryptoModelRepository.getCryptoModels();
    }

    public LiveData<List<CryptoModel>> getCryptos() {
        return this.cryptosModels;
    }
}
