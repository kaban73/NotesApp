package com.example.notesapp.edit

import androidx.lifecycle.LiveData
import com.example.notesapp.core.FakeClearViewModel
import com.example.notesapp.core.FakeClearViewModel.Companion.CLEAR
import com.example.notesapp.list.Order
import com.example.notesapp.main.FakeNavigation
import com.example.notesapp.main.FakeNavigation.Companion.NAVIGATE
import kotlinx.coroutines.Dispatchers
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class EditNoteViewModelTest {
    private lateinit var order: Order
    private lateinit var repository: FakeEditNoteRepository
    private lateinit var navigation: FakeNavigation.Update
    private lateinit var clear : FakeClearViewModel
    private lateinit var noteLiveDataWrapper: FakeNoteLiveDataWrapper
    private lateinit var notesListLiveDataWrapper: FakeNotesListLiveDataWrapper
    private lateinit var viewModel : EditNoteViewModel
    @Before
    fun setup() {
        order= Order()
        repository = FakeEditNoteRepository.Base(order)
        navigation = FakeNavigation.Base(order)
        clear = FakeClearViewModel.Base(order)
        noteLiveDataWrapper = FakeNoteLiveDataWrapper.Base(order)
        notesListLiveDataWrapper = FakeNotesListLiveDataWrapper.Base(order)
        viewModel = EditNoteViewModel(
            noteLiveDataWrapper = noteLiveDataWrapper,
            notesListLiveDataWrapper = notesListLiveDataWrapper,
            repository = repository,
            navigation = navigation,
            clear = clear,
            dispatcher = Dispatchers.Unconfined,
            dispatcherMain = Dispatchers.Unconfined
        )
    }

    @Test
    fun test_init() {
        viewModel.init(noteId = 1L)

        repository.checkNote(1L)
        noteLiveDataWrapper.check(MyNote(id = 1L, title = "first note", text = "i am a first note"))
        order.check(listOf(REPOSITORY_NOTE, NOTE_LIVE_DATA))
    }
    @Test
    fun test_delete() {
        viewModel.deleteNote(noteId = 1L)

        repository.checkDelete(1L)
        clear.check(listOf(EditNoteViewModel::class.java))
        navigation.checkUpdateCalled(NotesListScreen)
        order.check(listOf(REPOSITORY_DELETE,CLEAR,NAVIGATE))
    }
    @Test
    fun test_edit() {
        viewModel.editNote(noteId = 2L, newTitle = "a new title", newText = "this is a new text")

        repository.checkEdit(2L, "a new title", "this is a new text")
        notesListLiveDataWrapper.check(2L, "a new title", "this is a new text")
        clear.check(listOf(EditNoteViewModel::class.java))
        navigation.checkUpdateCalled(NotesListScreen)
        order.check(listOf(REPOSITORY_EDIT, NOTES_LIVE_DATA_UPDATE, CLEAR, NAVIGATE))
    }
    @Test
    fun comeback() {
        viewModel.comeback()

        clear.check(EditNoteViewModel::class.java)
        navigation.checkUpdateCalled(NotesListScreen)
        order.check(listOf(CLEAR, NAVIGATE))
    }
}
private const val NOTE_LIVE_DATA = "NoteLiveDataWrapper#update"
private const val REPOSITORY_DELETE = "NotesRepository.Edit#delete"
private const val REPOSITORY_EDIT = "NotesRepository.Edit#edit"
private const val REPOSITORY_NOTE = "NotesRepository.Edit#note"
private const val NOTES_LIVE_DATA_UPDATE = "NoteListLiveDataWrapper.Update#update"
private interface FakeNoteLiveDataWrapper : NoteLiveDataWrapper.Mutable {
    fun check(expected : MyNote)
    class Base(
        private val order: Order
    ) : FakeNoteLiveDataWrapper {
        private lateinit var actual :MyNote
        override fun check(expected: MyNote) {
            assertEquals(expected, actual)
        }
        override fun liveData() : LiveData<MyNote> {
            throw IllegalStateException("Don't use in Unit Test")
        }
        override fun update(myNote : MyNote) {
            actual = myNote
            order.add(NOTE_LIVE_DATA)
        }
    }
}
private interface FakeEditNoteRepository : NotesRepository.Edit {
    fun checkNote(expectedNoteId: Long)
    fun checkDelete(expectedNoteId: Long)
    fun checkEdit(expectedNoteId: Long,expectedNewTitle: String,expectedNewText: String)
    class Base(
        private val order: Order
    ) : FakeEditNoteRepository {
        private var actualId = -1L
        private var actualTitle = ""
        private var actualText = ""
        override fun checkDelete(expectedNoteId: Long) {
            assertEquals(expectedNoteId, actualId)
        }

        override fun checkNote(expectedNoteId: Long) {
            assertEquals(expectedNoteId, actualId)
        }

        override fun checkEdit(
            expectedNoteId: Long,
            expectedNewTitle: String,
            expectedNewText: String
        ) {
            assertEquals(expectedNoteId, actualId)
            assertEquals(expectedNewTitle,actualTitle)
            assertEquals(expectedNewText, actualText)
        }
        override suspend fun deleteNote(noteId: Long) {
            actualId = noteId
            order.add(REPOSITORY_DELETE)
        }
        override suspend fun editNote(noteId : Long, newTitle : String, newText : String) {
            actualId = noteId
            actualTitle = newTitle
            actualText = newText
            order.add(REPOSITORY_EDIT)
        }
        override suspend fun note(noteId: Long) : MyNote {
            actualId = noteId
            order.add(REPOSITORY_NOTE)
            return MyNote(id = noteId, title = "first note", text = "i am a first note")
        }
    }
}
private interface FakeNotesListLiveDataWrapper : NotesListLiveDataWrapper.Update {
    fun check(expectedNoteId : Long, expectedNewTitle : String, expectedNewText: String)
    class Base(
        private val order: Order
    ) : FakeNotesListLiveDataWrapper {
        private var actualId = -1L
        private var actualTitle = ""
        private var actualText = ""
        override fun check(
            expectedNoteId: Long,
            expectedNewTitle: String,
            expectedNewText: String
        ) {
            assertEquals(expectedNoteId, actualId)
            assertEquals(expectedNewTitle,actualTitle)
            assertEquals(expectedNewText, actualText)
        }
        override fun update(noteId : Long, newTitle : String, newText : String) {
            actualId = noteId
            actualTitle = newTitle
            actualText = newText
            order.add(NOTES_LIVE_DATA_UPDATE)
        }
    }
}