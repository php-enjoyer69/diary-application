package com.example.projekt.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.projekt.R
import com.example.projekt.adapter.NoteAdapter
import com.example.projekt.data.entity.Note
import com.example.projekt.databinding.FragmentNotesBinding
import com.example.projekt.viewmodel.NoteViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


//fragment for displaying a list of notes
@AndroidEntryPoint
class NoteFragment: Fragment(R.layout.fragment_notes), NoteAdapter.OnNoteClickListener {

    //ViewModel for managing UI-related data
    val viewModel by viewModels<NoteViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //binding object for accessing UI components
        val binding = FragmentNotesBinding.bind(requireView())

        binding.apply {
            //set up RecyclerView with GridLayoutManager
            recyclerViewNotes.layoutManager = GridLayoutManager(context, 2)
            recyclerViewNotes.setHasFixedSize(true)

            //set click listener for the add button
            addBtn.setOnClickListener {
                val action = NoteFragmentDirections.actionNoteFragmentToAddEditNoteFragment(null)
                findNavController().navigate(action)
            }

            //collect notes from ViewModel and set the adapter
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.notes.collect {notes ->
                    val adapter = NoteAdapter(notes, this@NoteFragment)
                    recyclerViewNotes.adapter = adapter
                }
            }
            //collect events from ViewModel and handle them
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.notesEvent.collect {event ->
                    if(event is NoteViewModel.NotesEvent.ShowUndoSnackBar){
                        Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_LONG).setAction(getString(R.string.undo)){
                            viewModel.insertNote(event.note)
                        }.show()
                    }

                }
            }
        }
    }

    //handle note click events
    override fun onNoteClick(note: Note) {
        val action = NoteFragmentDirections.actionNoteFragmentToAddEditNoteFragment(note)
        findNavController().navigate(action)
    }

    //handle note long click events
    override fun onNoteLongClick(note: Note) {
        viewModel.deleteNote(requireContext(), note)
    }
}