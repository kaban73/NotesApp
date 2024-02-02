package com.example.notesapp.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.example.notesapp.core.AbstractFragment
import com.example.notesapp.core.ProvideViewModel
import com.example.notesapp.databinding.NoteCreateLayoutBinding

class CreateNoteFragment : AbstractFragment<NoteCreateLayoutBinding>() {
    override fun bind(inflater: LayoutInflater, container: ViewGroup?): NoteCreateLayoutBinding =
        NoteCreateLayoutBinding.inflate(inflater,container,false)

    private lateinit var viewModel: CreateNoteViewModel
    private val onBackPressedCallback = object :OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            viewModel.comeback()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as ProvideViewModel).viewModel(CreateNoteViewModel::class.java)
        requireActivity().onBackPressedDispatcher.addCallback(onBackPressedCallback)

        binding.createNoteButton.setOnClickListener {
            val titleText = binding.createNoteTitleEditText.text.toString()
            val textText = binding.createNoteTextEditText.text.toString()
            viewModel.createNote(titleText,textText)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        onBackPressedCallback.remove()
    }
}