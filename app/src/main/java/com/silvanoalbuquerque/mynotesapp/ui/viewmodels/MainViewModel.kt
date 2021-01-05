package com.silvanoalbuquerque.mynotesapp.ui.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.silvanoalbuquerque.mynotesapp.db.entities.Note
import com.silvanoalbuquerque.mynotesapp.repositories.NotesRepository
import kotlinx.coroutines.launch

class MainViewModel @ViewModelInject constructor(
    private val notesRepository: NotesRepository
) : ViewModel() {

    private val _notes = MediatorLiveData<List<Note>>()
    val notes: LiveData<List<Note>>
        get() = _notes

    private val _finishedSavingNote = MutableLiveData<Boolean>()
    val finishedSavingNote: LiveData<Boolean>
        get() = _finishedSavingNote

    val selectedColor = MutableLiveData<String>()

    init {
        _notes.addSource(notesRepository.getAllNotes()) { result ->
            result?.let {
                _notes.value = it
            }
        }

        selectedColor.value = "#333333"
    }

    fun insertNote(note: Note) = viewModelScope.launch {
        notesRepository.insertNote(note)

        _finishedSavingNote.value = true
    }
}