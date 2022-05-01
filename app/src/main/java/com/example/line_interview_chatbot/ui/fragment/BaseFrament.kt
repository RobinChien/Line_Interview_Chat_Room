package com.example.line_interview_chatbot.ui.fragment

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.ncapdevi.fragnav.FragNavController

abstract class BaseFragment : Fragment() {
    var mFragmentNavigation: FragmentNavigation? = null
        protected set
    private var titleBase: String? = ""

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentNavigation) {
            mFragmentNavigation = context
        }
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

    protected fun setTitle(title: String?) {
        this.titleBase = title
        val activity = activity as? AppCompatActivity ?: return
        activity.title = title
    }
}