package com.example.notesapp.list

import androidx.lifecycle.LiveData
import org.junit.Assert.assertEquals

interface FakeNotesListLiveDataWrapper : NotesListLiveDataWrapper.All{
    fun checkCalledList(expected : List<NoteUi>)
    class Base : FakeNotesListLiveDataWrapper {
        private val actualList = mutableListOf<NoteUi>()
        override fun checkCalledList(expected: List<CharSequence>) {
            assertEquals(expected,actualList)
        }
        override fun liveData() : LiveData<NoteUi> {
            throw IllegalStateException("Not used in this test")
        }
        override fun update(list : List<NoteUi>) {
            actualList.clear()
            actualList.addAll(list)
        }
    }
}