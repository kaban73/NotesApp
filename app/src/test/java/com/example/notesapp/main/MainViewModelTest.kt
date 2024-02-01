package com.example.notesapp.main

import com.example.notesapp.list.NotesListScreen
import com.example.notesapp.list.Order
import org.junit.Before
import org.junit.Test

class MainViewModelTest {
    private lateinit var navigation: FakeNavigation.Mutable
    private lateinit var viewModel: MainViewModel
    private lateinit var order: Order

    @Before
    fun setup() {
        order = Order()
        navigation = FakeNavigation.Base(order)
        viewModel = MainViewModel(navigation = navigation)
    }

    @Test
    fun test_first_run() {
        viewModel.init(firstRun = true)
        navigation.checkUpdateCalled(NotesListScreen)
    }
}