package com.example.celulareport.ui.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.celulareport.R;
import com.example.celulareport.db.model.ReportEntity;
import com.example.celulareport.util.MaskEditText;
import com.example.celulareport.viewmodel.ReportListViewModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

public class AddReportFragment extends Fragment {

    private static final String TAG = "ReportPageActivity";
    public static final String EXTRA_CELULA = "com.example.celulareport.CELULA";
    public static final String EXTRA_LIDER = "com.example.celulareport.LIDER";
    public static final String EXTRA_COLIDER = "com.example.celulareport.COLIDER";
    public static final String EXTRA_ANFITRIAO = "com.example.celulareport.ANFITRIAO";
    public static final String EXTRA_DATA = "com.example.celulareport.DATA";
    public static final String EXTRA_MEMBROS = "com.example.celulareport.MEMBROS";
    public static final String EXTRA_VISITANTES = "com.example.celulareport.VISITANTES";
    public static final String EXTRA_OFERTA = "com.example.celulareport.OFERTA";
    public static final String EXTRA_COMENTARIOS = "com.example.celulareport.COMENTARIOS";

    private Toolbar mToolbar;

    //Buttom to save reports information filled in
    private Button saveButton;

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

    private TextInputLayout dateTextInputLayout;

    private TextInputEditText commitsTextInput;

    //date
    private int mDay;
    private int mMonth;
    private int mYear;

    ReportListViewModel mVieModel;
    public AddReportFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_report, container, false);

        //Initializing input edit texts in of the report
        InitializingViews(v);

        //Getting toolbar, adding logo icon and setting as actionbar
        SetupToolbar();

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

        mVieModel = new ViewModelProvider(requireActivity()).get(ReportListViewModel.class);
    }

    private void SetupToolbar(){
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
    }

    //Initializing Views objects and Set listeners
    private void InitializingViews(View v){
        mToolbar = (Toolbar) v.findViewById(R.id.report_pg_tollbar_id);
        celulaTextInput = v.findViewById(R.id.nome_celula_edit);

        liderTextInput = v.findViewById(R.id.nome_lider_edit);

        coliderTextInput = v.findViewById(R.id.nome_colider_edit);

        anfitriaoTextInput = v.findViewById(R.id.nome_anfitriao_edit);


        dateTextInput = v.findViewById(R.id.date_reuniao_edit);
        dateTextInputLayout = v.findViewById(R.id.layout_data_reuniao);

        membrosTextInput = v.findViewById(R.id.n_membros_edit);

        visitantesTextInput = v.findViewById(R.id.n_visitantes_edit);

        ofertaTextInput = v.findViewById(R.id.oferta_edit);

        estudoTextInput = v.findViewById(R.id.estudo_edit);

        commitsTextInput = v.findViewById(R.id.commits_edit);

        saveButton = v.findViewById(R.id.bt_salvar);

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
        dateTextInputLayout.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Focus on EditText before show datePickerDialog
                //show datePicker to choose the date of meeting
                chooseDate(dateTextInput);
            }
        });

        //When click in save button
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveReport();

            }
        });
    }

    //Get date and put it in a EditText of 'oferta'
    private DatePickerDialog.OnDateSetListener getDateSetListener(TextInputEditText inputEditText){
        DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
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
            }
        };

        return mDateSetListener;
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
    private void SaveReport(){

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

        //Intent to send to another activity
        Intent replyIntent = new Intent();
        if (IsValidReport()){

            celula = celulaTextInput.getText().toString();
            lider = liderTextInput.getText().toString();
            colider = coliderTextInput.getText().toString();
            anfitriao = anfitriaoTextInput.getText().toString();
            data = dateTextInput.getText().toString();
            membros = membrosTextInput.getText().toString();
            visitantes = visitantesTextInput.getText().toString();
            oferta = ofertaTextInput.getText().toString();
            estudo = estudoTextInput.getText().toString();
            commenits = commitsTextInput.getText().toString().isEmpty()? "" : commitsTextInput.getText().toString();

            ReportEntity reportEntity = new ReportEntity();

            reportEntity.setNomeCelula(celula);
            reportEntity.setNomeLider(lider);
            reportEntity.setNomeColider(colider);
            reportEntity.setNomeAnfitriao(anfitriao);
            reportEntity.setDataReuniao(data);
            reportEntity.setNumMembros(membros);
            reportEntity.setNumVisitantes(visitantes);
            reportEntity.setOferta(oferta);
            //reportEntity.setEstudo(estudo);
            reportEntity.setComentarios(commenits);

            mVieModel.Insert(reportEntity);

            Toast.makeText(getContext(), R.string.report_saved, Toast.LENGTH_SHORT);
            //finish fragment
            FragmentManager fragmentManager = getActivity()
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

        DatePickerDialog dialogPicker = new DatePickerDialog(getContext(),
                AlertDialog.THEME_HOLO_DARK,
                mDateSetListener,
                mYear,
                mMonth,
                mDay);

        dialogPicker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //show date dialog in the view
        dialogPicker.show();
    }


}