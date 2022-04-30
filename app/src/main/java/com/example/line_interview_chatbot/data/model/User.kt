package com.example.line_interview_chatbot.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import java.io.Serializable
import java.util.*

@Entity
data class User(
    @ColumnInfo(name= User.Column.id)
    val id: UUID = UUID.randomUUID(),

    @ColumnInfo(name= User.Column.id)
    val name: String = "",
) : Serializable {
    object User {
        object Column {
            const val id = "id"
            const val name = "name"
        }
    }
}
