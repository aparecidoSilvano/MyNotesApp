package com.silvanoalbuquerque.mynotesapp.repositories

import com.silvanoalbuquerque.mynotesapp.db.NoteDAO
import com.silvanoalbuquerque.mynotesapp.db.entities.Note
import javax.inject.Inject

class NotesRepository @Inject constructor(
    private val noteDao: NoteDAO
) {
    suspend fun insertNote(note: Note) = noteDao.insertNote(note)

    fun getAllNotes() =
        noteDao.getAllNotes()

    suspend fun getNoteById(noteId: Long) = noteDao.getNoteById(noteId)
}