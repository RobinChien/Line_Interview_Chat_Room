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
import com.example.line_interview_chatbot.data.model.User
import com.example.line_interview_chatbot.ui.activity.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_signup.*

class SignupFragment: BaseFragment() {

    private val TAG = this.javaClass.simpleName

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_signup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        setTitle("Sign Up")
        button_signup.setOnClickListener {
            performSignup()
        }

        log_in.setOnClickListener {
            mFragmentNavigation?.popFragment()
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.clear()
    }

    private fun performSignup() {
        val email = edittext_email.text.toString()
        val password = edittext_password.text.toString()
        val name = edittext_name.text.toString()

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(context, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Firebase Authentication to create a user with email and password
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener
                saveUserToFirebaseDatabase(name, email)
                Log.d(TAG, "Successfully created user with uid: ${it.result!!.user?.uid}")
            }
            .addOnFailureListener {
                Log.d(TAG, "Failed to create user: ${it.message}")
                Toast.makeText(context, "${it.message}", Toast.LENGTH_LONG).show()
            }
    }

    private fun saveUserToFirebaseDatabase(name: String = "", email: String = "") {
        val uid = FirebaseAuth.getInstance().uid ?: return
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        val user = User(uid = uid, name = name, email = email)

        ref.setValue(user)
            .addOnSuccessListener {
                Log.d(TAG, "Finally we saved the user to Firebase Database")
                (activity as MainActivity).fetchCurrentUser()
                mFragmentNavigation?.popFragmentToRoot()
            }
            .addOnFailureListener {
                Log.d(TAG, "Failed to set value to database: ${it.message}")
            }
    }



    companion object {
        fun newInstance(): SignupFragment = SignupFragment()
    }
}