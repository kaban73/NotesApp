package com.example.notesapp.create

import com.example.notesapp.core.FakeClearViewModel
import com.example.notesapp.core.FakeClearViewModel.Companion.CLEAR
import com.example.notesapp.core.NotesRepository
import com.example.notesapp.list.NoteUi
import com.example.notesapp.list.NotesListLiveDataWrapper
import com.example.notesapp.list.NotesListScreen
import com.example.notesapp.list.Order
import com.example.notesapp.main.FakeNavigation
import com.example.notesapp.main.FakeNavigation.Companion.NAVIGATE
import kotlinx.coroutines.Dispatchers
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Date

class CreateNoteViewModelTest {
    private lateinit var order: Order
    private lateinit var notesRepository: FakeCreateNoteRepository
    private lateinit var addNoteLiveDataWrapper: FakeAddNoteLiveDataWrapper
    private lateinit var navigation: FakeNavigation.Update
    private lateinit var clear : FakeClearViewModel
    private lateinit var viewModel: CreateNoteViewModel
    @Before
    fun setup(){
        order = Order()
        notesRepository = FakeCreateNoteRepository.Base(order,1L)
        addNoteLiveDataWrapper = FakeAddNoteLiveDataWrapper.Base(order)
        navigation = FakeNavigation.Base(order)
        clear = FakeClearViewModel.Base(order)
        viewModel = CreateNoteViewModel(
            addNoteLiveDataWrapper = addNoteLiveDataWrapper,
            notesRepository = notesRepository,
            navigation = navigation as FakeNavigation.Base,
            clear = clear,
            dispatcher = Dispatchers.Unconfined,
            dispatcherMain = Dispatchers.Unconfined
        )
    }
    @Test
    fun test_create() {
        viewModel.createNote(title = "new note", text = "I am a new note")

        notesRepository.check("new note","I am a new note")
        val currentDate = SimpleDateFormat("dd/M/yyyy hh:mm")
            .format(Date())
        addNoteLiveDataWrapper.check(NoteUi(id = 1L, title = "new note", text = "I am a new note", lastDate = currentDate))
        clear.check(listOf(CreateNoteViewModel::class.java))
        navigation.checkUpdateCalled(NotesListScreen)
        order.check(listOf(CREATE_NOTE_REPOSITORY, NOTE_LIST_LIVEDATA_ADD, CLEAR,NAVIGATE))
    }
    @Test
    fun comeback() {
        viewModel.comeback()

        clear.check(listOf(CreateNoteViewModel::class.java))
        navigation.checkUpdateCalled(NotesListScreen)
        order.check(listOf(CLEAR,NAVIGATE))
    }
}
private const val NOTE_LIST_LIVEDATA_ADD = "NotesListLiveDataWrapper.Create"
private const val CREATE_NOTE_REPOSITORY = "NotesRepository.Create#createNote"
private interface FakeAddNoteLiveDataWrapper : NotesListLiveDataWrapper.Create {
    fun check(expected : NoteUi)
    class Base(
        private val order: Order
    ) : FakeAddNoteLiveDataWrapper {
        private lateinit var actual : NoteUi
        override fun check(expected: NoteUi) {
            assertEquals(expected, actual)
        }
        override fun create(noteUi : NoteUi) {
            actual= noteUi
            order.add(NOTE_LIST_LIVEDATA_ADD)
        }
    }
}
private interface FakeCreateNoteRepository : NotesRepository.Create {
    fun check(title : String, text : String)
    class Base(
        private val order: Order,
        private var noteId : Long
    ) : FakeCreateNoteRepository {
        private var actualTitle = ""
        private var actualText = ""

        override fun check(title: String, text: String) {
            assertEquals(title,actualTitle)
            assertEquals(text,actualText)
        }
        override suspend fun createNote(noteTitle: String, noteText: String) : Long {
            actualTitle = noteTitle
            actualText = noteText
            order.add(CREATE_NOTE_REPOSITORY)
            return noteId++
        }
    }
}