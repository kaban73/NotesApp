package com.example.notesapp.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.notesapp.core.AbstractFragment
import com.example.notesapp.core.ProvideViewModel
import com.example.notesapp.databinding.NotesListLayoutBinding

class NotesListFragment : AbstractFragment<NotesListLayoutBinding>() {
    override fun bind(inflater: LayoutInflater, container: ViewGroup?): NotesListLayoutBinding =
        NotesListLayoutBinding.inflate(inflater,container,false)

    private lateinit var viewModel: NotesListViewModel
    private val adapter = NotesListAdapter(object :EditNoteUi {
        override fun editNoteUi(noteUi: NoteUi) {
            viewModel.editNote(noteUi)
        }

    })
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as ProvideViewModel).viewModel(NotesListViewModel::class.java)
        binding.notesRecyclerView.adapter = adapter

        viewModel.liveData().observe(viewLifecycleOwner) {
            adapter.update(it)
        }

        binding.addButton.setOnClickListener {
            viewModel.createNote()
        }

        viewModel.init()
    }
}