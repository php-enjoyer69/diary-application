package com.example.projekt.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projekt.R
import com.example.projekt.data.dao.NoteDao
import com.example.projekt.data.entity.Note
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(private val noteDao: NoteDao): ViewModel() {

    val notes = noteDao.getAllNotes()
    val notesChannel = Channel<NotesEvent>()
    val notesEvent = notesChannel.receiveAsFlow()

    fun insertNote(note: Note) = viewModelScope.launch {
        noteDao.insertNote(note)
        notesChannel.send(NotesEvent.NavigateToNotesFragment)
    }

    fun updateNote(note: Note) = viewModelScope.launch {
        noteDao.updateNote(note)
        notesChannel.send(NotesEvent.NavigateToNotesFragment)
    }

    fun deleteNote(context: Context, note: Note) = viewModelScope.launch {
        noteDao.deleteNote(note)
        val message = context.getString(R.string.delete_success)
        notesChannel.send(NotesEvent.ShowUndoSnackBar(msg = message, note))
    }

    sealed class NotesEvent{
        data class ShowUndoSnackBar(val msg: String, val note: Note): NotesEvent()
        object NavigateToNotesFragment: NotesEvent()
    }
}