package com.example.notesapp

import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withParent
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.hamcrest.CoreMatchers.allOf

class NotesListPage {
    private val rootId : Int = R.id.notesListLayout
    private fun recyclerViewMatcher() = RecyclerViewMatcher(R.id.notesRecyclerView)

    private fun title() = onView(
        allOf(
            withText("Your notes:"),
            isAssignableFrom(TextView::class.java),
            withId(R.id.notesTitleTextView),
            withParent(isAssignableFrom(ConstraintLayout::class.java)),
            withParent(withId(rootId))
        )
    )
    fun checkVisibleNow() {
        title().check(matches(isDisplayed()))
    }
    fun clickAddButton() {
        onView(
            allOf(
                withId(R.id.addButton),
                isAssignableFrom(FloatingActionButton::class.java),
                withParent(isAssignableFrom(ConstraintLayout::class.java)),
                withParent(withId(rootId))
            )
        ).perform(click())
    }
    fun checkNotVisibleNow() {
        title().check(doesNotExist())
    }
    fun checkNote(position : Int, title : String, date : String) {
        val notesLinearLayout : Int = R.id.notesLinearLayout
        onView(
            allOf(
                isAssignableFrom(TextView::class.java),
                withParent(withId(notesLinearLayout)),
                withParent(isAssignableFrom(LinearLayout::class.java)),
                recyclerViewMatcher().atPosition(position,R.id.notesTitleTextView)
            )
        ).check(matches(withText(title)))

        onView(
            allOf(
                isAssignableFrom(TextView::class.java),
                withParent(withId(notesLinearLayout)),
                withParent(isAssignableFrom(LinearLayout::class.java)),
                recyclerViewMatcher().atPosition(position,R.id.notesDateTextView)
            )
        ).check(matches(withText(date)))
    }
    fun clickNoteAt(position: Int) {
        onView(recyclerViewMatcher().atPosition(position)).perform(click())
    }
}