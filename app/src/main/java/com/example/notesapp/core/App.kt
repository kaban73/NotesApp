package com.example.notesapp.core

import android.app.Application
import androidx.lifecycle.ViewModel

class App : Application(), ProvideViewModel {
    lateinit var factory: ViewModelFactory
    private val clearViewModel = object :ClearViewModel {
        override fun clear(viewModelClass: Class<out ViewModel>) {
            factory.clear(viewModelClass)
        }
    }

    override fun onCreate() {
        super.onCreate()
        val provideViewModel = ProvideViewModel.Base(
            Core(this),
            clearViewModel
        )
        factory = ViewModelFactory.Base(provideViewModel)
    }
    override fun <T : ViewModel> viewModel(viewModelClass: Class<T>): T =
        factory.viewModel(viewModelClass)
}