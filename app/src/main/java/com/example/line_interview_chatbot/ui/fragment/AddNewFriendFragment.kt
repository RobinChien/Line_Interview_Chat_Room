package com.example.line_interview_chatbot.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.line_interview_chatbot.R
import com.example.line_interview_chatbot.data.model.User
import com.example.line_interview_chatbot.ui.view.FriendItemRow
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.fragment_add_new_friend.*

class AddNewFriendFragment : BaseFragment() {
    private val TAG = this.javaClass.simpleName

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_new_friend, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setTitle("Add New Friend")
        fetchUsers()
    }

    private fun fetchUsers() {
        val ref = FirebaseDatabase.getInstance().getReference("/users")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()

                dataSnapshot.children.forEach {
                    Log.d(TAG, it.toString())
                    @Suppress("NestedLambdaShadowedImplicitParameter")
                    it.getValue(User::class.java)?.let {
                        if (it.uid.toString() != FirebaseAuth.getInstance().uid) {
                            adapter.add(FriendItemRow(friend = it, isNewFriend = true))
                        }
                    }
                }

                adapter.setOnItemClickListener { item, view ->
                    mFragmentNavigation?.popFragmentToRoot()
                }

                recyclerview_new_friend.adapter = adapter
            }

        })
    }

    companion object {
        fun newInstance(): AddNewFriendFragment = AddNewFriendFragment()
    }
}