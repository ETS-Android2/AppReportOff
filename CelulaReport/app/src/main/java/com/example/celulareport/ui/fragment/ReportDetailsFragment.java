package com.example.celulareport.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.celulareport.R;
import com.example.celulareport.db.model.ReportEntity;
import com.example.celulareport.viewmodel.ReportsViewModel;
import com.google.android.material.appbar.CollapsingToolbarLayout;

public class ReportDetailsFragment extends Fragment {

    private static final String REPORT_ID= "REPORT_ID";
    //ViewModel to get report selected
    private ReportsViewModel mViewModel;

    //Views objects
    TextView mLiderText;
    TextView mColiderText;
    TextView mAnfitriaoText;
    TextView mDataText;
    TextView mMembrosText;
    TextView mVisitantesText;
    TextView mOfertaText;
    TextView mEstudoText;

    //Toolbar
    Toolbar mToolbar;
    CollapsingToolbarLayout mCollapsing;

    //Id of report
    private long mReportID;

    public ReportDetailsFragment() {
        // Required empty public constructor
    }

    public static ReportDetailsFragment newInstance(final long reportID){
        Bundle args = new Bundle();
        args.putLong(REPORT_ID, reportID);

        ReportDetailsFragment fragment = new ReportDetailsFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if(getArguments() != null){
            mReportID = getArguments().getLong(REPORT_ID);
        }else {
            Toast.makeText(getContext(), "Relório não existe no banco de dados ", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_report_details, container, false);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewModel = new ViewModelProvider(requireActivity()).get(ReportsViewModel.class);
        registerReportSelected(mViewModel);

        //Text Views Contain reports details
        getViewObjects(view);

        //Setup toolbar
        SetupToolbar(view);
    }

    public void  getViewObjects(View v){
        mLiderText = v.findViewById(R.id.txt_lider);
        mColiderText = v.findViewById(R.id.txt_colider);
        mAnfitriaoText = v.findViewById(R.id.txt_anfitriao);
        mDataText = v.findViewById(R.id.txt_data);
        mMembrosText = v.findViewById(R.id.txt_membros);
        mVisitantesText = v.findViewById(R.id.txt_visitantes);
        mOfertaText = v.findViewById(R.id.txt_oferta);
        mEstudoText = v.findViewById(R.id.txt_estudo);


        mToolbar = v.findViewById(R.id.toolbar_details);
        mCollapsing = v.findViewById(R.id.collapsing_details);

    }

    public void registerReportSelected(ReportsViewModel viewModel){
        viewModel.ReportSelected(mReportID).observe(getViewLifecycleOwner(), reportEntity -> {
            //Adding report data in the view
            addingReportDetails(reportEntity);
        });
    }


    public void addingReportDetails(ReportEntity mReport){
        String celula = mReport.getNomeCelula();
        mToolbar.setTitle(celula);

        mLiderText.setText(mReport.getNomeLider());
        mColiderText.setText(mReport.getNomeColider());
        mAnfitriaoText.setText(mReport.getNomeAnfitriao());
        mDataText.setText(mReport.getDataToShow());
        mMembrosText.setText(mReport.getNumMembros());
        mVisitantesText.setText(mReport.getNumVisitantes());
        mOfertaText.setText(mReport.getOferta());
        mEstudoText.setText(mReport.getComentarios());
    }

    private void SetupToolbar(View v){

        mToolbar.setLogo(R.drawable.ic_report);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        mCollapsing.setCollapsedTitleTextAppearance(R.style.Toolbar_TitleText_Collapsed_details);
        mCollapsing.setExpandedTitleTextAppearance(R.style.Toolbar_TitleText_Expanded_details);
        mCollapsing.setExpandedTitleGravity(Gravity.BOTTOM);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.report_details_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    boolean isFavorite = false;
    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {

        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()){

            case R.id.ic_favorite_details:
                Toast.makeText(getContext(), "Favorite action clicked", Toast.LENGTH_SHORT).show();
                isFavorite = !isFavorite;
                item.setIcon(isFavorite?R.drawable.ic_favorite:R.drawable.ic_favorite_border);
                return true;

            case R.id.ic_details_share:
                Toast.makeText(getContext(), "Shared icon clicked", Toast.LENGTH_SHORT).show();
                return true;
        }


        return super.onOptionsItemSelected(item);
    }
}