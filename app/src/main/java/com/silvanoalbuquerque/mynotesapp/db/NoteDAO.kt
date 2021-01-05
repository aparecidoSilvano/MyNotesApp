package com.silvanoalbuquerque.mynotesapp.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.silvanoalbuquerque.mynotesapp.db.entities.Note

@Dao
interface NoteDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    @Transaction
    @Query("SELECT * FROM notes_table ORDER BY id DESC")
    fun getAllNotes(): LiveData<List<Note>>

    @Transaction
    @Query("SELECT * FROM notes_table ORDER BY datetime")
    fun getAllNotesOrderedByDate(): LiveData<List<Note>>

    @Delete
    suspend fun delete(note: Note)
}
