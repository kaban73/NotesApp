package com.example.notesapp.main

import androidx.lifecycle.LiveData
import org.junit.Assert.assertEquals


interface FakeNavigation : Navigation.Mutable {
    fun checkUpdateCalled(expected : List<Screen>)

    class Base : FakeNavigation {
        private val callList = mutableListOf<Screen>()
        override fun checkUpdateCalled(expected: List<Screen>) {
            assertEquals(expected,callList)
        }
        override fun update(value : Screen) {
            callList.add(value)
        }
        override fun liveData() : LiveData<Screen> {
            throw IllegalStateException("Not used in this test")
        }
    }
}