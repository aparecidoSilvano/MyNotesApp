package com.silvanoalbuquerque.mynotesapp.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "notes_table")
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val title: String,
    val subtitle: String,
    val datetime: Calendar,
    val noteText: String,
    val imagePath: String,
    val color: String,
    val webLink: String
)
