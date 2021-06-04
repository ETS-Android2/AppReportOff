package com.example.celulareport.ui.fragment

import com.example.celulareport.db.model.ReportEntity.nomeCelula
import com.example.celulareport.db.model.ReportEntity.nomeLider
import com.example.celulareport.db.model.ReportEntity.nomeColider
import com.example.celulareport.db.model.ReportEntity.nomeAnfitriao
import com.example.celulareport.db.model.ReportEntity.numMembros
import com.example.celulareport.db.model.ReportEntity.numVisitantes
import com.example.celulareport.db.model.ReportEntity.oferta
import com.example.celulareport.db.model.ReportEntity.comentarios
import com.example.celulareport.viewmodel.ReportDetailsViewModel
import android.widget.TextView
import com.google.android.material.appbar.CollapsingToolbarLayout
import android.os.Bundle
import android.view.*
import com.example.celulareport.ui.fragment.ReportDetailsFragment
import android.widget.Toast
import com.example.celulareport.R
import androidx.lifecycle.ViewModelProvider
import com.example.celulareport.db.model.ReportEntity
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment

class ReportDetailsFragment : Fragment() {
    //ViewModel to get report selected
    private var mViewModel: ReportDetailsViewModel? = null

    //Views objects
    var mLiderText: TextView? = null
    var mColiderText: TextView? = null
    var mAnfitriaoText: TextView? = null
    var mDataText: TextView? = null
    var mMembrosText: TextView? = null
    var mVisitantesText: TextView? = null
    var mOfertaText: TextView? = null
    var mEstudoText: TextView? = null

    //Toolbar
    var mToolbar: Toolbar? = null
    var mCollapsing: CollapsingToolbarLayout? = null

    //Id of report
    private var mReportID: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        if (arguments != null) {
            mReportID = arguments!!.getLong(REPORT_ID)
        } else {
            Toast.makeText(context, "Relório não existe no banco de dados ", Toast.LENGTH_LONG)
                .show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_report_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel = ViewModelProvider(requireActivity()).get(
            ReportDetailsViewModel::class.java
        )
        registerReportSelected(mViewModel!!)

        //Text Views Contain reports details
        getViewObjects(view)

        //Setup toolbar
        SetupToolbar(view)
    }

    fun getViewObjects(v: View) {
        mLiderText = v.findViewById(R.id.txt_lider)
        mColiderText = v.findViewById(R.id.txt_colider)
        mAnfitriaoText = v.findViewById(R.id.txt_anfitriao)
        mDataText = v.findViewById(R.id.txt_data)
        mMembrosText = v.findViewById(R.id.txt_membros)
        mVisitantesText = v.findViewById(R.id.txt_visitantes)
        mOfertaText = v.findViewById(R.id.txt_oferta)
        mEstudoText = v.findViewById(R.id.txt_estudo)
        mToolbar = v.findViewById(R.id.toolbar_details)
        mCollapsing = v.findViewById(R.id.collapsing_details)
    }

    fun registerReportSelected(viewModel: ReportDetailsViewModel) {
        viewModel.ReportSelected(mReportID).observe(viewLifecycleOwner) { reportEntity ->
            //Adding report data in the view
            addingReportDetails(reportEntity)
        }
    }

    fun addingReportDetails(mReport: ReportEntity) {
        val celula = mReport.nomeCelula
        mToolbar!!.title = celula
        mLiderText!!.text = mReport.nomeLider
        mColiderText!!.text = mReport.nomeColider
        mAnfitriaoText!!.text = mReport.nomeAnfitriao
        mDataText.setText(mReport.getDataToShow())
        mMembrosText!!.setText(mReport.numMembros)
        mVisitantesText!!.setText(mReport.numVisitantes)
        mOfertaText.setText(mReport.oferta)
        mEstudoText!!.text = mReport.comentarios
    }

    private fun SetupToolbar(v: View) {
        mToolbar!!.setLogo(R.drawable.ic_report)
        (activity as AppCompatActivity?)!!.setSupportActionBar(mToolbar)
        mCollapsing!!.setCollapsedTitleTextAppearance(R.style.Toolbar_TitleText_Collapsed_details)
        mCollapsing!!.setExpandedTitleTextAppearance(R.style.Toolbar_TitleText_Expanded_details)
        mCollapsing!!.expandedTitleGravity = Gravity.BOTTOM
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.report_details_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    var isFavorite = false
    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.ic_favorite_details -> {
                Toast.makeText(context, "Favorite action clicked", Toast.LENGTH_SHORT).show()
                isFavorite = !isFavorite
                item.setIcon(if (isFavorite) R.drawable.ic_favorite else R.drawable.ic_favorite_border)
                return true
            }
            R.id.ic_details_share -> {
                Toast.makeText(context, "Shared icon clicked", Toast.LENGTH_SHORT).show()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private const val REPORT_ID = "REPORT_ID"
        fun newInstance(reportID: Long): ReportDetailsFragment {
            val args = Bundle()
            args.putLong(REPORT_ID, reportID)
            val fragment = ReportDetailsFragment()
            fragment.arguments = args
            return fragment
        }
    }
}