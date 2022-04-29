package com.example.line_interview_chatbot

import android.app.Application
import androidx.lifecycle.LifecycleObserver

class LineChatApplication: Application(), LifecycleObserver {
    override fun onCreate() {
        super.onCreate()
    }

    companion object {
        lateinit var instance: LineChatApplication
            private set
    }

    private fun syncChats() {
        // Sync chats when open the app
    }

    init {
        instance = this
    }
}