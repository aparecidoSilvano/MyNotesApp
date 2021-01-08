package com.silvanoalbuquerque.mynotesapp.adapters

import android.graphics.BitmapFactory
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.silvanoalbuquerque.mynotesapp.db.entities.Note
import com.silvanoalbuquerque.mynotesapp.db.entities.NoteColor
import com.silvanoalbuquerque.mynotesapp.other.Constants.NOTE_TEXTUAL_DATETIME_PATTERN
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("noteColor")
fun View.setNoteColor(color: NoteColor?) {
    color?.let {
        val gradientDrawable = background as GradientDrawable
        gradientDrawable.setColor(
            ContextCompat.getColor(
                context,
                color.colorResource
            )
        )
    }
}

@BindingAdapter("noteSubtitle")
fun TextView.setNoteSubtitle(item: Note?) {
    item?.let {
        if (item.subtitle == null || item.subtitle.isEmpty()) {
            visibility = View.GONE
        } else {
            visibility = View.VISIBLE
            text = item.subtitle
        }
    }
}

@BindingAdapter("noteDateTime")
fun TextView.setNoteDateTime(dateTime: Calendar?) {
    dateTime?.let {
        text = SimpleDateFormat(
            NOTE_TEXTUAL_DATETIME_PATTERN,
            Locale.getDefault()
        ).format(
            dateTime.time
        )
    }
}

@BindingAdapter("noteImage")
fun ImageView.setNoteImage(imagePath: String?) {
    visibility = if (imagePath == null || imagePath.isEmpty()) {
        View.GONE
    } else {
        setImageBitmap(BitmapFactory.decodeFile(imagePath))
        View.VISIBLE
    }
}