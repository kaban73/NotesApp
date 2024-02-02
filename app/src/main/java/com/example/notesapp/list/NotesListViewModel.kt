package com.example.notesapp.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.notesapp.core.NotesRepository
import com.example.notesapp.create.CreateNoteScreen
import com.example.notesapp.edit.EditNoteScreen
import com.example.notesapp.main.Navigation
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NotesListViewModel(
    private val notesRepository: NotesRepository.ReadList,
    private val notesListLiveDataWrapper: NotesListLiveDataWrapper.Mutable,
    private val navigation: Navigation.Update,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val dispatcherMain : CoroutineDispatcher = Dispatchers.Main
)  :ViewModel(), NotesListLiveDataWrapper.Read {
    fun init() {
        viewModelScope.launch(dispatcher) {
            val list = notesRepository.notesList().map { NoteUi(it.id,it.title,it.text, it.lastDate) }
            withContext(dispatcherMain){
                notesListLiveDataWrapper.update(list)
            }
        }
    }

    fun createNote() {
        navigation.update(CreateNoteScreen)
    }

    fun editNote(noteUi: NoteUi) {
        navigation.update(EditNoteScreen(noteUi.id))
    }

    private val viewModelScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    override fun liveData(): LiveData<List<NoteUi>> =
        notesListLiveDataWrapper.liveData()

}