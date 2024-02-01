package com.example.notesapp.core

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [NoteCache::class], version = 1)
abstract class NotesDataBase : RoomDatabase(){
    abstract fun notesDao() : NotesDao
}