package com.silvanoalbuquerque.mynotesapp.adapters

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
                if(isEmpty()) {
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
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}