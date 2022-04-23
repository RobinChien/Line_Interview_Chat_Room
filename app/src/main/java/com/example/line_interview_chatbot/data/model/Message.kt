package com.example.line_interview_chatbot.data.model

data class Message(
    val id:Int = 0,
    val message:String = "",
    val timeStamp:String,
    val user:User?,
)
