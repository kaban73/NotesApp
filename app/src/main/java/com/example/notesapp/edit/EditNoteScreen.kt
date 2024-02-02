package com.example.notesapp.edit

import androidx.fragment.app.FragmentManager
import com.example.notesapp.main.Screen

data class EditNoteScreen(private val noteId: Long) : Screen {
    override fun show(supportFragmentManager: FragmentManager, containerId: Int) {
        supportFragmentManager.beginTransaction()
            .replace(containerId,EditNoteFragment.newInstance(noteId))
            .commit()
    }
}