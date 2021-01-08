package com.silvanoalbuquerque.mynotesapp.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.silvanoalbuquerque.mynotesapp.db.entities.Note

@Dao
interface NoteDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    @Transaction
    @Query("SELECT * FROM note ORDER BY id DESC")
    fun getAllNotes(): LiveData<List<Note>>

    @Transaction
    @Query("SELECT * FROM note ORDER BY datetime")
    fun getAllNotesOrderedByDate(): LiveData<List<Note>>

    @Query("SELECT * FROM note WHERE id =:noteId")
    suspend fun getNoteById(noteId: Long): Note?

    @Delete
    suspend fun delete(note: Note)
}
