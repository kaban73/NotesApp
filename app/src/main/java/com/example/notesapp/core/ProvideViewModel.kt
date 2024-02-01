package com.example.notesapp.core

import androidx.lifecycle.ViewModel

interface ProvideViewModel {
    fun <T : ViewModel> viewModel(viewModelClass : Class<T>) : T
    class Base(
        core: Core,
        private val clearViewModel: ClearViewModel
    ) : ProvideViewModel {
        override fun <T : ViewModel> viewModel(viewModelClass: Class<T>): T = when(viewModelClass) {
            else -> throw IllegalStateException("unknown viewModelClass $viewModelClass")
        }

    }
}