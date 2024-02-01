package com.example.notesapp.create

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.notesapp.core.AbstractFragment
import com.example.notesapp.databinding.NoteCreateLayoutBinding

class CreateNoteFragment : AbstractFragment<NoteCreateLayoutBinding>() {
    override fun bind(inflater: LayoutInflater, container: ViewGroup?): NoteCreateLayoutBinding =
        NoteCreateLayoutBinding.inflate(inflater,container,false)
}