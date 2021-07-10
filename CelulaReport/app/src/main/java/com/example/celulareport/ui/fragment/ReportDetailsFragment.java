package com.example.celulareport.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Environment;
import android.os.Parcelable;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.celulareport.R;
import com.example.celulareport.db.model.ReportEntity;
import com.example.celulareport.viewmodel.ReportsViewModel;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Objects;

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
    MultiAutoCompleteTextView mCommitsText;

    //Toolbar
    Toolbar mToolbar;
    CollapsingToolbarLayout mCollapsing;

    //Id of report
    private long mReportID;
    private ReportEntity reportEntity;

    private View viewRoot;

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

        //Just for propose test
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

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

        viewRoot = view;
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
        mCommitsText = v.findViewById(R.id.txt_commits);

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
        reportEntity = mReport;
        String celula = mReport.getNomeCelula();
        mToolbar.setTitle(celula);

        mLiderText.setText(mReport.getNomeLider());
        mColiderText.setText(mReport.getNomeColider());
        mAnfitriaoText.setText(mReport.getNomeAnfitriao());
        mDataText.setText(mReport.getDataToShow());
        mMembrosText.setText(mReport.getNumMembros());
        mVisitantesText.setText(mReport.getNumVisitantes());
        mOfertaText.setText(mReport.getOferta());
        mEstudoText.setText(mReport.getEstudo());
        mCommitsText.setText(mReport.getComentarios());

        Log.i("ReportDetailsFragment", "Commit Text: "+mReport.getComentarios());
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
                File pdfFile = createReportPDF();
                final Uri uri = Uri.fromFile(pdfFile);

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Relatório de"+reportEntity.getDataToShow());
                sendIntent.putExtra(Intent.EXTRA_STREAM, uri);
                sendIntent.putExtra(Intent.EXTRA_TITLE, "Relatório de Célula");
                sendIntent.setType("application/pdf");

                Intent share = Intent.createChooser(sendIntent, getString(R.string.sharing_title));
                startActivity(share);

                Toast.makeText(getContext(), "Shared icon clicked", Toast.LENGTH_SHORT).show();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @NonNull
    private File createReportPDF(){

        // create a new document
        PdfDocument document = new PdfDocument();

        // create a page description
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(900, 1424, 1).create();

        // start a page
        PdfDocument.Page page = document.startPage(pageInfo);

        // draw something on the page
        //View content = viewRoot.findViewById(R.id.layout_report_details);

        View content = View.inflate(getContext(), R.layout.report_page, null);
        TextView celula = content.findViewById(R.id.txt_celula);
        celula.setText(reportEntity.getNomeCelula());

        TextView lider = content.findViewById(R.id.txt_lider);
        lider.setText(reportEntity.getNomeLider());

        TextView colider = content.findViewById(R.id.txt_colider);
        colider.setText(reportEntity.getNomeColider());

        TextView anfitriao = content.findViewById(R.id.txt_anfitriao);
        anfitriao.setText(reportEntity.getNomeAnfitriao());

        TextView membros = content.findViewById(R.id.txt_membros);
        membros.setText(reportEntity.getNumMembros());

        TextView visitantes = content.findViewById(R.id.txt_visitantes);
        visitantes.setText(reportEntity.getNumVisitantes());

        TextView data = content.findViewById(R.id.txt_data);
        data.setText(reportEntity.getDataToShow());

        TextView versiculo = content.findViewById(R.id.txt_estudo);
        versiculo.setText(reportEntity.getEstudo());

        TextView oferta = content.findViewById(R.id.txt_oferta);
        oferta.setText(reportEntity.getOferta());

        TextView comentarios = content.findViewById(R.id.txt_commits);
        comentarios.setText(reportEntity.getComentarios());


        //draw according to page size
        content.measure(900, 1424);
        content.layout(0, 0, 900, 1424);
        content.draw(page.getCanvas());

        // finish the page
        document.finishPage(page);

        //create a file do write pdf
        File path  = Objects.requireNonNull(requireContext()).getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        //String path = "/sdcard/"+"report-"+reportEntity.getDataReuniao()+".pdf";
        String fileName = "report-"+reportEntity.getDataReuniao()+".pdf";
        File file = new File(path, fileName);

        try {
            OutputStream outputStream = new FileOutputStream(file);
            document.writeTo(outputStream);
            // write the document content
            Toast.makeText(getActivity(), "PDF file generated successfully. Local: "+file.getPath(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }



        // close the document
        document.close();

        return file;
    }


}