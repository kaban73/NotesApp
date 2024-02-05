package com.example.notesapp

import androidx.test.espresso.Espresso
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.notesapp.main.MainActivity
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.text.SimpleDateFormat
import java.util.Date

@RunWith(AndroidJUnit4::class)
class NotesAppTest {
    @get:Rule
    var activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)
    @Test
    fun create_note_recreate_and_back() {
        val notesListPage = NotesListPage()
        notesListPage.checkVisibleNow()

        notesListPage.clickAddButton()
        notesListPage.checkNotVisibleNow()

        val createNotePage = CreateNotePage()
        createNotePage.checkVisibleNow()

        val title = "first note"
        val text = "i am a first note"
        createNotePage.inputNoteTitle(title = title)
        createNotePage.inputNoteText(text = text)
        activityScenarioRule.scenario.recreate()
        createNotePage.checkEditNote(title, text)

        createNotePage.clickSaveButton()
        createNotePage.checkNotVisibleNow()
        val currentDate = SimpleDateFormat("dd/M/yyyy hh:mm")
            .format(Date())
        notesListPage.checkNote(position = 0, title = title, date = currentDate)
        notesListPage.checkVisibleNow()

        notesListPage.clickAddButton()
        notesListPage.checkNotVisibleNow()

        createNotePage.checkVisibleNow()
        createNotePage.checkEditNote("", "")
        Espresso.pressBack()
        notesListPage.checkNote(position = 0, title = title, date = currentDate)
        notesListPage.checkVisibleNow()
    }
    @Test
    fun create_note_edit_note_and_back() {
        val notesListPage = NotesListPage()
        notesListPage.checkVisibleNow()

        notesListPage.clickAddButton()
        notesListPage.checkNotVisibleNow()

        val createNotePage = CreateNotePage()
        createNotePage.checkVisibleNow()

        createNotePage.inputNoteTitle(title = "first note")
        createNotePage.inputNoteText(text = "i am a first note")

        createNotePage.clickSaveButton()
        createNotePage.checkNotVisibleNow()
        val currentDate = SimpleDateFormat("dd/M/yyyy hh:mm")
            .format(Date())
        notesListPage.checkNote(position = 0, title = "first note", date = currentDate)
        notesListPage.checkVisibleNow()

        notesListPage.clickNoteAt(0)
        notesListPage.checkNotVisibleNow()

        val editNotePage = EditNotePage()
        editNotePage.checkVisibleNow(title = "first note",
            text = "i am a first note")

        editNotePage.replaceText(title = "second note", text = "i am a second note now")
        activityScenarioRule.scenario.recreate()
        editNotePage.checkVisibleNow(title = "second note", text = "i am a second note now")

        editNotePage.clickSaveButton()

        editNotePage.checkNotVisibleNow()
        val lastCurrentDate = SimpleDateFormat("dd/M/yyyy hh:mm")
            .format(Date())
        notesListPage.checkNote(position = 0, title = "second note", date = lastCurrentDate)
        notesListPage.checkVisibleNow()

        notesListPage.clickNoteAt(0)
        notesListPage.checkNotVisibleNow()

        editNotePage.checkVisibleNow(title = "second note", text = "i am a second note now")
        activityScenarioRule.scenario.recreate()
        editNotePage.checkVisibleNow(title = "second note", text = "i am a second note now")
        Espresso.pressBack()

        editNotePage.checkNotVisibleNow()
        val lastCurrentDate1 = SimpleDateFormat("dd/M/yyyy hh:mm")
            .format(Date())
        notesListPage.checkNote(position = 0, title = "second note", date = lastCurrentDate1)
        notesListPage.checkVisibleNow()
    }
    @Test
    fun create_note_delete_note() {
        val notesListPage = NotesListPage()
        notesListPage.checkVisibleNow()

        notesListPage.clickAddButton()
        notesListPage.checkNotVisibleNow()

        val createNotePage = CreateNotePage()
        createNotePage.checkVisibleNow()

        createNotePage.inputNoteTitle(title = "first note")
        createNotePage.inputNoteText(text = "i am a first note")

        createNotePage.clickSaveButton()
        createNotePage.checkNotVisibleNow()
        val currentDate1 = SimpleDateFormat("dd/M/yyyy hh:mm")
            .format(Date())
        notesListPage.checkNote(position = 0, title = "first note", date = currentDate1)
        notesListPage.checkVisibleNow()

        notesListPage.clickAddButton()
        notesListPage.checkNotVisibleNow()

        createNotePage.checkVisibleNow()

        createNotePage.inputNoteTitle(title = "second note")
        createNotePage.inputNoteText(text = "i am a second note")

        createNotePage.clickSaveButton()
        createNotePage.checkNotVisibleNow()
        val currentDate2 = SimpleDateFormat("dd/M/yyyy hh:mm")
            .format(Date())
        notesListPage.checkNote(position = 0, title = "first note", date = currentDate1)
        notesListPage.checkNote(position = 1, title = "second note", date = currentDate2)
        notesListPage.checkVisibleNow()

        notesListPage.clickNoteAt(0)
        notesListPage.checkNotVisibleNow()

        val editNotePage = EditNotePage()
        editNotePage.checkVisibleNow(title = "first note",
            text = "i am a first note")

        editNotePage.clickDeleteButton()
        editNotePage.checkNotVisibleNow()
        notesListPage.checkNote(position = 0, title = "second note", date = currentDate2)
        notesListPage.checkVisibleNow()
        activityScenarioRule.scenario.recreate()
        notesListPage.checkNote(position = 0, title = "second note", date = currentDate2)
    }
}