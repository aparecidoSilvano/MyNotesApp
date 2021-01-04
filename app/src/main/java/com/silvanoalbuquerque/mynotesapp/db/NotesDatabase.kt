package com.silvanoalbuquerque.mynotesapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.silvanoalbuquerque.mynotesapp.db.entities.Note

@Database(
    entities = [Note::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class NotesDatabase : RoomDatabase() {

    abstract fun getNotesDao(): NotesDAO
}