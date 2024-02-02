package com.example.notesapp.list

import com.example.notesapp.core.LiveDataWrapper

interface NotesListLiveDataWrapper {
    interface Create {
        fun create(noteUi : NoteUi)
    }
    interface Read : LiveDataWrapper.Read<List<NoteUi>>
    interface Update : LiveDataWrapper.Update<List<NoteUi>>
    interface Mutable : Read, Update
    interface All : Create, Mutable
    class Base : LiveDataWrapper.Abstract<List<NoteUi>>(), All {
        override fun create(noteUi: NoteUi) {
            val list = liveData.value?.toMutableList() ?: ArrayList()
            list.add(noteUi)
            update(list)
        }
    }
}