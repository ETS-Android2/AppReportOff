package com.example.celulareport.ui.fragment

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.celulareport.R
import com.example.celulareport.db.model.ReportEntity
import com.example.celulareport.ui.MaskEditText
import com.example.celulareport.viewmodel.ReportListViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.util.*

class AddReportFragment : Fragment() {
    private var mToolbar: Toolbar? = null

    //Buttom to save reports information filled in
    private var saveButton: Button? = null

    //Variables related to reports attributes
    private var celulaTextInput: TextInputEditText? = null
    private var liderTextInput: TextInputEditText? = null
    private var coliderTextInput: TextInputEditText? = null
    private var anfitriaoTextInput: TextInputEditText? = null
    private var dateTextInput: TextInputEditText? = null
    private var membrosTextInput: TextInputEditText? = null
    private var ofertaTextInput: TextInputEditText? = null
    private var visitantesTextInput: TextInputEditText? = null
    private var dateTextInputLayout: TextInputLayout? = null
    private var commitsTextInput: TextInputEditText? = null

    //date
    private var mDay = 0
    private var mMonth = 0
    private var mYear = 0
    var mVieModel: ReportListViewModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_add_report, container, false)

        //Initializing input edit texts in of the report
        InitializingViews(v)

        //Getting toolbar, adding logo icon and setting as actionbar
        SetupToolbar()

        //Initializing with current date
        val calendar = Calendar.getInstance()
        mYear = calendar[Calendar.YEAR]
        mMonth = calendar[Calendar.MONTH]
        mDay = calendar[Calendar.DAY_OF_MONTH]
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mVieModel = ViewModelProvider(requireActivity()).get(
            ReportListViewModel::class.java
        )
    }

    private fun SetupToolbar() {
        (activity as AppCompatActivity?)!!.setSupportActionBar(mToolbar)
    }

    //Initializing Views objects and Set listeners
    private fun InitializingViews(v: View) {
        mToolbar = v.findViewById<View>(R.id.report_pg_tollbar_id) as Toolbar
        celulaTextInput = v.findViewById(R.id.nome_celula_edit)
        liderTextInput = v.findViewById(R.id.nome_lider_edit)
        coliderTextInput = v.findViewById(R.id.nome_colider_edit)
        anfitriaoTextInput = v.findViewById(R.id.nome_anfitriao_edit)
        dateTextInput = v.findViewById(R.id.date_reuniao_edit)
        dateTextInputLayout = v.findViewById(R.id.layout_data_reuniao)
        membrosTextInput = v.findViewById(R.id.n_membros_edit)
        visitantesTextInput = v.findViewById(R.id.n_visitantes_edit)
        ofertaTextInput = v.findViewById(R.id.oferta_edit)
        commitsTextInput = v.findViewById(R.id.estudo_edit)
        saveButton = v.findViewById(R.id.bt_salvar)


        //Adding masks
        dateTextInput.addTextChangedListener(
            MaskEditText.mask(
                dateTextInput,
                MaskEditText.FORMAT_DATE
            )
        )
        //adding money mask on editText
        ofertaTextInput.addTextChangedListener(MaskEditText.mask(ofertaTextInput))

        //Enable scrolling on input Edit text
        celulaTextInput.setHorizontallyScrolling(true)
        celulaTextInput.setNestedScrollingEnabled(true)
        liderTextInput.setHorizontallyScrolling(true)
        liderTextInput.setNestedScrollingEnabled(true)
        coliderTextInput.setHorizontallyScrolling(true)
        coliderTextInput.setNestedScrollingEnabled(true)
        anfitriaoTextInput.setHorizontallyScrolling(true)
        anfitriaoTextInput.setNestedScrollingEnabled(true)
        membrosTextInput.setHorizontallyScrolling(true)
        membrosTextInput.setNestedScrollingEnabled(true)
        visitantesTextInput.setHorizontallyScrolling(true)
        visitantesTextInput.setNestedScrollingEnabled(true)
        ofertaTextInput.setHorizontallyScrolling(true)
        ofertaTextInput.setNestedScrollingEnabled(true)
        commitsTextInput.setVerticalScrollBarEnabled(true)
        commitsTextInput.setNestedScrollingEnabled(true)

        //Click date icon dateTextField
        dateTextInputLayout.setEndIconOnClickListener(View.OnClickListener { //Focus on EditText before show datePickerDialog
            //show datePicker to choose the date of meeting
            chooseDate(dateTextInput)
        })

        //When click in save button
        saveButton.setOnClickListener(View.OnClickListener { SaveReport() })
    }

    //Get date and put it in a EditText of 'oferta'
    private fun getDateSetListener(inputEditText: TextInputEditText?): OnDateSetListener {
        return OnDateSetListener { view, year, month, dayOfMonth -> //update current date to choose date
            var month = month
            mYear = year
            mMonth = month
            mDay = dayOfMonth
            var strMonth = ""
            var strDayOfMonth = ""

            //adding zero in left from unit when it needed
            month = month + 1
            strMonth = if (month < 10) {
                "0$month"
            } else month.toString()
            strDayOfMonth = if (dayOfMonth < 10) {
                "0$dayOfMonth"
            } else dayOfMonth.toString()

            //clear text before update
            inputEditText!!.setText("")
            //update text to date choose
            inputEditText.setText(strDayOfMonth + strMonth + year)
        }
    }

    //Validating filled report
    private fun IsValidReport(): Boolean {
        //verify if this editTexts are null
        val noValidCelula = TextUtils.isEmpty(celulaTextInput!!.text)
        val noValidLider = TextUtils.isEmpty(liderTextInput!!.text)
        val noValidColider = TextUtils.isEmpty(coliderTextInput!!.text)
        val noValidAnfitriao = TextUtils.isEmpty(anfitriaoTextInput!!.text)
        val noValidDate = TextUtils.isEmpty(dateTextInput!!.text)
        val noValidMembros = TextUtils.isEmpty(membrosTextInput!!.text)
        val noValidVisitantes = TextUtils.isEmpty(visitantesTextInput!!.text)
        val noValidOferta = TextUtils.isEmpty(ofertaTextInput!!.text)

        //True when all inout is valid
        var isValid = true
        if (noValidCelula) {
            celulaTextInput!!.error = getString(R.string.message_empty_edit)
            isValid = false
        }
        if (noValidLider) {
            liderTextInput!!.error = getString(R.string.message_empty_edit)
            isValid = false
        }
        if (noValidColider) {
            coliderTextInput!!.error = getString(R.string.message_empty_edit)
            isValid = false
        }
        if (noValidAnfitriao) {
            anfitriaoTextInput!!.error = getString(R.string.message_empty_edit)
            isValid = false
        }
        if (noValidDate) {
            dateTextInput!!.error = getString(R.string.message_empty_edit)
            isValid = false
        }
        if (noValidMembros) {
            membrosTextInput!!.error = getString(R.string.message_empty_edit)
            isValid = false
        }
        if (noValidVisitantes) {
            visitantesTextInput!!.error = getString(R.string.message_empty_edit)
            isValid = false
        }
        if (noValidOferta) {
            ofertaTextInput!!.error = getString(R.string.message_empty_edit)
            isValid = false
        }
        return isValid
    }

    //save report information in the the database
    private fun SaveReport() {
        val celula: String
        val lider: String
        val colider: String
        val anfitriao: String
        val data: String
        val membros: String
        val visitantes: String
        val oferta: String
        val commenits: String

        //Intent to send to another activity
        val replyIntent = Intent()
        if (IsValidReport()) {
            celula = celulaTextInput!!.text.toString()
            lider = liderTextInput!!.text.toString()
            colider = coliderTextInput!!.text.toString()
            anfitriao = anfitriaoTextInput!!.text.toString()
            data = dateTextInput!!.text.toString()
            membros = membrosTextInput!!.text.toString()
            visitantes = visitantesTextInput!!.text.toString()
            oferta = ofertaTextInput!!.text.toString()
            commenits = if (commitsTextInput!!.text.toString()
                    .isEmpty()
            ) "" else commitsTextInput!!.text.toString()
            val reportEntity = ReportEntity()
            reportEntity.nomeCelula = celula
            reportEntity.nomeLider = lider
            reportEntity.nomeColider = colider
            reportEntity.nomeAnfitriao = anfitriao
            reportEntity.setDataReuniao(data)
            reportEntity.numMembros = membros
            reportEntity.numVisitantes = visitantes
            reportEntity.oferta = oferta
            reportEntity.comentarios = commenits
            mVieModel.Insert(reportEntity)
            Toast.makeText(context, R.string.report_saved, Toast.LENGTH_SHORT)
            //finish fragment
            val fragmentManager = activity
                .getSupportFragmentManager()
            fragmentManager.popBackStackImmediate()
        } else {
            Toast.makeText(context, getString(R.string.report_error_save), Toast.LENGTH_SHORT)
                .show()
        }
    }

    //Show datePicker and Choose Date through it
    private fun chooseDate(dateTextField: TextInputEditText?) {

        //get DateSetListener
        //Initializing listener to get date and put it in editText
        val mDateSetListener = getDateSetListener(dateTextField)
        val dialogPicker = DatePickerDialog(
            context!!,
            AlertDialog.THEME_HOLO_DARK,
            mDateSetListener,
            mYear,
            mMonth,
            mDay
        )
        dialogPicker.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        //show date dialog in the view
        dialogPicker.show()
    }

    companion object {
        private const val TAG = "ReportPageActivity"
        const val EXTRA_CELULA = "com.example.celulareport.CELULA"
        const val EXTRA_LIDER = "com.example.celulareport.LIDER"
        const val EXTRA_COLIDER = "com.example.celulareport.COLIDER"
        const val EXTRA_ANFITRIAO = "com.example.celulareport.ANFITRIAO"
        const val EXTRA_DATA = "com.example.celulareport.DATA"
        const val EXTRA_MEMBROS = "com.example.celulareport.MEMBROS"
        const val EXTRA_VISITANTES = "com.example.celulareport.VISITANTES"
        const val EXTRA_OFERTA = "com.example.celulareport.OFERTA"
        const val EXTRA_COMENTARIOS = "com.example.celulareport.COMENTARIOS"
    }
}