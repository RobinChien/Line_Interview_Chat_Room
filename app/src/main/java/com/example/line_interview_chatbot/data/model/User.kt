package com.example.line_interview_chatbot.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity
data class User(
    @ColumnInfo(name= User.Column.id)
    val id: Int = 0,

    @ColumnInfo(name= User.Column.id)
    val name: String = "",
) {
    object User {
        object Column {
            const val id = "id"
            const val name = "name"
        }
    }
}
