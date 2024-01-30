package com.example.notesapp.core

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class NotesRepositoryTest {
    private lateinit var now : FakeNow
    private lateinit var notesDao: FakeNotesDao
    private lateinit var notesRepository : NotesRepository
    @Test
    fun test_repository() = runBlocking {
        now = FakeNow.Base(1L)
        notesDao = FakeNotesDao.Base()
        notesRepository = NotesRepository(
            now = now,
            notesDao = notesDao
        )

        notesRepository.createNote(
            noteTitle = "first note",
            noteText = "this is a first note!")
        notesRepository.createNote(
            noteTitle = "second note",
            noteText = "this is a second note!")
        val notesListActual = notesRepository.notesList()
        val notesListExpected = listOf<MyNote>(
            MyNote(id = 1L,title = "first note", text = "this is a first note!"),
            MyNote(id = 2L , title ="second note", text = "this is a second note!")
        )
        assertEquals(notesListExpected, notesListActual)

        notesRepository.deleteNote(1L)
        notesRepository.renameNote(id = 2L, newTitle = "i am the first note now!")
        notesRepository.changeContentNote(id = 2L, newText = "this is a first note now!")

        val notesListActual = notesRepository.notesList()
        val notesListExpected = listOf<MyNote>(
            MyNote(id = 2L , title ="i am the first note now!", text = "this is a first note now!")
        )
        assertEquals(notesListActual,notesListExpected)

        val noteActual = notesRepository.note(2L)
        val noteExpected = MyNote(id = 2L , title ="i am the first note now!", text = "this is a first note now!")
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
        override suspend fun insert(noteCache : NoteCache) {
            map[noteCache.id] = noteCache
        }
        override suspend fun note(noteId : Long) : NoteCache =
            map[noteId]
        override suspend fun notes() : List<NoteCache> =
            map.map {it.value}.toList
        override suspend fun delete(noteId : Long) =
            map.remove(noteId)

    }
}