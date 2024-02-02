package com.example.notesapp.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.example.notesapp.core.AbstractFragment
import com.example.notesapp.core.ProvideViewModel
import com.example.notesapp.databinding.NoteEditLayoutBinding

class EditNoteFragment : AbstractFragment<NoteEditLayoutBinding>() {
    companion object {
        fun newInstance(
            noteId : Long
        ) : EditNoteFragment {
            val instance = EditNoteFragment()
            instance.arguments = Bundle().apply {
                putLong(KEY,noteId)
            }
            return instance
        }
        private const val KEY = "noteIdKey"
    }
    override fun bind(inflater: LayoutInflater, container: ViewGroup?): NoteEditLayoutBinding =
        NoteEditLayoutBinding.inflate(inflater,container,false)

    private lateinit var viewModel: EditNoteViewModel
    private val onBackPressedCallback = object :OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            viewModel.comeback()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val noteId = requireArguments().getLong(KEY)
        viewModel = (activity as ProvideViewModel).viewModel(EditNoteViewModel::class.java)
        requireActivity().onBackPressedDispatcher.addCallback(onBackPressedCallback)

        viewModel.liveData().observe(viewLifecycleOwner) {
            binding.noteTitleEditText.setText(it.title)
            binding.noteTextEditText.setText(it.text)
        }

        binding.saveNoteButton.setOnClickListener {
            val newTitle = binding.noteTitleEditText.text.toString()
            val newText = binding.noteTextEditText.text.toString()
            viewModel.editNote(noteId, newTitle,newText)
        }
        binding.deleteNoteButton.setOnClickListener {
            viewModel.deleteNote(noteId)
        }

        viewModel.init(noteId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        onBackPressedCallback.remove()
    }
}