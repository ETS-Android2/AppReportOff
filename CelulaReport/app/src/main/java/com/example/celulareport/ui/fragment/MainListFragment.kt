package com.example.celulareport.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.celulareport.R
import com.example.celulareport.ui.Constraint
import com.example.celulareport.ui.adapter.LineAdapter
import com.example.celulareport.ui.adapter.LineAdapter.OnLineClickListerner
import com.example.celulareport.ui.fragment.ReportsListFragment.Companion.newInstance
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

class MainListFragment : Fragment(), OnLineClickListerner {
    var mLayoutManager: LinearLayoutManager? = null
    private var Months: ArrayList<String>? = null
    var mToolbar: Toolbar? = null
    var mCollapsing: CollapsingToolbarLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Initialize data set
        initDataSet()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewRoot = inflater.inflate(R.layout.fragment_main_list, container, false)
        viewRoot.tag = TAG


        //setup toolbar to change color when expanding or collapsing
        SetupToolbar(viewRoot)

        //getting floating button
        val mFButtonAddReport: FloatingActionButton = viewRoot.findViewById(R.id.fb_add_report)
        onCLickFloatingButton(mFButtonAddReport)
        //inflate recycleView
        SetupRecycler(viewRoot)
        return viewRoot
    }

    //Setup toolbar and collapsing bar
    private fun SetupToolbar(v: View) {
        mToolbar = v.findViewById<View>(R.id.main_toolbar) as Toolbar
        mToolbar!!.setTitle(R.string.main_title)
        mToolbar!!.setLogo(R.drawable.ic_report)
        (activity as AppCompatActivity?)!!.setSupportActionBar(mToolbar)
        mCollapsing = v.findViewById<View>(R.id.CollapsingToolbar) as CollapsingToolbarLayout
        mCollapsing!!.setCollapsedTitleTextAppearance(R.style.Toolbar_TitleText_Collapsed_main)
        mCollapsing!!.setExpandedTitleTextAppearance(R.style.Toolbar_TitleText_Expanded_main)
        mCollapsing!!.expandedTitleGravity = Gravity.CENTER
    }

    //RecycleView settings
    private fun SetupRecycler(viewRoot: View) {
        val mRecycler: RecyclerView = viewRoot.findViewById(R.id.recycleView_months)
        /* setting layout manager to the list */mLayoutManager = LinearLayoutManager(
            activity
        )
        mRecycler.layoutManager = mLayoutManager

        //Objects to be adding in the list

        //Adding adapter that will be annexe the object in the list
        val mAdapter = LineAdapter(Months, this)
        mRecycler.adapter = mAdapter

        //adding divisor between lists
        mRecycler.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
    }

    //Init data set used
    private fun initDataSet() {
        Months = ArrayList()

        //Adding months
        Months!!.add(Constraint.MONTHS[0])
        Months!!.add(Constraint.MONTHS[1])
        Months!!.add(Constraint.MONTHS[2])
        Months!!.add(Constraint.MONTHS[3])
        Months!!.add(Constraint.MONTHS[4])
        Months!!.add(Constraint.MONTHS[5])
        Months!!.add(Constraint.MONTHS[6])
        Months!!.add(Constraint.MONTHS[7])
        Months!!.add(Constraint.MONTHS[8])
        Months!!.add(Constraint.MONTHS[9])
        Months!!.add(Constraint.MONTHS[10])
        Months!!.add(Constraint.MONTHS[11])
    }

    override fun onLineClick(position: Int, textMonth: TextView) {
        Log.d(TAG, "OnLineClick: clicked,line $position")
        //make transition between Activity
        val fragment = newInstance(Constraint.MONTHS[position])
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

    fun onCLickFloatingButton(mFButtonAddReport: FloatingActionButton) {
        mFButtonAddReport.setOnClickListener { v: View? ->
            val fragment = AddReportFragment()
            activity
                .getSupportFragmentManager()
                .beginTransaction().setReorderingAllowed(true)
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
        }
    }

    companion object {
        private const val TAG = "MonthRecyclerFragment"
    }
}