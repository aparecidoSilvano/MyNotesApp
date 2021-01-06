package com.silvanoalbuquerque.mynotesapp.adapters

import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.silvanoalbuquerque.mynotesapp.R
import com.silvanoalbuquerque.mynotesapp.db.entities.Note
import com.silvanoalbuquerque.mynotesapp.other.Constants
import kotlinx.android.synthetic.main.item_note.view.*
import java.text.SimpleDateFormat
import java.util.*

class NotesAdapter(private val clickListener: NoteListener) :
    ListAdapter<Note, NotesAdapter.ViewHolder>(NoteDiffCallback()) {

    class ViewHolder private constructor(viewItem: View) : RecyclerView.ViewHolder(viewItem) {
        fun bind(listener: NoteListener, item: Note) {
            itemView.apply {
                setOnClickListener {
                    listener.onClick(item)
                }

                textTitle.text = item.title

                item.subtitle.apply {
                    if (isEmpty()) {
                        textSubtitle.visibility = View.GONE
                    } else {
                        textSubtitle.visibility = View.VISIBLE
                        textSubtitle.text = item.subtitle
                    }
                }

                textDateTime.text = SimpleDateFormat(
                    Constants.NOTE_TEXTUAL_DATETIME_PATTERN,
                    Locale.getDefault()
                ).format(
                    item.datetime.time
                )

                val gradientDrawable = layoutNote.background as GradientDrawable
                if (item.color.isNotEmpty()) {
                    gradientDrawable.setColor(Color.parseColor(item.color))
                }

                if (item.imagePath.isNotEmpty()) {
                    imageNote.setImageBitmap(BitmapFactory.decodeFile(item.imagePath))
                    imageNote.visibility = View.VISIBLE
                } else {
                    imageNote.visibility = View.GONE
                }
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.item_note, parent, false)
                return ViewHolder(view)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note = getItem(position)
        holder.bind(clickListener, note)
    }
}

class NoteDiffCallback : DiffUtil.ItemCallback<Note>() {
    override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem == newItem
    }
}

class NoteListener(val clickListener: (noteId: Long) -> Unit) {
    fun onClick(note: Note) = clickListener(note.id!!)
}