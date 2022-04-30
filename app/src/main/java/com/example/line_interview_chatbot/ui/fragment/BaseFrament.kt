package com.example.line_interview_chatbot.ui.fragment

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.ncapdevi.fragnav.FragNavController

abstract class BaseFragment : Fragment() {
    var mFragmentNavigation: FragmentNavigation? = null
        protected set
    protected var titleBase: String? = ""
    var attached = false
        private set

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentNavigation) {
            mFragmentNavigation = context
        }
        attached = true
    }

    override fun onDetach() {
        super.onDetach()
        attached = false
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            requireActivity().invalidateOptionsMenu()
            setTitle(titleBase)
        }
    }

    interface FragmentNavigation {
        fun pushFragment(fragment: Fragment?)
        fun popFragment()
        fun popFragmentToRoot()
        fun clearStack()
        fun currentTab(): Int?
    }

    fun onFragmentTransaction(transactionType: FragNavController.TransactionType) {
        setTitle(titleBase)
    }

    fun onTabTransaction(index: Int) {
        setTitle(titleBase)
    }

    protected fun setTitle(title: String?) {
        this.titleBase = title
        val activity = activity as? AppCompatActivity ?: return
        val actionBar = activity.supportActionBar ?: return
        if (title == null) {
            actionBar.hide()
        } else {
            actionBar.show()
            activity.title = title
        }
    }

    protected fun setActionBarVisibility(fragmentActivity: FragmentActivity, visible: Boolean) {
        val activity = fragmentActivity as? AppCompatActivity
        val actionBar = activity?.supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(visible)
        actionBar?.setDisplayShowHomeEnabled(visible)
    }
}