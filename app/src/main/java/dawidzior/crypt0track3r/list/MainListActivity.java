package dawidzior.crypt0track3r.list;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import dawidzior.crypt0track3r.R;

public class MainListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_list);
    }

    //TODO switched off till stable release of the architecture component.
//    @Override
//    public boolean onSupportNavigateUp() {
//        return Navigation.findNavController(this, R.id.fragment_nav_host).navigateUp();
//    }
}
