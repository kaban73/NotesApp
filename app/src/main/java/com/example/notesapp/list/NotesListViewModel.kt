package com.example.notesapp.list

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
import java.text.SimpleDateFormat
import java.util.Date

class NotesListViewModel(
    private val notesRepository: NotesRepository.ReadList,
    private val notesListLiveDataWrapper: NotesListLiveDataWrapper.Mutable,
    private val navigation: Navigation.Update,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val dispatcherMain : CoroutineDispatcher = Dispatchers.Main
) {
    fun init() {
        viewModelScope.launch(dispatcher) {
        val currentDate = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
            .format(Date())
            val list = notesRepository.notesList().map { NoteUi(it.id,it.title,it.text, currentDate) }
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

}