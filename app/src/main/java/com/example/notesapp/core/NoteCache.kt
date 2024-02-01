package com.example.notesapp.core

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "notes_table")
data class NoteCache(
    @PrimaryKey @ColumnInfo(name = "id") val id : Long,
    @ColumnInfo(name = "title") val title : String,
    @ColumnInfo(name = "text") val text : String
)