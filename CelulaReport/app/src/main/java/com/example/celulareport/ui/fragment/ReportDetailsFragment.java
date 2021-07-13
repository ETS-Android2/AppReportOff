package com.example.celulareport.ui.fragment;

import android.Manifest;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
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
import android.widget.ProgressBar;
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
import java.text.MessageFormat;
import java.util.Objects;

public class ReportDetailsFragment extends Fragment {

    private static final String REPORT_ID= "REPORT_ID";
    private static final String URI_PROVIDER_AUTHORITY= "com.example.fileprovider";
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
    TextView mCommitsText;

    //Toolbar
    Toolbar mToolbar;
    CollapsingToolbarLayout mCollapsing;

    //progress
    private ProgressBar progressBar;
    //Id of report
    private long mReportID;
    private ReportEntity reportEntity;

    //for permission
    private ActivityResultLauncher<String> requestPermissionLauncher;

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

        requestPermissionLauncher = getPermissionCallback();

        if(getArguments() != null){
            mReportID = getArguments().getLong(REPORT_ID);
        }else {
            Toast.makeText(getContext(),
                    R.string.report_not_found,
                    Toast.LENGTH_LONG).show();
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

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.report_details_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {

        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()){

            case R.id.ic_delete:
                deleteReport(mReportID);
                requireActivity().getSupportFragmentManager()
                        .popBackStackImmediate();
                Toast.makeText(requireContext(), "Delete action clicked", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.ic_details_share:
                shareWithPermissionGranted();
                return true;

            case R.id.ic_edit:

                Toast.makeText(requireContext(), "Edit action clicked", Toast.LENGTH_SHORT).show();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void  getViewObjects(@NonNull View v){
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

        progressBar = v.findViewById(R.id.progress_details);

    }

    public void registerReportSelected(ReportsViewModel viewModel){
        //get report selected
        viewModel.ReportSelected(mReportID).observe(getViewLifecycleOwner(), this::addingReportDetails);
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

    public void updateReport(ReportEntity reportEntity){
        mViewModel.update(reportEntity);
    }

    public void deleteReport(long reportId){

        //TODO: Create dialog alert
        mViewModel.deleteById(reportId);
    }

    private void SetupToolbar(View v){

        mToolbar.setLogo(R.drawable.ic_report);
        ((AppCompatActivity)requireActivity()).setSupportActionBar(mToolbar);
        mCollapsing.setCollapsedTitleTextAppearance(R.style.Toolbar_TitleText_Collapsed_details);
        mCollapsing.setExpandedTitleTextAppearance(R.style.Toolbar_TitleText_Expanded_details);
        mCollapsing.setExpandedTitleGravity(Gravity.BOTTOM);
    }


    @NonNull
    private ActivityResultLauncher<String> getPermissionCallback() {
        return registerForActivityResult(
                new ActivityResultContracts.RequestPermission(), isGranted -> {
                    if (isGranted) {

                        progressBar.setVisibility(View.VISIBLE);
                        Thread thread = new Thread(){
                            @Override
                            public void run() {
                                shareReportPdf();
                            }
                        };

                        thread.start();
                    } else {
                        Toast.makeText(getContext(), R.string.explaining_why_feature_is_unavalible, Toast.LENGTH_LONG).show();
                    }
                }
            );
    }

    private void shareWithPermissionGranted(){
        if(ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED){

            //show progress bar
            progressBar.setVisibility(View.VISIBLE);

            Thread thread = new Thread(){
                @Override
                public void run() {
                    shareReportPdf();
                }
            };
            thread.start();
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)){
            Toast.makeText(getContext(), R.string.study_for_permission_requested, Toast.LENGTH_LONG).show();
        }else{
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }

    private void shareReportPdf(){

        File pdfFile = createReportPDF();
        final Uri uri = FileProvider.getUriForFile(requireContext(), URI_PROVIDER_AUTHORITY, pdfFile);

        requireActivity().runOnUiThread(() -> {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Relatório de"+reportEntity.getDataToShow());
            sendIntent.putExtra(Intent.EXTRA_STREAM, uri);
            sendIntent.putExtra(Intent.EXTRA_TITLE, "Relatório de Célula");
            sendIntent.setType("application/pdf");

            Intent share = Intent.createChooser(sendIntent, getString(R.string.sharing_title));

            progressBar.setVisibility(View.GONE);
            startActivity(share);
        });
    }

    @NonNull
    private File createReportPDF(){

        int width = 1000;
        int height = 1524;

        // create a new document
        PdfDocument document = new PdfDocument();

        // create a page description
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(width, height, 1).create();

        // start a page
        PdfDocument.Page page = document.startPage(pageInfo);

        // draw something on the page
        //View content = viewRoot.findViewById(R.id.layout_report_details);

        View contentView = getContentView(width, height);
        Canvas canvas = page.getCanvas();

        //draw border in page
        Paint rect = new Paint();
        contentView.draw(canvas);
        // finish the page
        document.finishPage(page);

        //create a file do write pdf
        File path  = requireContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        String fileName = reportEntity.getNomeCelula()+reportEntity.getDataReuniao()+".pdf";
        File file = new File(path, fileName);

        try {
            OutputStream outputStream = new FileOutputStream(file);
            document.writeTo(outputStream);

            requireActivity().runOnUiThread(() ->
                    Toast.makeText(requireContext(), getString(R.string.pdf_generated) + file.getPath(), Toast.LENGTH_SHORT).show()
            );
            // write the document content
        } catch (IOException e) {
            e.printStackTrace();
        }

        // close the document
        document.close();

        return file;
    }

    @NonNull
    private View getContentView(int width, int height){

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
        content.measure(width, height);
        content.layout(0, 0, width, height);

        return content;
    }
}