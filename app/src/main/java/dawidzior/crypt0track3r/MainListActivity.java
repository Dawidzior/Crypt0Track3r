package dawidzior.crypt0track3r;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import androidx.navigation.Navigation;

public class MainListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_list);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return Navigation.findNavController(this, R.id.fragment_nav_host).navigateUp();
    }
}
