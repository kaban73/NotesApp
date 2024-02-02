package com.example.notesapp.list

import androidx.lifecycle.LiveData
import com.example.notesapp.core.MyNote
import com.example.notesapp.core.NotesRepository
import com.example.notesapp.create.CreateNoteScreen
import com.example.notesapp.edit.EditNoteScreen
import com.example.notesapp.main.FakeNavigation
import com.example.notesapp.main.FakeNavigation.Companion.NAVIGATE
import kotlinx.coroutines.Dispatchers
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Date

class NotesListViewModelTest {
    private lateinit var notesViewModel : NotesListViewModel
    private lateinit var notesListLiveDataWrapper: FakeNotesListLiveDataWrapper
    private lateinit var notesRepository: FakeNotesRepository
    private lateinit var navigation: FakeNavigation.Update
    private lateinit var order : Order
    @Before
    fun setup() {
        order = Order()
        navigation = FakeNavigation.Base(order)
        notesRepository = FakeNotesRepository.Base(order)
        notesListLiveDataWrapper = FakeNotesListLiveDataWrapper.Base(order)
        notesViewModel = NotesListViewModel(
            notesRepository = notesRepository,
            notesListLiveDataWrapper = notesListLiveDataWrapper,
            navigation = navigation as FakeNavigation.Base,
            dispatcher = Dispatchers.Unconfined,
            dispatcherMain = Dispatchers.Unconfined
        )
    }

    @Test
    fun test_init() {
        val currentDate = SimpleDateFormat("dd/M/yyyy hh:mm")
            .format(Date())
        notesRepository.expectNotes(
            listOf(
                MyNote(id = 1L, title = "first note", text = "this is a first note!", lastDate = currentDate),
                MyNote(id = 2L, title = "second note", text = "this is a second note!", lastDate = currentDate)
            )
        )
        notesViewModel.init()
        notesListLiveDataWrapper.checkCalledList(
            listOf(
                NoteUi(id = 1L, title = "first note", text = "this is a first note!", lastDate = currentDate),
                NoteUi(id = 2L, title = "second note", text = "this is a second note!", lastDate = currentDate)
            )
        )
        order.check(listOf(NOTES_REPOSITORY_READ, UPDATE_NOTES_LIVEDATA))
    }
    @Test
    fun test_create_note() {
        notesViewModel.createNote()
        navigation.checkUpdateCalled(CreateNoteScreen)
        order.check(listOf(NAVIGATE))
    }
    @Test
    fun test_edit_note() {
        val currentDate = SimpleDateFormat("dd/M/yyyy hh:mm")
            .format(Date())
        notesRepository.expectNotes(
            listOf(
                MyNote(id = 1L, title = "first note", text = "this is a first note!", lastDate = currentDate),
                MyNote(id = 2L, title = "second note", text = "this is a second note!", lastDate = currentDate)
            )
        )
        val currentDate2 = SimpleDateFormat("dd/M/yyyy hh:mm")
            .format(Date())
        notesViewModel.editNote(noteUi =
        NoteUi(id = 1L, title = "first note", text = "this is a first note!", lastDate = currentDate2))
        navigation.checkUpdateCalled(EditNoteScreen(noteId = 1L))
        order.check(listOf(NAVIGATE))
    }
}
private const val NOTES_REPOSITORY_READ = "NotesRepository.ReadList#notesList"
private const val UPDATE_NOTES_LIVEDATA = "NotesListLiveDate.Mutable#update"
interface FakeNotesListLiveDataWrapper : NotesListLiveDataWrapper.Mutable{
    fun checkCalledList(expected : List<NoteUi>)
    class Base(
        private val order: Order
    ) : FakeNotesListLiveDataWrapper {
        private val actualList = mutableListOf<NoteUi>()
        override fun checkCalledList(expected: List<NoteUi>) {
            assertEquals(expected,actualList)
        }
        override fun liveData() : LiveData<List<NoteUi>> {
            throw IllegalStateException("Not used in this test")
        }
        override fun update(value : List<NoteUi>) {
            actualList.clear()
            actualList.addAll(value)
            order.add(UPDATE_NOTES_LIVEDATA)
        }
    }
}

private interface FakeNotesRepository : NotesRepository.ReadList {
    fun expectNotes(list : List<MyNote>)
    class Base(
        private val order : Order
    ) : FakeNotesRepository {
        private val list = mutableListOf<MyNote>()
        override suspend fun notesList() : List<MyNote> {
            order.add(NOTES_REPOSITORY_READ)
            return list
        }
        override fun expectNotes(list: List<MyNote>) {
            this.list.clear()
            this.list.addAll(list)
        }
    }
}

class Order {
    private val list = mutableListOf<String>()
    fun add(action : String) {
        list.add(action)
    }
    fun check(expected : List<String>) {
        assertEquals(expected, list)
    }
}