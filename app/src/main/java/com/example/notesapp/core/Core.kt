package com.example.notesapp.core

import android.content.Context
import androidx.room.Room

class Core(
    private val context : Context
) {
    private val dataBase by lazy {
        Room.databaseBuilder(
            context,
            NotesDataBase::class.java,
            "notes_database"
        ).build()
    }
    fun notesDao() = dataBase.notesDao()
}