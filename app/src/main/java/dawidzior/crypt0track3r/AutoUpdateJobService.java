package dawidzior.crypt0track3r;


import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

public class AutoUpdateJobService extends JobService {

    @Override
    public boolean onStartJob(JobParameters job) {
        Log.d(AutoUpdateJobService.class.getSimpleName(), "AutoUpdateJobService onStartJob");
        new Thread(() -> {
            Log.d(AutoUpdateJobService.class.getSimpleName(), "AutoUpdateJobService run");
            CryptoModelRepository.refreshCryptos();
        }).start();

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return false;
    }

}
