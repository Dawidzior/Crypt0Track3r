package dawidzior.crypt0track3r.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import dawidzior.crypt0track3r.model.CryptoModel;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface CryptosModelDao {

    @Insert(onConflict = REPLACE)
    void save(List<CryptoModel> cryptoModelList);

    @Query("SELECT * FROM cryptoModel")
    LiveData<List<CryptoModel>> load();
}
