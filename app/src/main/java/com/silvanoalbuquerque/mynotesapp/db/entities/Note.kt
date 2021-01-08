package com.silvanoalbuquerque.mynotesapp.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "note")
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    val title: String? = null,
    val subtitle: String? = null,
    val datetime: Calendar? = null,
    val noteText: String? = null,
    val imagePath: String? = null,
    val color: NoteColor? = null,
    val webLink: String? = null
)
