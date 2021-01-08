package com.silvanoalbuquerque.mynotesapp.ui.fragments.createnote

import android.net.Uri
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.silvanoalbuquerque.mynotesapp.db.entities.Note
import com.silvanoalbuquerque.mynotesapp.db.entities.NoteColor
import com.silvanoalbuquerque.mynotesapp.repositories.NotesRepository
import kotlinx.coroutines.launch
import java.util.*

class NoteViewModel @ViewModelInject constructor(
    private val notesRepository: NotesRepository
) : ViewModel() {

    private val _note = MutableLiveData<NoteDTO>()
    val note: LiveData<NoteDTO>
        get() = _note

    private val _selectedColor = MutableLiveData<Boolean>()
    val selectedColor: LiveData<Boolean>
        get() = _selectedColor

    private val _finishedSavingNote = MutableLiveData<Boolean>()
    val finishedSavingNote: LiveData<Boolean>
        get() = _finishedSavingNote

    private val _openImageSelector = MutableLiveData<Boolean>()
    val openImageSelector: LiveData<Boolean>
        get() = _openImageSelector

    private val _selectedImage = MutableLiveData<Uri>()
    val selectedImage: LiveData<Uri>
        get() = _selectedImage

    private val _noteLink = MutableLiveData<String>()
    val noteLink: LiveData<String>
        get() = _noteLink

    private val _navigateBack = MutableLiveData<Boolean>()
    val navigateBack: LiveData<Boolean>
        get() = _navigateBack

    fun loadNote(noteKey: Long) {
        viewModelScope.launch {
            val note = notesRepository.getNoteById(noteKey)

            val noteDTO = if (note == null) {
                NoteDTO()
            } else {
                NoteDTO(
                    id = note.id,
                    title = note.title,
                    subtitle = note.subtitle,
                    datetime = note.datetime,
                    noteText = note.noteText,
                    imagePath = note.imagePath,
                    color = note.color,
                    webLink = note.webLink
                )
            }

            _note.value = noteDTO
        }
    }

    fun saveNote() {
        // TODO add validation
        val noteDTO = _note.value!!

        val note = Note(
            id = noteDTO.id,
            title = noteDTO.title,
            subtitle = noteDTO.subtitle,
            noteText = noteDTO.noteText,
            color = noteDTO.color,
            webLink = noteDTO.webLink,
            imagePath = noteDTO.imagePath,
            datetime = Calendar.getInstance()
        )

        insertNote(note)
    }

    fun selectImage() {
        _openImageSelector.value = true
    }

    fun setSelectedImage(imageUri: Uri, selectedImagePath: String) {
        _selectedImage.value = imageUri
        _note.value?.imagePath = selectedImagePath
    }

    fun setWebLink(selected: String) {
        _noteLink.postValue(selected)
        _note.value?.webLink = selected
    }

    fun onSelectedColor(color: NoteColor) {
        _note.value?.color = color
        _selectedColor.value = true
    }

    fun navigateBack() {
        _navigateBack.value = true
    }

    private fun insertNote(note: Note) = viewModelScope.launch {
        notesRepository.insertNote(note)
        _finishedSavingNote.value = true
    }
}