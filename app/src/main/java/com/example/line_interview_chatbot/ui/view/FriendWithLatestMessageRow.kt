package com.example.line_interview_chatbot.ui.view

import com.example.line_interview_chatbot.R
import com.example.line_interview_chatbot.data.model.ChatMessage
import com.example.line_interview_chatbot.data.model.User
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder

class FriendWithLatestMessageRow(val chatMessage: ChatMessage) : Item<ViewHolder>() {
    var chatPartnerUser: User? = null

    override fun getLayout(): Int {
        return R.layout.friend_with_lastest_message_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

}