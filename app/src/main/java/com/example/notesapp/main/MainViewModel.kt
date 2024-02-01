package com.example.notesapp.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.notesapp.list.NotesListScreen

class MainViewModel(
    private val navigation: Navigation.Mutable
) : ViewModel(), Navigation.Read{
    override fun liveData(): LiveData<Screen> = navigation.liveData()
    fun init(firstRun : Boolean) {
        if (firstRun) navigation.update(NotesListScreen)
    }
}