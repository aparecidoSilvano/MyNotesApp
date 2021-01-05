package com.silvanoalbuquerque.mynotesapp.ui.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.silvanoalbuquerque.mynotesapp.db.entities.Note
import com.silvanoalbuquerque.mynotesapp.other.Constants.COLOR_PICKER_FIRST_VALUE
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

    private val _selectedColor = MutableLiveData<String>()
    val selectedColor: LiveData<String>
        get() = _selectedColor

    private val _selectedImagePath = MutableLiveData<String>()
    val selectedImagePath: LiveData<String>
        get() = _selectedImagePath

    init {
        _notes.addSource(notesRepository.getAllNotes()) { result ->
            result?.let {
                _notes.value = it
            }
        }

        _selectedColor.value = COLOR_PICKER_FIRST_VALUE
    }

    fun insertNote(note: Note) = viewModelScope.launch {
        notesRepository.insertNote(note)

        _finishedSavingNote.value = true
    }

    fun setSelectedColor(selectedColor: String) {
        _selectedColor.postValue(selectedColor)
    }

    fun setSelectedImagePath(selectedImagePath: String) {
        _selectedImagePath.postValue(selectedImagePath)
    }
}