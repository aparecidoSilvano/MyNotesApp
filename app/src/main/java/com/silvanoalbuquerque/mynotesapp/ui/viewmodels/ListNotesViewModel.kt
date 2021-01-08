package com.silvanoalbuquerque.mynotesapp.ui.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.silvanoalbuquerque.mynotesapp.db.entities.Note
import com.silvanoalbuquerque.mynotesapp.repositories.NotesRepository

class ListNotesViewModel @ViewModelInject constructor(
    notesRepository: NotesRepository
) : ViewModel() {

    private val _notes = MediatorLiveData<List<Note>>()
    val notes: LiveData<List<Note>>
        get() = _notes

    /**
     * Variable that tells the Fragment to navigate to a specific [CreateNoteFragment]
     *
     * It's private because we don't want to expose settings this value to the Fragment.
     */
    private val _navigateToNote = MutableLiveData<Note>()

    /**
     * If it's non-null, immediately navigate to [CreateNoteFragment] an call [doneNavigating]
     */
    val navigateToNote: LiveData<Note>
        get() = _navigateToNote

    fun doneNavigating() {
        _navigateToNote.value = null
    }

    fun onNoteClicked(note: Note) {
        _navigateToNote.value = note
    }

    init {
        _notes.addSource(notesRepository.getAllNotes()) { result ->
            result?.let {
                _notes.value = it
            }
        }
    }
}