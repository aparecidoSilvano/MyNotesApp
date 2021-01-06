package com.silvanoalbuquerque.mynotesapp.adapters

import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.makeramen.roundedimageview.RoundedImageView
import com.silvanoalbuquerque.mynotesapp.db.entities.Note
import com.silvanoalbuquerque.mynotesapp.other.Constants.NOTE_TEXTUAL_DATETIME_PATTERN
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("noteColor")
fun View.setNoteColor(item: Note?) {
    item?.let {
        val gradientDrawable = background as GradientDrawable
        if (item.color.isNotEmpty()) {
            gradientDrawable.setColor(Color.parseColor(item.color))
        }
    }
}

@BindingAdapter("noteSubtitle")
fun TextView.setNoteSubtitle(item: Note?) {
    item?.let {
        if (item.subtitle.isEmpty()) {
            visibility = View.GONE
        } else {
            visibility = View.VISIBLE
            text = item.subtitle
        }
    }
}

@BindingAdapter("noteDateTime")
fun TextView.setNoteDateTime(item: Note?) {
    item?.let {
        text = SimpleDateFormat(
            NOTE_TEXTUAL_DATETIME_PATTERN,
            Locale.getDefault()
        ).format(
            item.datetime.time
        )
    }
}

@BindingAdapter("noteImage")
fun RoundedImageView.setNoteImage(item: Note?) {
    item?.let {
        visibility = if (item.imagePath.isNotEmpty()) {
            setImageBitmap(BitmapFactory.decodeFile(item.imagePath))
            View.VISIBLE
        } else {
            View.GONE
        }
    }
}