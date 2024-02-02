package com.example.notesapp.list

import android.widget.TextView

data class NoteUi(
    val id : Long,
    val title : String,
    val text : String,
    val lastDate : String
) {
    fun areItemsTheSame(noteUi: NoteUi) = id == noteUi.id
    fun areContentTheSame(noteUi: NoteUi) = title == noteUi.title && text == noteUi.text
    fun editNoteUi(editNoteUi : EditNoteUi) = editNoteUi.editNoteUi(this)
    fun show(titleTextView: TextView, dateTextView: TextView) {
        titleTextView.text = title
        dateTextView.text = lastDate
    }
}