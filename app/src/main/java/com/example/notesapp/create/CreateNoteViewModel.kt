package com.example.notesapp.create

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.notesapp.core.ClearViewModel
import com.example.notesapp.core.NotesRepository
import com.example.notesapp.list.NoteUi
import com.example.notesapp.list.NotesListLiveDataWrapper
import com.example.notesapp.list.NotesListScreen
import com.example.notesapp.main.Navigation
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date

class CreateNoteViewModel(
    private val addNoteLiveDataWrapper : NotesListLiveDataWrapper.Create,
    private val createNoteLiveDataWrapper : CreateNoteLiveDataWrapper.All,
    private val notesRepository: NotesRepository.Create,
    private val navigation: Navigation.Update,
    private val clear : ClearViewModel,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val dispatcherMain : CoroutineDispatcher = Dispatchers.Main
) : ViewModel(), CreateNoteLiveDataWrapper.Read {
    fun save(title : String, text : String) {
        createNoteLiveDataWrapper.update(Pair(title,text))
    }
    override fun liveData(): LiveData<Pair<String, String>> =
        createNoteLiveDataWrapper.liveData()
    fun createNote(title: String, text: String) {
        viewModelScope.launch(dispatcher) {
            val id = notesRepository.createNote(title,text)
            withContext(dispatcherMain) {
                val currentDate = SimpleDateFormat("dd/M/yyyy hh:mm")
                    .format(Date())
                addNoteLiveDataWrapper.create(NoteUi(id,title,text, currentDate))
                comeback()
            }
        }
    }

    fun comeback() {
        createNoteLiveDataWrapper.clear()
        clear.clear(CreateNoteViewModel::class.java)
        navigation.update(NotesListScreen)
    }

    private val viewModelScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
}