package com.example.celulareport.ui.activity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import com.example.celulareport.ui.fragment.MainListFragment;
import com.example.celulareport.R;


public class MainActivity extends AppCompatActivity {

    private static final int NEW_REPORT_REQUEST_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initializing in a main view fragment Recycle
        if(savedInstanceState == null){
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            MainListFragment mainListFragment = new MainListFragment();
            fragmentTransaction.replace(R.id.main_container, mainListFragment, "MAIN_LIST");
            fragmentTransaction.commit();
        }
    }
}
