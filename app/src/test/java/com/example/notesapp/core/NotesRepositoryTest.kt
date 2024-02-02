package com.example.notesapp.core

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Date

class NotesRepositoryTest {
    private lateinit var now : FakeNow
    private lateinit var notesDao: FakeNotesDao
    private lateinit var notesRepository : NotesRepository.All
    @Test
    fun test_repository() = runBlocking {
        now = FakeNow.Base(1L)
        notesDao = FakeNotesDao.Base()
        notesRepository = NotesRepository.Base(
            now = now,
            notesDao = notesDao
        )

        val firstNoteId = notesRepository.createNote(
            noteTitle = "first note",
            noteText = "this is a first note!")
        val secondNoteId = notesRepository.createNote(
            noteTitle = "second note",
            noteText = "this is a second note!")
        assertEquals(1L, firstNoteId)
        assertEquals(2L, secondNoteId)

        val notesListActual = notesRepository.notesList()
        val currentDate = SimpleDateFormat("dd/M/yyyy hh:mm")
            .format(Date())
        val notesListExpected = listOf(
            MyNote(id = 1L,title = "first note", text = "this is a first note!", lastDate = currentDate),
            MyNote(id = 2L , title ="second note", text = "this is a second note!", lastDate = currentDate)
        )
        assertEquals(notesListExpected, notesListActual)

        notesRepository.deleteNote(1L)
        notesRepository.editNote(noteId = 2L, newTitle = "i am the first note now!", newText = "this is a first note now!")

        val notesListActual1 = notesRepository.notesList()
        val notesListExpected1 = listOf(
            MyNote(id = 2L , title ="i am the first note now!", text = "this is a first note now!", lastDate = currentDate)
        )
        assertEquals(notesListActual1,notesListExpected1)

        val noteActual = notesRepository.note(2L)
        val noteExpected = MyNote(id = 2L , title ="i am the first note now!", text = "this is a first note now!", lastDate = currentDate)
        assertEquals(noteExpected, noteActual)
    }
}
private interface FakeNow : Now {
    class Base(
        private var time : Long
    ) : FakeNow {
        override fun timeInMillis() : Long =
            time++
    }
}

private interface FakeNotesDao : NotesDao {
    class Base : FakeNotesDao {
        private val map = mutableMapOf<Long, NoteCache>()
        override suspend fun insert(note : NoteCache) {
            map[note.id] = note
        }
        override suspend fun note(noteId : Long) : NoteCache =
            map[noteId]!!
        override suspend fun notes() : List<NoteCache> =
            map.map {it.value}.toList().sortedBy { it.id }
        override suspend fun deleteNote(noteId : Long) {
            map.remove(noteId)
        }
    }
}