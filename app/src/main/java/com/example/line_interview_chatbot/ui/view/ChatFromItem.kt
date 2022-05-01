package com.example.line_interview_chatbot.ui.view

import com.example.line_interview_chatbot.R
import com.example.line_interview_chatbot.data.model.User
import com.example.line_interview_chatbot.utils.DateUtils.getFormattedTimeChatLog
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.chat_from_row.view.*

class ChatFromItem(val text: String, val user: User, private val timestamp: Long) : Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textview_from_row.text = text
        viewHolder.itemView.from_msg_time.text = getFormattedTimeChatLog(timestamp)
    }

    override fun getLayout(): Int {
        return R.layout.chat_from_row
    }

}