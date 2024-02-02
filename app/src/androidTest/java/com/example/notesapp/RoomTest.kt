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
import com.example.notesapp.core.NoteCache
import com.example.notesapp.core.NotesDao
import com.example.notesapp.core.NotesDataBase
import java.text.SimpleDateFormat
import java.util.Date

@RunWith(AndroidJUnit4::class)
class RoomTest {
    private lateinit var db : NotesDataBase
    private lateinit var notesDao: NotesDao
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
        val currentDate = SimpleDateFormat("dd/M/yyyy hh:mm")
            .format(Date())
        notesDao.insert(note = NoteCache(id = 1L, title = "first note" , text = "i am a first note", lastDate = currentDate))
        notesDao.insert(note = NoteCache(id = 2L, title = "second note" , text = "i am a second note", lastDate = currentDate))
        notesDao.insert(note = NoteCache(id = 3L, title = "third note" , text = "i am a third note", lastDate = currentDate))

        val notesListExpected = listOf(
            NoteCache(id = 1L, title = "first note" , text = "i am a first note", lastDate = currentDate),
            NoteCache(id = 2L, title = "second note" , text = "i am a second note", lastDate = currentDate),
            NoteCache(id = 3L, title = "third note" , text = "i am a third note", lastDate = currentDate)
        )
        val notesListActual = notesDao.notes()
        assertEquals(notesListExpected,notesListActual)

        notesDao.deleteNote(noteId = 1L)
        val currentDate2 = SimpleDateFormat("dd/M/yyyy hh:mm")
            .format(Date())
        notesDao.insert(note = NoteCache(id = 2L, title = "new first note" , text = "i am a first note now", lastDate = currentDate2))

        val expectedNote = NoteCache(id = 2L, title = "new first note" , text = "i am a first note now", lastDate = currentDate2)
        val actualNote = notesDao.note(noteId = 2L)
        assertEquals(expectedNote,actualNote)

        val notesListFinalExpected = listOf(
            NoteCache(id = 2L, title = "new first note" , text = "i am a first note now", lastDate = currentDate2),
            NoteCache(id = 3L, title = "third note" , text = "i am a third note", lastDate = currentDate)
        )
        val notesListFinalActual = notesDao.notes()
        assertEquals(notesListFinalExpected,notesListFinalActual)
    }
}