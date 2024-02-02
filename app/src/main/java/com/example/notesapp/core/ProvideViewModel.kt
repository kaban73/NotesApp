package com.example.notesapp.core

import androidx.lifecycle.ViewModel
import com.example.notesapp.create.CreateNoteViewModel
import com.example.notesapp.list.NotesListLiveDataWrapper
import com.example.notesapp.main.MainViewModel
import com.example.notesapp.main.Navigation

interface ProvideViewModel {
    fun <T : ViewModel> viewModel(viewModelClass : Class<T>) : T
    class Base(
        core: Core,
        private val clearViewModel: ClearViewModel
    ) : ProvideViewModel {
        private val navigation = Navigation.Base()
        private val notesListLiveDataWrapper = NotesListLiveDataWrapper.Base()
        private val notesRepository = NotesRepository.Base(Now.Base(), core.notesDao())
        override fun <T : ViewModel> viewModel(viewModelClass: Class<T>): T = when(viewModelClass) {
            MainViewModel::class.java->MainViewModel(navigation)
            CreateNoteViewModel::class.java->CreateNoteViewModel(notesListLiveDataWrapper,notesRepository,navigation,clearViewModel)
            
            else -> throw IllegalStateException("unknown viewModelClass $viewModelClass")
        } as T

    }
}