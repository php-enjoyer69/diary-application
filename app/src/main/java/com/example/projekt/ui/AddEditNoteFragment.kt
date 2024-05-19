package com.example.projekt.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.projekt.R
import com.example.projekt.data.entity.Note
import com.example.projekt.databinding.FragmentAddeditnotesBinding
import com.example.projekt.viewmodel.NoteViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

//a fragment for adding and editing notes
@AndroidEntryPoint
class AddEditNoteFragment: Fragment(R.layout.fragment_addeditnotes)  {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //ViewModel for managing UI-related data
        val viewModel by viewModels<NoteViewModel>()
        //binding object for accessing UI components
        val binding = FragmentAddeditnotesBinding.bind(requireView())
        //arguments passed to this fragment
        val args: AddEditNoteFragmentArgs by navArgs()
        val note = args.note

        //if editing an existing note
        if(note != null){
            binding.apply {
                titleEdit.setText(note.title)
                contentEdit.setText(note.content)
                saveBtn.setOnClickListener {
                    val title = titleEdit.text.toString()
                    val content = contentEdit.text.toString()
                    val updateNote = note.copy(title = title, content = content, date = System.currentTimeMillis())
                    viewModel.updateNote(updateNote)
                }
            }
        }else{ //if adding a new note
            binding.apply {
                saveBtn.setOnClickListener {
                    val title = titleEdit.text.toString()
                    val content = contentEdit.text.toString()
                    val note = Note(title = title, content = content, date = System.currentTimeMillis())
                    viewModel.insertNote(note)
                }
            }
        }

        //collect events from the ViewModel
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.notesEvent.collect{event ->
                if(event is NoteViewModel.NotesEvent.NavigateToNotesFragment){
                    val action = AddEditNoteFragmentDirections.actionAddEditNoteFragmentToNoteFragment()
                    findNavController().navigate(action)
                }
            }
        }
    }
}