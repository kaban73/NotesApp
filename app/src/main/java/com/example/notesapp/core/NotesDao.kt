package com.example.notesapp.core

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface NotesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: NoteCache)
    @Query("select * from notes_table")
    suspend fun notes() : List<NoteCache>
    @Query("delete from notes_table where id = :noteId")
    suspend fun deleteNote(noteId : Long)
    @Query("select * from notes_table where id = :noteId")
    suspend fun note(noteId : Long) : NoteCache
}