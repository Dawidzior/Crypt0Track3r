package dawidzior.crypt0track3r;

import android.app.Application;
import android.content.Context;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

public class App extends Application {

    private static GoogleAnalytics analytics;
    private static Tracker tracker;
    private static App app;

    public static App getApplication() {
        return app;
    }

    public static Context getContext() {
        return getApplication().getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        analytics = GoogleAnalytics.getInstance(this);
    }

    synchronized public Tracker getDefaultTracker() {
        if (tracker == null) {
            tracker = analytics.newTracker(R.xml.global_tracker);
        }

        return tracker;
    }
}
