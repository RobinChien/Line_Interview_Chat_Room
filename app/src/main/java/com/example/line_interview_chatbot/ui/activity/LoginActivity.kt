package com.example.line_interview_chatbot.ui.activity

import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.example.line_interview_chatbot.R
import com.example.line_interview_chatbot.ui.fragment.LoginFragment

class LoginActivity: NavigationAppCompatActivity() {
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_cancel, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_item_cancel -> finish()
            else -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override val numberOfRootFragments: Int
        get() = 1

    override fun getRootFragment(index: Int): Fragment {
        return LoginFragment.newInstance()
    }
}