package com.example.line_interview_chatbot.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import java.io.Serializable
import java.util.*

@Entity
data class ChatMessage(
    @ColumnInfo(name= Message.Column.id)
    val id: Int = 0,

    @ColumnInfo(name=Message.Column.text)
    val text: String = "",

    @ColumnInfo(name=Message.Column.timeStamp)
    val timeStamp: Date = Date(),

    @ColumnInfo(name=Message.Column.fromUserId)
    val fromUserId: UUID? = null,

    @ColumnInfo(name=Message.Column.toUserId)
    val toUserId: UUID? = null,
) : Serializable {
    object Message {
        object Column {
            const val id = "id"
            const val text = "text"
            const val timeStamp = "timeStamp"
            const val fromUserId = "from_user_id"
            const val toUserId = "to_user_id"
        }
    }
}
