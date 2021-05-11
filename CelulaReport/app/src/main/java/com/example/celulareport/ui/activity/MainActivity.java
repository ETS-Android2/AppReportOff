package com.example.celulareport.ui.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.celulareport.db.model.ReportEntity;
import com.example.celulareport.ui.fragment.MainListFragment;
import com.google.android.material.appbar.CollapsingToolbarLayout;
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
