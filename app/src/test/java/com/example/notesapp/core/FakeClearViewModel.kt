package com.example.notesapp.core

import androidx.lifecycle.ViewModel
import com.example.notesapp.list.Order
import org.junit.Assert.assertEquals

interface FakeClearViewModel : ClearViewModel {
    companion object{
        const val CLEAR = "FakeClear#clear"
    }
    fun check(expected : List<Class<out ViewModel>>)
    class Base(
        private val order: Order
    ) : FakeClearViewModel {
        private val actualList  = mutableListOf<Class<out ViewModel>>()
        override fun check(expected: List<Class<out ViewModel>>) {
            assertEquals(expected,actualList)
        }
        override fun clear(viewModelClass : Class<out ViewModel>) {
            actualList.remove(viewModelClass)
            order.add(CLEAR)
        }
    }
}