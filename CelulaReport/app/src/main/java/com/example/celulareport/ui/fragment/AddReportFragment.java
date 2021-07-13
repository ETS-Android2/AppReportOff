package com.example.celulareport.ui.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.celulareport.R;
import com.example.celulareport.db.model.ReportEntity;
import com.example.celulareport.util.MaskEditText;
import com.example.celulareport.viewmodel.ReportsViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

public class AddReportFragment extends Fragment {

    private static final String TAG = "ReportPageActivity";

    private Toolbar mToolbar;

    //Variables related to reports attributes
    private TextInputEditText celulaTextInput;
    private TextInputEditText liderTextInput;
    private TextInputEditText coliderTextInput;
    private TextInputEditText anfitriaoTextInput;
    private TextInputEditText dateTextInput;
    private TextInputEditText membrosTextInput;
    private TextInputEditText ofertaTextInput;
    private TextInputEditText estudoTextInput;
    private TextInputEditText visitantesTextInput;

    private TextInputEditText commitsTextInput;

    //date
    private int mDay;
    private int mMonth;
    private int mYear;

    private ReportsViewModel mVieModel;

    protected long reportId= -1;

    public static String ARG_REPORT = "com.example.celulareport.Report";

    public static AddReportFragment newInstance(long reportId) {
        AddReportFragment fragment = new AddReportFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_REPORT, reportId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if(getArguments() != null){
            reportId = getArguments().getLong(ARG_REPORT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_report, container, false);
        //Initializing with current date
        Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Initializing input edit texts in of the report
        initViews(view);

        //Getting toolbar, adding logo icon and setting as actionbar
        SetupToolbar();

        mVieModel = new ViewModelProvider(this).get(ReportsViewModel.class);

        if(reportId > -1){
            initUpdateReport();
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.add_report_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.save_icon){
            saveReport();
        }
        return super.onOptionsItemSelected(item);
    }


    private void SetupToolbar(){
        ((AppCompatActivity)requireActivity()).setSupportActionBar(mToolbar);
    }

    //Initializing Views objects and Set listeners
    private void initViews(@NonNull View v){
        mToolbar = v.findViewById(R.id.report_pg_tollbar_id);
        celulaTextInput = v.findViewById(R.id.nome_celula_edit);

        liderTextInput = v.findViewById(R.id.nome_lider_edit);

        coliderTextInput = v.findViewById(R.id.nome_colider_edit);

        anfitriaoTextInput = v.findViewById(R.id.nome_anfitriao_edit);


        dateTextInput = v.findViewById(R.id.date_reuniao_edit);
        TextInputLayout dateTextInputLayout = v.findViewById(R.id.layout_data_reuniao);

        membrosTextInput = v.findViewById(R.id.n_membros_edit);

        visitantesTextInput = v.findViewById(R.id.n_visitantes_edit);

        ofertaTextInput = v.findViewById(R.id.oferta_edit);

        estudoTextInput = v.findViewById(R.id.estudo_edit);

        commitsTextInput = v.findViewById(R.id.commits_edit);

        //Adding masks
        dateTextInput.addTextChangedListener(MaskEditText.mask(dateTextInput, MaskEditText.FORMAT_DATE));
        //adding money mask on editText
        ofertaTextInput.addTextChangedListener(MaskEditText.mask(ofertaTextInput));

        //Enable scrolling on input Edit text
        celulaTextInput.setHorizontallyScrolling(true);
        celulaTextInput.setNestedScrollingEnabled(true);

        liderTextInput.setHorizontallyScrolling(true);
        liderTextInput.setNestedScrollingEnabled(true);

        coliderTextInput.setHorizontallyScrolling(true);
        coliderTextInput.setNestedScrollingEnabled(true);

        anfitriaoTextInput.setHorizontallyScrolling(true);
        anfitriaoTextInput.setNestedScrollingEnabled(true);

        membrosTextInput.setHorizontallyScrolling(true);
        membrosTextInput.setNestedScrollingEnabled(true);

        visitantesTextInput.setHorizontallyScrolling(true);
        visitantesTextInput.setNestedScrollingEnabled(true);

        ofertaTextInput.setHorizontallyScrolling(true);
        ofertaTextInput.setNestedScrollingEnabled(true);

        estudoTextInput.setHorizontallyScrolling(true);
        estudoTextInput.setNestedScrollingEnabled(true);

        commitsTextInput.setVerticalScrollBarEnabled(true);
        commitsTextInput.setNestedScrollingEnabled(true);
        //Click date icon dateTextField
        dateTextInputLayout.setEndIconOnClickListener(v1 -> {
            //Focus on EditText before show datePickerDialog
            //show datePicker to choose the date of meeting
            chooseDate(dateTextInput);
        });
    }

    private void initUpdateReport() {
        mVieModel.reportSelected(reportId).observe(getViewLifecycleOwner(), entity ->{
            celulaTextInput.setText(entity.getNomeCelula());
            liderTextInput.setText(entity.getNomeLider());
            coliderTextInput.setText(entity.getNomeColider());
            anfitriaoTextInput.setText(entity.getNomeAnfitriao());
            membrosTextInput.setText(entity.getNumMembros());
            visitantesTextInput.setText(entity.getNumVisitantes());
            dateTextInput.setText(entity.getDataToShow());
            estudoTextInput.setText(entity.getEstudo());
            ofertaTextInput.setText(entity.getOferta());
            commitsTextInput.setText(entity.getComentarios());
        });
    }

    //Get date and put it in a EditText of 'oferta'
    private DatePickerDialog.OnDateSetListener getDateSetListener(TextInputEditText inputEditText){
        return (view, year, month, dayOfMonth) -> {
            //update current date to choose date
            mYear = year;
            mMonth = month;
            mDay = dayOfMonth;

            String strMonth = "";
            String strDayOfMonth = "";

            //adding zero in left from unit when it needed
            month = month+1;
            if(month < 10){
                strMonth = "0"+month;
            }else
                strMonth = String.valueOf(month);

            if(dayOfMonth < 10){
                strDayOfMonth = "0"+dayOfMonth;
            }else
                strDayOfMonth = String.valueOf(dayOfMonth);

            //clear text before update
            inputEditText.setText("");
            //update text to date choose
            inputEditText.setText(strDayOfMonth + strMonth + year);
        };
    }

    //Validating filled report
    private boolean IsValidReport(){
        //verify if this editTexts are null
        boolean noValidCelula = TextUtils.isEmpty(celulaTextInput.getText());
        boolean noValidLider = TextUtils.isEmpty(liderTextInput.getText());
        boolean noValidColider = TextUtils.isEmpty(coliderTextInput.getText());
        boolean noValidAnfitriao = TextUtils.isEmpty(anfitriaoTextInput.getText());
        boolean noValidDate = TextUtils.isEmpty(dateTextInput.getText());
        boolean noValidMembros = TextUtils.isEmpty(membrosTextInput.getText());
        boolean noValidVisitantes = TextUtils.isEmpty(visitantesTextInput.getText());
        boolean noValidOferta = TextUtils.isEmpty(ofertaTextInput.getText());

        //True when all inout is valid
        boolean isValid = true;

        if (noValidCelula){
            celulaTextInput.setError(getString(R.string.message_empty_edit));
            isValid = false;
        }
        if (noValidLider){
            liderTextInput.setError(getString(R.string.message_empty_edit));
            isValid = false;
        }
        if (noValidColider){
            coliderTextInput.setError(getString(R.string.message_empty_edit));
            isValid = false;
        }
        if (noValidAnfitriao){
            anfitriaoTextInput.setError(getString(R.string.message_empty_edit));
            isValid = false;
        }
        if (noValidDate){
            dateTextInput.setError(getString(R.string.message_empty_edit));
            isValid = false;
        }
        if (noValidMembros){
            membrosTextInput.setError(getString(R.string.message_empty_edit));
            isValid = false;
        }
        if (noValidVisitantes){
            visitantesTextInput.setError(getString(R.string.message_empty_edit));
            isValid = false;
        }
        if (noValidOferta){
            ofertaTextInput.setError(getString(R.string.message_empty_edit));
            isValid = false;
        }

        return isValid;
    }

    //save report information in the the database
    private void saveReport(){

        String celula;
        String lider;
        String colider;
        String anfitriao;
        String data;
        String membros;
        String visitantes;
        String oferta;
        String estudo;
        String commenits;

        if (IsValidReport()){

            celula = requireNonNull(celulaTextInput.getText()).toString();
            lider = requireNonNull(liderTextInput.getText()).toString();
            colider = requireNonNull(coliderTextInput.getText()).toString();
            anfitriao = requireNonNull(anfitriaoTextInput.getText()).toString();
            data = requireNonNull(dateTextInput.getText()).toString();
            membros = requireNonNull(membrosTextInput.getText()).toString();
            visitantes = requireNonNull(visitantesTextInput.getText()).toString();
            oferta = requireNonNull(ofertaTextInput.getText()).toString();
            estudo = requireNonNull(estudoTextInput.getText()).toString();
            commenits = requireNonNull(commitsTextInput.getText()).toString().isEmpty()? "" : commitsTextInput.getText().toString();

            ReportEntity reportEntity = new ReportEntity();

            reportEntity.setNomeCelula(celula);
            reportEntity.setNomeLider(lider);
            reportEntity.setNomeColider(colider);
            reportEntity.setNomeAnfitriao(anfitriao);
            reportEntity.setDataReuniao(data);
            reportEntity.setNumMembros(membros);
            reportEntity.setNumVisitantes(visitantes);
            reportEntity.setOferta(oferta);
            reportEntity.setEstudo(estudo);
            reportEntity.setComentarios(commenits);

            if(reportId  > -1){
                //identity of entity will be updated
                reportEntity.setId(reportId);
                mVieModel.update(reportEntity);
            }else {
                mVieModel.Insert(reportEntity);
            }

            Toast.makeText(requireContext(), R.string.report_saved, Toast.LENGTH_SHORT).show();
            //finish fragment
            FragmentManager fragmentManager = requireActivity()
                    .getSupportFragmentManager();
            fragmentManager.popBackStackImmediate();


        }else {
            Toast.makeText(getContext(), getString(R.string.report_error_save), Toast.LENGTH_SHORT).show();
        }
    }

    //Show datePicker and Choose Date through it
    private void chooseDate(TextInputEditText dateTextField){

        //get DateSetListener
        //Initializing listener to get date and put it in editText
        DatePickerDialog.OnDateSetListener mDateSetListener = getDateSetListener(dateTextField);

        DatePickerDialog dialogPicker = new DatePickerDialog(requireContext(),
                AlertDialog.THEME_HOLO_DARK,
                mDateSetListener,
                mYear,
                mMonth,
                mDay);
        //AlertDialog.THEME_HOLO_DARK
        dialogPicker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //show date dialog in the view
        dialogPicker.show();
    }
}