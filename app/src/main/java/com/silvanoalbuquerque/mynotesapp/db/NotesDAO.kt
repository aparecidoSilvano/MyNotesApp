package com.silvanoalbuquerque.mynotesapp.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.silvanoalbuquerque.mynotesapp.db.entities.Note

@Dao
interface NotesDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    @Transaction
    @Query("SELECT * FROM notes_table ORDER BY datetime")
    fun getAllNotesOrderedByDate(): LiveData<List<Note>>
}
