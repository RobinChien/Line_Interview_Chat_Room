package com.example.line_interview_chatbot.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.line_interview_chatbot.R
import com.example.line_interview_chatbot.data.model.User
import com.example.line_interview_chatbot.ui.fragment.FriendsFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ncapdevi.fragnav.FragNavController
import java.lang.IllegalStateException

class MainActivity : NavigationAppCompatActivity() {
    private val TAG = this.javaClass.simpleName
    private val INDEX_FRIENDS = FragNavController.TAB1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fetchCurrentUser()
    }

    override val numberOfRootFragments: Int
        get() = 1

    override fun getRootFragment(index: Int): Fragment {
        when(index) {
            INDEX_FRIENDS -> return FriendsFragment.newInstance()
        }
        throw IllegalStateException("$TAG: getRootFragment: index not found")
    }

    private fun fetchCurrentUser() {
        val uid = FirebaseAuth.getInstance().uid ?: return
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                currentUser = dataSnapshot.getValue(User::class.java)
            }
        })
    }

    companion object {
        var currentUser: User? = null
    }
}