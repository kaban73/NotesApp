package com.example.notesapp

import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.clearText
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

class EditNotePage {
    private val rootId : Int = R.id.editNoteLayout
    private fun title() = onView(
        allOf(
            withParent(isAssignableFrom(LinearLayout::class.java)),
            withParent(withId(rootId)),
            isAssignableFrom(TextView::class.java),
            withId(R.id.editNoteTextView),
            withText("Edit note:")
        )
    )
    private val inputTitleView = onView(
        allOf(
            isAssignableFrom(TextInputEditText::class.java),
            withId(R.id.noteTitleEditText),
            withParent(isAssignableFrom(LinearLayout::class.java)),
            withParent(withId(rootId))
        )
    )
    private val inputTextView = onView(
        allOf(
            isAssignableFrom(TextInputEditText::class.java),
            withId(R.id.noteTextEditText),
            withParent(isAssignableFrom(LinearLayout::class.java)),
            withParent(withId(rootId))
        )
    )
    fun checkVisibleNow(title : String, text : String) {
        title().check(matches(isDisplayed()))
        inputTitleView.check(matches(withText(title)))
        inputTextView.check(matches(withText(text)))
    }
    fun checkNotVisibleNow() {
        title().check(doesNotExist())
        inputTitleView.check(doesNotExist())
        inputTextView.check(doesNotExist())
    }
    fun replaceText(title : String, text : String) {
        inputTitleView.perform(clearText(), typeText(title), closeSoftKeyboard())
        inputTextView.perform(clearText(), typeText(text), closeSoftKeyboard())
    }
    fun clickSaveButton() {
        onView(
            allOf(
                withParent(isAssignableFrom(LinearLayout::class.java)),
                withParent(withId(rootId)),
                isAssignableFrom(Button::class.java),
                withId(R.id.saveNoteButton),
                withText("Save")
            )
        ).perform(click())
    }
    fun clickDeleteButton() {
        onView(
            allOf(
                withParent(isAssignableFrom(LinearLayout::class.java)),
                withParent(withId(rootId)),
                isAssignableFrom(Button::class.java),
                withId(R.id.deleteNoteButton),
                withText("Delete")
            )
        ).perform(click())
    }
}