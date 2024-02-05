package com.example.notesapp.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.notesapp.core.ClearViewModel
import com.example.notesapp.core.MyNote
import com.example.notesapp.core.NotesRepository
import com.example.notesapp.list.NotesListLiveDataWrapper
import com.example.notesapp.list.NotesListScreen
import com.example.notesapp.main.Navigation
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditNoteViewModel(
    private val noteLiveDataWrapper: NoteLiveDataWrapper.Mutable,
    private val notesListLiveDataWrapper: NotesListLiveDataWrapper.UpdateNote,
    private val repository: NotesRepository.Edit,
    private val navigation: Navigation.Update,
    private val clear: ClearViewModel,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val dispatcherMain : CoroutineDispatcher = Dispatchers.Main
) : ViewModel(), NoteLiveDataWrapper.Read {
    private var title = ""
    private var text = ""
    private var isFirstRun = true
    fun save(newTitle: String, newText: String) {
        title = newTitle
        text = newText
        isFirstRun = false
    }
    private fun restore() {
        noteLiveDataWrapper.liveData().value?.let {
            val restoreNote = MyNote(it.id,title,text,it.lastDate)
            noteLiveDataWrapper.update(restoreNote)
        }
    }
    fun init(noteId: Long) {
        viewModelScope.launch(dispatcher) {
            val note = repository.note(noteId)
            withContext(dispatcherMain) {
                if (isFirstRun)
                    noteLiveDataWrapper.update(note)
                else
                    restore()
            }
        }
    }

    fun deleteNote(noteId: Long) {
        viewModelScope.launch(dispatcher) {
            repository.deleteNote(noteId)
            withContext(dispatcherMain) {
                comeback()
            }
        }
    }

    fun editNote(noteId: Long, newTitle: String, newText: String) {
        viewModelScope.launch(dispatcher) {
            repository.editNote(noteId,newTitle,newText)
            withContext(dispatcherMain) {
                notesListLiveDataWrapper.update(noteId,newTitle,newText)
                comeback()
            }
        }
    }

    fun comeback() {
        clear.clear(EditNoteViewModel::class.java)
        navigation.update(NotesListScreen)
    }

    private val viewModelScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    override fun liveData(): LiveData<MyNote> =
        noteLiveDataWrapper.liveData()
}