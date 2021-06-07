package com.bar.youday.data.repository

import android.app.Application

class NoteIntentApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        NotesRepositoryImp.initialize(this)
    }
}