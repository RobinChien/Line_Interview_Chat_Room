package com.example.line_interview_chatbot.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.line_interview_chatbot.R
import com.example.line_interview_chatbot.ui.activity.MainActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_login.edittext_email
import kotlinx.android.synthetic.main.fragment_login.edittext_password
import kotlinx.android.synthetic.main.fragment_signup.*

class LoginFragment: BaseFragment() {
    private val TAG = this.javaClass.name

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        setTitle("Log In")
        button_login.setOnClickListener {
            performLogin()
        }
        sign_up.setOnClickListener {
            mFragmentNavigation?.pushFragment(SignupFragment.newInstance())
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.clear()
    }

    private fun performLogin() {
        val email = edittext_email.text.toString()
        val password = edittext_password.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(context, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            return
        }

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                (activity as MainActivity).fetchCurrentUser()
                if (!it.isSuccessful) return@addOnCompleteListener
                Log.d(TAG, "Successfully logged in: ${it.result!!.user?.uid}")
                mFragmentNavigation?.popFragmentToRoot()
            }
            .addOnFailureListener {
                Toast.makeText(context, "${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    companion object {
        fun newInstance(): LoginFragment = LoginFragment()
    }
}