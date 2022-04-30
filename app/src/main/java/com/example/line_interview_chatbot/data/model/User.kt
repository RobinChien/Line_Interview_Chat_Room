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
) : Serializable {
    object User {
        object Column {
            const val uid = "uid"
            const val name = "name"
        }
    }
}
