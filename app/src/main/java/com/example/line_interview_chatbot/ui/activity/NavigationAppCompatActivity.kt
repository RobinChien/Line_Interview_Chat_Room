package com.example.line_interview_chatbot.ui.activity

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.line_interview_chatbot.R
import com.example.line_interview_chatbot.ui.fragment.BaseFragment
import com.ncapdevi.fragnav.FragNavController
import com.ncapdevi.fragnav.FragNavTransactionOptions
import com.ncapdevi.fragnav.tabhistory.NavigationStrategy

abstract class NavigationAppCompatActivity : AppCompatActivity(), BaseFragment.FragmentNavigation,
    FragNavController.TransactionListener, FragNavController.RootFragmentListener {
    private val TAG = this.javaClass.name
    private val pushAndPopTransaction by lazy {
        FragNavTransactionOptions.Builder().customAnimations(
            R.anim.slide_in_from_right,
            R.anim.slide_out_to_left,
            R.anim.slide_in_from_left,
            R.anim.slide_out_to_right
        ).allowStateLoss(true).build()
    }

    private var navigationStrategy: NavigationStrategy? = null
    var fragNavController: FragNavController? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initFragNavController(savedInstanceState)
    }

    private fun initFragNavController(savedInstanceState: Bundle?) {
        fragNavController = FragNavController(supportFragmentManager, containerId)
        fragNavController?.transactionListener = this
        fragNavController?.rootFragmentListener = this
        fragNavController?.defaultTransactionOptions =
            FragNavTransactionOptions.Builder().allowStateLoss(true).build()
        fragNavController?.fragmentHideStrategy =
            FragNavController.DETACH_ON_NAVIGATE_HIDE_ON_SWITCH
        fragNavController?.createEager = true
        if (navigationStrategy != null) {
            fragNavController?.navigationStrategy = navigationStrategy as NavigationStrategy
        }
        fragNavController?.initialize(FragNavController.TAB1, savedInstanceState)

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        fragNavController?.onSaveInstanceState(outState)
    }

    override fun pushFragment(fragment: Fragment?) {
        fragNavController?.pushFragment(fragment, pushAndPopTransaction)
    }

    override fun popFragment() {
        if (fragNavController?.isRootFragment == false && fragNavController?.isStateSaved == false) {
            fragNavController?.popFragment(pushAndPopTransaction)
        }
    }

    override fun popFragmentToRoot() {
        while (fragNavController?.isRootFragment == false && fragNavController?.isStateSaved == false) {
            fragNavController?.popFragment(pushAndPopTransaction)
        }
    }

    override fun clearStack() {
        fragNavController?.clearStack()
    }

    override fun currentTab(): Int? {
        return fragNavController?.currentStackIndex
    }

    override fun onFragmentTransaction(
        fragment: Fragment?,
        transactionType: FragNavController.TransactionType
    ) {
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(!(fragNavController?.isRootFragment ?: true))
        (fragment as? BaseFragment)?.onFragmentTransaction(transactionType)
    }

    override fun onBackPressed() {
        if (!tryPopFragment()) {
            super.onBackPressed()
        }
    }

    @get:IdRes
    protected val containerId: Int
        get() = R.id.layout_fragment

    private fun tryPopFragment(): Boolean {
        return fragNavController?.isRootFragment == false &&
                fragNavController?.popFragment(pushAndPopTransaction) == true &&
                fragNavController?.isStateSaved == false
    }
}