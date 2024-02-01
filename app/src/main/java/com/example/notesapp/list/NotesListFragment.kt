package com.example.notesapp.list

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.notesapp.core.AbstractFragment
import com.example.notesapp.databinding.NotesListLayoutBinding

class NotesListFragment : AbstractFragment<NotesListLayoutBinding>() {
    override fun bind(inflater: LayoutInflater, container: ViewGroup?): NotesListLayoutBinding =
        NotesListLayoutBinding.inflate(inflater,container,false)
}