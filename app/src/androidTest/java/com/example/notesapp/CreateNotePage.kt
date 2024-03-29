package com.example.notesapp

import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withParent
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.google.android.material.textfield.TextInputEditText
import org.hamcrest.CoreMatchers.allOf

class CreateNotePage {
    private val rootId : Int = R.id.createNoteLayout
    private fun title() = onView(
        allOf(
            withParent(isAssignableFrom(LinearLayout::class.java)),
            withParent(withId(rootId)),
            isAssignableFrom(TextView::class.java),
            withId(R.id.createNoteTextView),
            withText("Create note:")
        )
    )
    private fun editTitle() = onView(
        allOf(
            withParent(isAssignableFrom(LinearLayout::class.java)),
            withParent(withId(rootId)),
            isAssignableFrom(TextInputEditText::class.java),
            withId(R.id.createNoteTitleEditText)
        )
    )
    private fun editText() = onView(
        allOf(
            withParent(isAssignableFrom(LinearLayout::class.java)),
            withParent(withId(rootId)),
            isAssignableFrom(TextInputEditText::class.java),
            withId(R.id.createNoteTextEditText)
        )
    )
    fun inputNoteTitle(title : String) {
        editTitle().perform(typeText(title), closeSoftKeyboard())
    }
    fun inputNoteText(text : String) {
        editText().perform(typeText(text), closeSoftKeyboard())
    }
    fun clickSaveButton() {
        onView(
            allOf(
                withParent(isAssignableFrom(LinearLayout::class.java)),
                withParent(withId(rootId)),
                isAssignableFrom(Button::class.java),
                withId(R.id.createNoteButton),
                withText("Create")
            )
        ).perform(click())
    }
    fun checkVisibleNow() {
        title().check(matches(isDisplayed()))
    }
    fun checkNotVisibleNow() {
        title().check(doesNotExist())
    }
    fun checkEditNote(title : String, text : String) {
        editTitle().check(matches(withText(title)))
        editText().check(matches(withText(text)))
    }
}