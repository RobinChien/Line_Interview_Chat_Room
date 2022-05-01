package com.example.line_interview_chatbot.ui.view

import android.util.Log
import android.view.View
import com.example.line_interview_chatbot.R
import com.example.line_interview_chatbot.data.model.ChatMessage
import com.example.line_interview_chatbot.data.model.User
import com.example.line_interview_chatbot.utils.DateUtils
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.friend_item_row.view.*

class FriendItemRow(val friend: User, val chatMessage: ChatMessage? = null, val isNewFriend: Boolean = false) : Item<ViewHolder>() {
    val TAG = this.javaClass.simpleName
    override fun getLayout(): Int {
        return R.layout.friend_item_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.username_textview_latest_message.text = friend.name
        viewHolder.itemView.textview_friend_name_abbr.text = friend.name.first().toString().uppercase()

        if (isNewFriend) {
            viewHolder.itemView.latest_message_textview.visibility = View.INVISIBLE
            viewHolder.itemView.latest_msg_time.visibility = View.INVISIBLE
        } else {
            viewHolder.itemView.latest_message_textview.visibility = View.VISIBLE
            viewHolder.itemView.latest_msg_time.visibility = View.VISIBLE

            chatMessage?.apply {
                viewHolder.itemView.latest_message_textview.text = this.text
                viewHolder.itemView.latest_msg_time.text = DateUtils.getFormattedTime(this.timestamp)
            }
        }
    }

}