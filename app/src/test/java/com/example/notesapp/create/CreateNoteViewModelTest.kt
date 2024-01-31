package com.example.notesapp.create

import com.example.notesapp.core.FakeClearViewModel
import com.example.notesapp.core.FakeClearViewModel.Companion.CLEAR
import com.example.notesapp.list.Order
import com.example.notesapp.main.FakeNavigation
import com.example.notesapp.main.FakeNavigation.Companion.NAVIGATE
import kotlinx.coroutines.Dispatchers
import org.junit.Before
import org.junit.Test

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
            navigation = navigation,
            clear = clear,
            dispatcher = Dispatchers.Unconfined,
            dispatcherMain = Dispatchers.Unconfined
        )
    }
    @Test
    fun test_create() {
        viewModel.createNote(title = "new note", text = "I am a new note")

        notesRepository.check("new note","I am a new note")
        addNoteLiveDataWrapper.check(NoteUi(id = 1L, title = "new note", text = "I am a new note"))
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
private const val NOTE_LIST_LIVEDATA_ADD = "NoteListLiveDataWrapper.Create"
private const val CREATE_NOTE_REPOSITORY = "NotesRepository.Create#createNote"
private interface FakeAddNoteLiveDataWrapper : NotesListViewModel.Create {
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
        private val noteId : Long
    ) : FakeCreateNoteRepository {
        private var actualTitle = ""
        private var actualText = ""

        override fun check(title: String, text: String) {
            assertEquals(title,actualTitle)
            assertEquals(text,actualText)
        }
        override suspend fun createNote(title : String, text : String) : Long {
            actualTitle = title
            actualText = text
            order.add(CREATE_NOTE_REPOSITORY)
            return noteId++
        }
    }
}