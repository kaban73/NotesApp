package com.example.notesapp.edit

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.notesapp.core.AbstractFragment
import com.example.notesapp.databinding.NoteEditLayoutBinding

class EditNoteFragment : AbstractFragment<NoteEditLayoutBinding>() {
    override fun bind(inflater: LayoutInflater, container: ViewGroup?): NoteEditLayoutBinding =
        NoteEditLayoutBinding.inflate(inflater,container,false)
}