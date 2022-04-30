package com.example.line_interview_chatbot.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import java.util.*

@Entity
data class Message(
    @ColumnInfo(name= Message.Column.id)
    val id: Int = 0,

    @ColumnInfo(name=Message.Column.message)
    val message: String = "",

    @ColumnInfo(name=Message.Column.timeStamp)
    val timeStamp: Date = Date(),

    @ColumnInfo(name=Message.Column.fromUserId)
    val fromUserId: Int = 0,

    @ColumnInfo(name=Message.Column.toUserId)
    val toUserId: Int = 0,
) {
    object Message {
        object Column {
            const val id = "id"
            const val message = "message"
            const val timeStamp = "timeStamp"
            const val fromUserId = "from_user_id"
            const val toUserId = "to_user_id"
        }
    }
}
