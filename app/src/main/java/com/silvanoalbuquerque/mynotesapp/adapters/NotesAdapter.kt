package com.silvanoalbuquerque.mynotesapp.adapters

import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.silvanoalbuquerque.mynotesapp.R
import com.silvanoalbuquerque.mynotesapp.db.entities.Note
import com.silvanoalbuquerque.mynotesapp.other.Constants
import kotlinx.android.synthetic.main.item_note.view.*
import java.text.SimpleDateFormat
import java.util.*

class NotesAdapter : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    class NoteViewHolder(viewItem: View) : RecyclerView.ViewHolder(viewItem)

    private val differCallback = object : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.title == newItem.title && oldItem.id == newItem.id
        }
    }

    private val differ = AsyncListDiffer(this, differCallback)

    fun submitList(notes: List<Note>) {
        differ.submitList(notes)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_note,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = differ.currentList[position]

        holder.itemView.apply {
            textTitle.text = note.title

            note.subtitle.apply {
                if (isEmpty()) {
                    textSubtitle.visibility = View.GONE
                } else {
                    textSubtitle.visibility = View.VISIBLE
                    textSubtitle.text = note.subtitle
                }
            }

            textDateTime.text = SimpleDateFormat(
                Constants.NOTE_TEXTUAL_DATETIME_PATTERN,
                Locale.getDefault()
            ).format(
                note.datetime.time
            )

            val gradientDrawable = layoutNote.background as GradientDrawable
            if (note.color.isNotEmpty()) {
                gradientDrawable.setColor(Color.parseColor(note.color))
            }

            if (note.imagePath.isNotEmpty()) {
                imageNote.setImageBitmap(BitmapFactory.decodeFile(note.imagePath))
                imageNote.visibility = View.VISIBLE
            } else {
                imageNote.visibility = View.GONE
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}