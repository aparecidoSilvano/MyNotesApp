package com.silvanoalbuquerque.mynotesapp.ui.fragments.createnote

import com.silvanoalbuquerque.mynotesapp.db.entities.NoteColor
import java.util.*

data class NoteDTO(
    val id: Long? = null,
    var title: String? = null,
    var subtitle: String? = null,
    var datetime: Calendar? = Calendar.getInstance(),
    var noteText: String? = null,
    var imagePath: String? = null,
    var color: NoteColor? = NoteColor.FIRST_COLOR_NOTE,
    var webLink: String? = null
)
