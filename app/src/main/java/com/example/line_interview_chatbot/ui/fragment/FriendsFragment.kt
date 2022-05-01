package com.example.line_interview_chatbot.ui.fragment

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.line_interview_chatbot.R
import com.example.line_interview_chatbot.data.model.ChatMessage
import com.example.line_interview_chatbot.data.model.User
import com.example.line_interview_chatbot.ui.activity.MainActivity
import com.example.line_interview_chatbot.ui.view.FriendItemRow
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.fragment_friends.*

class FriendsFragment: BaseFragment() {
    private val TAG = this.javaClass.simpleName
    private val adapter = GroupAdapter<ViewHolder>()
    private val latestMessagesMap = HashMap<String, ChatMessage>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_friends, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle("My Friends")
        recyclerview_friends.adapter = adapter
        add_friend_fab.setOnClickListener {
            mFragmentNavigation?.pushFragment(AddNewFriendFragment.newInstance())
        }
    }

    override fun onResume() {
        super.onResume()

        latestMessagesMap.clear()
        adapter.clear()

        if (FirebaseAuth.getInstance().uid != null) {
            listenForLatestMessages()

            adapter.setOnItemClickListener { item, _ ->
                val row = item as FriendItemRow
                val chatLogFragment = row.friend.let {
                    ChatLogFragment.newInstance(user = it)
                }
                mFragmentNavigation?.pushFragment(chatLogFragment)
            }
            add_friend_fab.visibility = View.VISIBLE

        } else {
            add_friend_fab.visibility = View.GONE
        }
    }

    private fun refreshRecyclerViewMessages() {
        adapter.clear()
        latestMessagesMap.values.forEach {
            fetchParentFriendOfChatMessage(it)
        }
    }

    private fun fetchParentFriendOfChatMessage(chatMessage: ChatMessage) {
        val chatPartnerId: String = if (chatMessage.fromUserId.toString() == FirebaseAuth.getInstance().uid) {
            chatMessage.toUserId.toString()
        } else {
            chatMessage.fromUserId.toString()
        }
        val ref = FirebaseDatabase.getInstance().getReference("/users/$chatPartnerId")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                val parentUser = p0.getValue(User::class.java)
                parentUser?.let {
                    adapter.add(FriendItemRow(
                        friend = it,
                        chatMessage = chatMessage,
                        isNewFriend = false
                    ))
                }
            }

        })
    }

    private fun listenForLatestMessages() {
        val fromId = FirebaseAuth.getInstance().uid ?: return
        val ref = FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId")

        ref.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(databaseError: DatabaseError) {
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
                dataSnapshot.getValue(ChatMessage::class.java)?.let {
                    latestMessagesMap[dataSnapshot.key!!] = it
                    refreshRecyclerViewMessages()
                }
            }

            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                dataSnapshot.getValue(ChatMessage::class.java)?.let {
                    latestMessagesMap[dataSnapshot.key!!] = it
                    refreshRecyclerViewMessages()
                }
            }

            override fun onChildRemoved(p0: DataSnapshot) {
            }
        })
    }

    companion object {
        fun newInstance(): FriendsFragment = FriendsFragment()
    }
}