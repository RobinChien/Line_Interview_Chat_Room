package com.example.line_interview_chatbot.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.example.line_interview_chatbot.R
import com.example.line_interview_chatbot.data.model.User
import com.example.line_interview_chatbot.ui.fragment.FriendsFragment
import com.example.line_interview_chatbot.ui.fragment.LoginFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ncapdevi.fragnav.FragNavController
import java.lang.IllegalStateException

class MainActivity : NavigationAppCompatActivity() {
    private val TAG = this.javaClass.simpleName
    var currentUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (!verifyUserIsLoggedIn()) {
            pushFragment(LoginFragment.newInstance())
        }
        fetchCurrentUser()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_sign_in_and_out, menu)
        val item = if (!verifyUserIsLoggedIn()) {
            menu.findItem(R.id.menu_item_sign_out)
        } else {
            menu.findItem(R.id.menu_item_sign_in)
        }
        item.isVisible = false
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_item_sign_in -> {
                if (!verifyUserIsLoggedIn()) {
                    pushFragment(LoginFragment.newInstance())
                }
            }
            R.id.menu_item_sign_out -> {
                FirebaseAuth.getInstance().signOut()
                pushFragment(LoginFragment.newInstance())
                currentUser = null
            }
            android.R.id.home -> popFragment()
        }
        return super.onOptionsItemSelected(item)
    }

    override val numberOfRootFragments: Int
        get() = 1

    override fun getRootFragment(index: Int): Fragment {
        when(index) {
            INDEX_FRIENDS -> return FriendsFragment.newInstance()
            INDEX_LOGIN -> return LoginFragment.newInstance()
        }
        throw IllegalStateException("$TAG: getRootFragment: index not found")
    }

    private fun verifyUserIsLoggedIn(): Boolean {
        Log.d(TAG, "FirebaseAuth.getInstance().uid: ${FirebaseAuth.getInstance().uid}")
        FirebaseAuth.getInstance().uid ?: return false
        return true
    }

    fun fetchCurrentUser() {
        val uid = FirebaseAuth.getInstance().uid ?: return
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                currentUser = dataSnapshot.getValue(User::class.java)
                Log.d(TAG, "fetchCurrentUser: currentUser: ${currentUser}")
            }
        })
    }

    companion object {
        private val INDEX_FRIENDS = FragNavController.TAB1
        private val INDEX_LOGIN = FragNavController.TAB2
    }
}