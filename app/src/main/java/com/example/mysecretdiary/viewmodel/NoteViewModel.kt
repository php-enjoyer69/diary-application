package com.example.mysecretdiary.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projekt.R
import com.example.mysecretdiary.data.dao.NoteDao
import com.example.mysecretdiary.data.entity.Note
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

//ViewModel class for managing UI-related data for notes
//this ViewModel is responsible for handling operations related to notes such as insert, update, and delete
@HiltViewModel
class NoteViewModel @Inject constructor(private val noteDao: NoteDao): ViewModel() {

    //live data list of all notes retrieved from the DAO
    val notes = noteDao.getAllNotes()
    //channel for sending events to the UI
    val notesChannel = Channel<NotesEvent>()
    val notesEvent = notesChannel.receiveAsFlow()

    //inserts a new note into the database
    fun insertNote(note: Note) = viewModelScope.launch {
        noteDao.insertNote(note)
        notesChannel.send(NotesEvent.NavigateToNotesFragment)
    }

    //updates an existing note in the database
    fun updateNote(note: Note) = viewModelScope.launch {
        noteDao.updateNote(note)
        notesChannel.send(NotesEvent.NavigateToNotesFragment)
    }

    //deletes a note from the database and sends a Snackbar event to the UI
    fun deleteNote(context: Context, note: Note) = viewModelScope.launch {
        noteDao.deleteNote(note)
        val message = context.getString(R.string.delete_success)
        notesChannel.send(NotesEvent.ShowUndoSnackBar(msg = message, note))
    }

    //sealed class representing various events that can be sent to the UI
    sealed class NotesEvent{
        data class ShowUndoSnackBar(val msg: String, val note: Note): NotesEvent()
        object NavigateToNotesFragment: NotesEvent()
    }
}