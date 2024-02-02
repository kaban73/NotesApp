package com.example.notesapp.core

interface NotesRepository  {
    interface Create {
        suspend fun createNote(noteTitle: String, noteText: String) : Long
    }
    interface ReadList {
        suspend fun notesList(): List<MyNote>
    }
    interface ReadNote {
        suspend fun note(noteId: Long): MyNote
    }
    interface Edit  :ReadNote {
        suspend fun deleteNote(noteId: Long)
        suspend fun editNote(noteId: Long, newTitle: String, newText: String)
    }
    interface Mutable : Create, ReadList
    interface All : Mutable, Edit
    class Base(
        private val now: Now,
        private val notesDao: NotesDao
    ) : All {
        override suspend fun createNote(noteTitle: String, noteText: String) : Long {
            val id = now.timeInMillis()
            notesDao.insert(NoteCache(id, noteTitle, noteText))
            return id
        }

        override suspend fun notesList(): List<MyNote> =
            notesDao.notes().map { MyNote(it.id,it.title,it.text) }

        override suspend fun note(noteId: Long): MyNote =
            notesDao.note(noteId).let { MyNote(it.id,it.title,it.text) }

        override suspend fun deleteNote(noteId: Long) {
            notesDao.deleteNote(noteId)
        }

        override suspend fun editNote(noteId: Long, newTitle: String, newText: String) {
            notesDao.insert(NoteCache(noteId,newTitle,newText))
        }
    }
}