package com.example.notesapp.list

import com.example.notesapp.core.LiveDataWrapper
import java.text.SimpleDateFormat
import java.util.Date

interface NotesListLiveDataWrapper {
    interface Create {
        fun create(noteUi : NoteUi)
    }
    interface UpdateNote {
        fun update(noteId : Long, newTitle : String, newText : String)
    }
    interface Read : LiveDataWrapper.Read<List<NoteUi>>
    interface Update : LiveDataWrapper.Update<List<NoteUi>>
    interface Mutable : Read, Update
    interface All : Create, Mutable, UpdateNote
    class Base : LiveDataWrapper.Abstract<List<NoteUi>>(), All {
        override fun create(noteUi: NoteUi) {
            val list = liveData.value?.toMutableList() ?: ArrayList()
            list.add(noteUi)
            update(list)
        }

        override fun update(noteId: Long, newTitle: String, newText: String) {
            val currentDate = SimpleDateFormat("dd/M/yyyy hh:mm")
                .format(Date())
            val updateNote = NoteUi(noteId,newTitle,newText, currentDate)
            val list = liveData.value?.toMutableList() ?: ArrayList()
            list.add(updateNote)
            update(list)
        }
    }
}