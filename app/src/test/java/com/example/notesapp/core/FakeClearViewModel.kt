package com.example.notesapp.core

import androidx.lifecycle.ViewModel
import org.junit.Assert.assertEquals

interface FakeClearViewModel : ClearViewModel {
    fun check(expected : List<Class<out ViewModel>>)
    class Base : FakeClearViewModel {
        private val actualList  = mutableListOf<Class<out ViewModel>>()
        override fun check(expected: List<Class<out ViewModel>>) {
            assertEquals(expected,actualList)
        }
        override fun clear(viewModelClass : Class<out ViewModel>) {
            actualList.remove(viewModelClass)
        }
    }
}