package dawidzior.crypt0track3r.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import dawidzior.crypt0track3r.model.CryptoModel;

@Database(entities = CryptoModel.class, version = 1, exportSchema = false)
public abstract class CryptoModelDatabase extends RoomDatabase {

    private static CryptoModelDatabase INSTANCE;

    public abstract CryptosModelDao cryptoModelDao();

    private static final Object sLock = new Object();

    public static CryptoModelDatabase getInstance(Context context) {
        synchronized (sLock) {
            if (INSTANCE == null) {
                INSTANCE =
                        Room.databaseBuilder(context.getApplicationContext(), CryptoModelDatabase.class,
                                "CryptoDatabase").build();
            }
            return INSTANCE;
        }
    }
}
