package com.example.celulareport.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.celulareport.R;
import com.example.celulareport.ui.Constraint;
import com.example.celulareport.ui.adapter.LineAdapter;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainListFragment extends Fragment implements LineAdapter.OnLineClickListerner {

    private static final String TAG = "MonthRecyclerFragment";
    LinearLayoutManager mLayoutManager;
    private ArrayList<String> Months;
    Toolbar mToolbar;
    CollapsingToolbarLayout mCollapsing;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Initialize data set
        initDataSet();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_main_list, container, false);
        viewRoot.setTag(TAG);


        //setup toolbar to change color when expanding or collapsing
        SetupToolbar(viewRoot);

        //getting floating button
        FloatingActionButton mFButtonAddReport = viewRoot.findViewById(R.id.fb_add_report);
        onCLickFloatingButton(mFButtonAddReport);
        //inflate recycleView
        SetupRecycler(viewRoot);

        return viewRoot;
    }

    //Setup toolbar and collapsing bar
    private void SetupToolbar(View v){
        mToolbar = (Toolbar) v.findViewById(R.id.main_toolbar);
        mToolbar.setTitle(R.string.main_title);
        mToolbar.setLogo(R.drawable.ic_report);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        mCollapsing = (CollapsingToolbarLayout) v.findViewById(R.id.CollapsingToolbar);

        mCollapsing.setCollapsedTitleTextAppearance(R.style.Toolbar_TitleText_Collapsed_main);
        mCollapsing.setExpandedTitleTextAppearance(R.style.Toolbar_TitleText_Expanded_main);
        mCollapsing.setExpandedTitleGravity(Gravity.CENTER);
    }

    //RecycleView settings
    private void SetupRecycler(View viewRoot){

        RecyclerView mRecycler = viewRoot.findViewById(R.id.recycleView_months);
        /* setting layout manager to the list */
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecycler.setLayoutManager(mLayoutManager);

        //Objects to be adding in the list

        //Adding adapter that will be annexe the object in the list
        LineAdapter mAdapter = new LineAdapter(Months, this);
        mRecycler.setAdapter(mAdapter);

        //adding divisor between lists
        mRecycler.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

    }

    //Init data set used
    private void initDataSet() {
        Months = new ArrayList<String>();

        //Adding months
        Months.add(Constraint.MONTHS[0]);
        Months.add(Constraint.MONTHS[1]);
        Months.add(Constraint.MONTHS[2]);
        Months.add(Constraint.MONTHS[3]);
        Months.add(Constraint.MONTHS[4]);
        Months.add(Constraint.MONTHS[5]);
        Months.add(Constraint.MONTHS[6]);
        Months.add(Constraint.MONTHS[7]);
        Months.add(Constraint.MONTHS[8]);
        Months.add(Constraint.MONTHS[9]);
        Months.add(Constraint.MONTHS[10]);
        Months.add(Constraint.MONTHS[11]);

    }

    @Override
    public void onLineClick(int position, TextView textMonth) {
        Log.d(TAG, "OnLineClick: clicked,line "+position);
        //make transition between Activity
        ReportsListFragment fragment = ReportsListFragment.newInstance(Constraint.MONTHS[position]);
        getActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .setCustomAnimations(R.anim.enter_fade_in, R.anim.exit_fade_in, R.anim.enter_fade_in, R.anim.exit_fade_in)
                .replace(R.id.main_container, fragment, fragment.getClass().getName())
                .addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();

    }

    public void onCLickFloatingButton(FloatingActionButton mFButtonAddReport){

        mFButtonAddReport.setOnClickListener(v -> {

            AddReportFragment fragment = new AddReportFragment();
            getActivity()
                    .getSupportFragmentManager()
                    .beginTransaction().setReorderingAllowed(true)
                    .setCustomAnimations(R.anim.enter_slide_up,
                            R.anim.exit_fade_freeze,
                            R.anim.enter_fade_freeze,
                            R.anim.exit_slide_down)
                    .replace(R.id.main_container, fragment, fragment.getClass().getName())
                    .addToBackStack(null)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit();
        });
    }

}
