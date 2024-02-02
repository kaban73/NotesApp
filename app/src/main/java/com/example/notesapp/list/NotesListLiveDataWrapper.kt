package com.example.notesapp.list

import com.example.notesapp.core.LiveDataWrapper

interface NotesListLiveDataWrapper {
    interface Read : LiveDataWrapper.Read<List<NoteUi>>
    interface Update : LiveDataWrapper.Update<List<NoteUi>>
    interface Mutable : Read, Update
    class Base : LiveDataWrapper.Abstract<List<NoteUi>>()
}