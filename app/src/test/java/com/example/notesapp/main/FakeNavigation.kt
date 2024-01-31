package com.example.notesapp.main

import androidx.lifecycle.LiveData
import com.example.notesapp.list.Order
import org.junit.Assert.assertEquals


interface FakeNavigation : Navigation.Mutable {
    interface Update {
        fun checkUpdateCalled(expected : Screen)
    }
    interface Mutable : Update, Navigation.Mutable

    companion object {
        const val NAVIGATE = "Navigation#update"
    }

    class Base(
        private val order: Order
    ) : Mutable {
        private lateinit var actual : Screen
        override fun checkUpdateCalled(expected: Screen) {
            assertEquals(expected,actual)
        }
        override fun update(value : Screen) {
            actual = value
            order.add(NAVIGATE)
        }
        override fun liveData() : LiveData<Screen> {
            throw IllegalStateException("Not used in this test")
        }
    }
}