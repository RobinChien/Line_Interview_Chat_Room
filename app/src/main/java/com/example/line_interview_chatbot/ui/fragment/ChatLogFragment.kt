package com.example.line_interview_chatbot.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.line_interview_chatbot.R
import com.example.line_interview_chatbot.data.enum.BundleKey
import com.example.line_interview_chatbot.data.model.ChatMessage
import com.example.line_interview_chatbot.data.model.User
import com.example.line_interview_chatbot.ui.activity.MainActivity
import com.example.line_interview_chatbot.ui.view.ChatFromItem
import com.example.line_interview_chatbot.ui.view.ChatToItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.fragment_chat_log.*

class ChatLogFragment : BaseFragment() {
    private val TAG = this.javaClass.simpleName
    private val adapter = GroupAdapter<ViewHolder>()
    private var toUser: User? = null
    private var ref: DatabaseReference? = null
    private var childEventListener: ChildEventListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = arguments
        toUser = bundle?.getSerializable(BundleKey.TO_USER.key) as User

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chat_log, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTitle(toUser?.name)
        recyclerview_chat_log.adapter = adapter

        send_button_chat_log.setOnClickListener {
            performSendMessage()
        }
    }

    override fun onResume() {
        super.onResume()
        listenForMessages()
    }

    override fun onPause() {
        super.onPause()
        childEventListener?.apply {
            ref?.removeEventListener(this)
        }

    }

    private fun listenForMessages() {
        val fromId = FirebaseAuth.getInstance().uid ?: return
        val toId = toUser?.uid
        val currentUser = (activity as MainActivity).currentUser ?: return

        ref = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId")
        childEventListener = ChildEventListener(recyclerview_chat_log, adapter, currentUser, toUser!!)
        childEventListener?.apply {
            ref?.addChildEventListener(this)
        }
    }

    private fun performSendMessage() {
        val text = edittext_chat_log.text.toString()
        if (text.isEmpty()) {
            Toast.makeText(context, "Message cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }

        val fromId = FirebaseAuth.getInstance().uid ?: return
        val toId = toUser?.uid

        val reference = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId").push()
        val toReference = FirebaseDatabase.getInstance().getReference("/user-messages/$toId/$fromId").push()

        val chatMessage = ChatMessage(
            id = reference.key!!,
            text = text,
            fromUserId = fromId,
            toUserId = toId,
            timestamp = System.currentTimeMillis() / 1000
        )
        reference.setValue(chatMessage)
            .addOnSuccessListener {
                Log.d(TAG, "Saved our chat message: ${reference.key}")
                edittext_chat_log.text.clear()
                Log.d(TAG, "performSendMessage: adapter.itemCount: ${adapter.itemCount}")
                Log.d(TAG, "performSendMessage: recyclerview_chat_log: $recyclerview_chat_log")
                recyclerview_chat_log.smoothScrollToPosition(adapter.itemCount - 1)
            }

        toReference.setValue(chatMessage)


        val latestMessageRef = FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId/$toId")
        latestMessageRef.setValue(chatMessage)

        val latestMessageToRef = FirebaseDatabase.getInstance().getReference("/latest-messages/$toId/$fromId")
        latestMessageToRef.setValue(chatMessage)
    }

    private class ChildEventListener(
        val recyclerView: RecyclerView,
        val adapter: GroupAdapter<ViewHolder>,
        val fromUser: User,
        val toUser: User
        ): com.google.firebase.database.ChildEventListener
    {
        private val TAG = this.javaClass.simpleName
        override fun onChildAdded(p0: DataSnapshot, p1: String?) {
            p0.getValue(ChatMessage::class.java)?.let {
                Log.d(TAG, "onChildAdded: chat message: ${it}")
                if (it.fromUserId == FirebaseAuth.getInstance().uid) {
                    adapter.add(ChatFromItem(it.text, fromUser, it.timestamp))
                } else {
                    adapter.add(ChatToItem(it.text, toUser, it.timestamp))
                }
            }
            recyclerView.scrollToPosition(adapter.itemCount - 1)
        }

        override fun onChildChanged(p0: DataSnapshot, p1: String?) {}

        override fun onChildRemoved(p0: DataSnapshot) {}

        override fun onChildMoved(p0: DataSnapshot, p1: String?) {}

        override fun onCancelled(p0: DatabaseError) {}

    }

    companion object {
        fun newInstance(user: User): ChatLogFragment {
            val chatLogFragment = ChatLogFragment()
            val bundle = Bundle()
            bundle.putSerializable(BundleKey.TO_USER.key, user)
            chatLogFragment.arguments = bundle
            return chatLogFragment
        }
    }
}