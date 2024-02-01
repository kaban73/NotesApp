package com.example.notesapp.core

import androidx.lifecycle.ViewModel

interface ClearViewModel {
    fun clear(viewModelClass : Class<out ViewModel>)
}