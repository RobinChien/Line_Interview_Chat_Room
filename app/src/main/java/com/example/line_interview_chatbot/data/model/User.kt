package com.example.line_interview_chatbot.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import java.io.Serializable

@Entity
data class User(
    @ColumnInfo(name= User.Column.uid)
    val uid: String = "",

    @ColumnInfo(name= User.Column.uid)
    val name: String = "",

    @ColumnInfo(name= User.Column.email)
    val email: String = "",
) : Serializable {
    object User {
        object Column {
            const val uid = "uid"
            const val name = "name"
            const val email = "email"
        }
    }
}
