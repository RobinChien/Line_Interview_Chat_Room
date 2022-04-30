package com.example.line_interview_chatbot.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.line_interview_chatbot.R
import com.example.line_interview_chatbot.data.enum.BundleKey
import com.example.line_interview_chatbot.data.model.User
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.fragment_chat_log.*

class ChatLogFragment : BaseFragment() {
    private val adapter = GroupAdapter<ViewHolder>()
    private var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = arguments
        user = bundle?.getSerializable(BundleKey.USER.key) as User

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
        setTitle(user?.name)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        recyclerview_chat_log.adapter = adapter
    }

    companion object {
        fun newInstance(user: User): ChatLogFragment {
            val chatLogFragment = ChatLogFragment()
            val bundle = Bundle()
            bundle.putSerializable(BundleKey.USER.key, user)
            chatLogFragment.arguments = bundle
            return chatLogFragment
        }
    }
}