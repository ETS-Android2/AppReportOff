package com.example.celulareport.ui.fragment

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.celulareport.R
import com.example.celulareport.db.model.ReportCard
import com.example.celulareport.ui.Constraint
import com.example.celulareport.ui.adapter.CardAdapter
import com.example.celulareport.ui.adapter.CardAdapter.OnCardClickListener
import com.example.celulareport.ui.adapter.CardAdapter.OnCardLongCLickListener
import com.example.celulareport.viewmodel.ReportListViewModel

/**
 * [ReportsListFragment] is fragment class to show up report card list
 *
 *
 */
class ReportsListFragment : Fragment(), OnCardClickListener, OnCardLongCLickListener {
    private var mMonth: String? = null
    private var mRecycler: RecyclerView? = null
    private var mAdapter: CardAdapter? = null
    private var mToolbar: Toolbar? = null
    private var textTitle: TextView? = null
    private var mCardView: CardView? = null
    private var mViewModel: ReportListViewModel? = null

    // Get position item when long click is activate
    var longClickPosition = 0

    //Context menu mode
    private var mActionMode: ActionMode? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        if (arguments != null) {
            mMonth = arguments!!.getString(ARG_MONTH)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_reports_list, container, false)
        mRecycler = v.findViewById(R.id.rv_report_list)

        //Setup toolbar as actionbar
        SetupToolbar(mMonth, v)
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Create view model to share between fragments
        mViewModel = ViewModelProvider(requireActivity()).get(
            ReportListViewModel::class.java
        )

        //Observe if there're changes in reports Card list
        subscribeReportList(mViewModel!!, mMonth)

        //initializing Recyclerview
        SetupRecycler()
    }

    //Setup toolbar and collapsing bar
    @SuppressLint("RestrictedApi")
    private fun SetupToolbar(title: String?, v: View) {
        mToolbar = v.findViewById<View>(R.id.reports_toolbar) as Toolbar
        textTitle = v.findViewById<View>(R.id.month_title) as TextView
        textTitle!!.text = title
        mToolbar!!.setLogo(R.drawable.ic_report)
        (activity as AppCompatActivity?)!!.setSupportActionBar(mToolbar)
        //ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        //actionBar.setDisplayShowTitleEnabled(false);
        //actionBar.setDisplayHomeAsUpEnabled(true);
    }

    //Adding recycleView(list of reports)
    fun SetupRecycler() {
        val nColumn = 2
        val layoutManager = GridLayoutManager(context, nColumn)
        mRecycler!!.layoutManager = layoutManager
        mAdapter = CardAdapter(context, this, this)
        mRecycler!!.adapter = mAdapter
        //divisor between cards
        //mRecycler.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        //mRecycler.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL));
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
        val item = menu.findItem(R.id.ic_reports_search)
        val searchView = item.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                mAdapter!!.filter.filter(newText)
                return false
            }
        })
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.ic_add_report -> {
                //Toast.makeText(getContext(), "Click in add report icon!", Toast.LENGTH_SHORT).show();
                val fragment = AddReportFragment()
                activity
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .setReorderingAllowed(true)
                    .setCustomAnimations(
                        R.anim.enter_slide_up,
                        R.anim.exit_fade_freeze,
                        R.anim.enter_fade_freeze,
                        R.anim.exit_slide_down
                    )
                    .replace(R.id.main_container, fragment, fragment.javaClass.name)
                    .addToBackStack(null)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()
                return true
            }
            else -> {
            }
        }
        return false
    }

    // menu context when we have a long click in the card
    val actionModeCallBack: ActionMode.Callback
        get() = object : ActionMode.Callback {
            override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
                mode.menuInflater.inflate(R.menu.card_context_menu, menu)
                mode.title = "opções"
                return true
            }

            override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
                return false
            }

            override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
                return when (item.itemId) {
                    R.id.ic_delete_card -> {
                        val id = mAdapter!!.getReportId(longClickPosition)
                        mViewModel.deleteById(id)
                        mode.finish()
                        true
                    }
                    R.id.ic_share_card -> {
                        Toast.makeText(
                            context,
                            "Shared icon is still Disabled.",
                            Toast.LENGTH_LONG
                        ).show()
                        mode.finish()
                        true
                    }
                    else -> false
                }
            }

            override fun onDestroyActionMode(mode: ActionMode) {
                mCardView!!.setCardBackgroundColor(Color.parseColor("#455A64"))
                mCardView = null
                mActionMode = null
            }
        }

    override fun onCardClick(position: Int, mCardView: CardView) {
        if (mActionMode != null) {
            mActionMode!!.finish()
        } else {
            //Toast.makeText(getContext(), "Clicked item " + position, Toast.LENGTH_SHORT).show();
            val reportID: Long
            reportID = mAdapter!!.getReportId(position)
            val fragment = ReportDetailsFragment.newInstance(reportID)
            activity
                .getSupportFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .setCustomAnimations(
                    R.anim.enter_fade_in,
                    R.anim.exit_fade_in,
                    R.anim.enter_fade_in,
                    R.anim.exit_fade_in
                )
                .replace(R.id.main_container, fragment, fragment.javaClass.name)
                .addToBackStack(null)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit()
        }
    }

    @SuppressLint("ResourceAsColor")
    override fun onCardLongClick(position: Int, mCardView: CardView): Boolean {
        if (mActionMode == null) {

            //trigger menu context
            val mCallback = actionModeCallBack
            mActionMode = (activity as AppCompatActivity?)!!.startSupportActionMode(mCallback)
            longClickPosition = position
            this.mCardView = mCardView
            this.mCardView!!.setCardBackgroundColor(Color.parseColor("#37474F"))
            this.mCardView!!.isSelected = true
        } else {
            mCardView.isSelected = false
            mActionMode!!.finish()
        }
        return true
    }

    //Verify the month to return number according to month to search in database
    private fun returnMonthValue(monthSelect: String?): String {
        var monthValue: String? = ""
        return when (monthSelect) {
            Constraint.STRING_JANUARY -> Constraint.VALUE_JANUARY.also { monthValue = it }
            Constraint.STRING_FEBRUARY -> Constraint.VALUE_FEBRUARY.also { monthValue = it }
            Constraint.STRING_MARCH -> Constraint.VALUE_MARCH.also { monthValue = it }
            Constraint.STRING_APRIL -> Constraint.VALUE_APRIL.also { monthValue = it }
            Constraint.STRING_MAY -> Constraint.VALUE_MAY.also { monthValue = it }
            Constraint.STRING_JUNE -> Constraint.VALUE_JUNE.also { monthValue = it }
            Constraint.STRING_JULY -> Constraint.VALUE_JULY.also { monthValue = it }
            Constraint.STRING_AUGUST -> Constraint.VALUE_AUGUST.also { monthValue = it }
            Constraint.STRING_SEPTEMBER -> Constraint.VALUE_SEPTEMBER.also { monthValue = it }
            Constraint.STRING_OCTOBER -> Constraint.VALUE_OCTOBER.also { monthValue = it }
            Constraint.STRING_NOVEMBER -> Constraint.VALUE_NOVEMBER.also { monthValue = it }
            Constraint.STRING_DECEMBER -> Constraint.VALUE_DECEMBER.also { monthValue = it }
            else -> throw NullPointerException("Value don't exist!")
        }
    }

    //observe changes in report cards list
    fun subscribeReportList(viewModel: ReportListViewModel, monthSelect: String?) {
        try {

            //Verify month value to query by it
            val monthValue = returnMonthValue(monthSelect)
            //select reports by month and observe changes in that
            viewModel.getReportsByMonth(monthValue).observe(
                viewLifecycleOwner,
                { reportCards: List<ReportCard?>? -> mAdapter!!.setReportCardsByMonth(reportCards) })
        } catch (exception: NullPointerException) {
            Toast.makeText(context, exception.message.toString(), Toast.LENGTH_LONG)
        }
    }

    companion object {
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private const val ARG_MONTH = "Month"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param selectMonth month selected in list of month of report.
         * @return A new instance of fragment ReportsListFragment.
         */
        @JvmStatic
        fun newInstance(selectMonth: String?): ReportsListFragment {
            val fragment = ReportsListFragment()
            val args = Bundle()
            args.putString(ARG_MONTH, selectMonth)
            fragment.arguments = args
            return fragment
        }
    }
}