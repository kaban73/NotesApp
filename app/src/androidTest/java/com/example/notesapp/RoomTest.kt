package com.example.notesapp

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import androidx.room.Room

@RunWith(AndroidJUnit4::class)
class RoomTest {
    private lateinit var db : NotesDataBase
    private lateinit var notesDao:NotesDao
    @Before
    fun setup() {
        val context : Context = ApplicationProvider.getApplicationContext()
        db = Room.inMemoryDatabaseBuilder(context, NotesDataBase::class.java)
            .allowMainThreadQueries()
            .build()
        notesDao = db.notesDao()
    }
    @After
    @Throws(IOException::class)
    fun clear() {
        db.close()
    }
    @Test
    fun test_folders() = runBlocking {
        notesDao.insert(note = NoteCache(id = 1L, title = "first note" , text = "i am a first note"))
        notesDao.insert(note = NoteCache(id = 2L, title = "second note" , text = "i am a second note"))
        notesDao.insert(note = NoteCache(id = 3L, title = "third note" , text = "i am a third note"))

        val notesListExpected = listOf<NoteCache>(
            NoteCache(id = 1L, title = "first note" , text = "i am a first note"),
            NoteCache(id = 2L, title = "second note" , text = "i am a second note"),
            NoteCache(id = 3L, title = "third note" , text = "i am a third note")
        )
        val notesListActual = notesDao.notes()
        assertEquals(notesListExpected,notesListActual)

        notesDao.deleteNote(noteId = 1L)
        notesDao.insert(note = NoteCache(id = 2L, title = "new first note" , text = "i am a first note now"))

        val notesListFinalExpected = listOf<NoteCache>(
            NoteCache(id = 2L, title = "new first note" , text = "i am a first note now"),
            NoteCache(id = 3L, title = "third note" , text = "i am a third note")
        )
        val notesListFinalActual = notesDao.notes()
        assertEquals(notesListFinalExpected,notesListFinalActual)
    }
}