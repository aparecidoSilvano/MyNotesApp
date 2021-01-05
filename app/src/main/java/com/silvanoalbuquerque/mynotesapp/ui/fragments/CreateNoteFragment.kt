package com.silvanoalbuquerque.mynotesapp.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.silvanoalbuquerque.mynotesapp.R
import com.silvanoalbuquerque.mynotesapp.db.entities.Note
import com.silvanoalbuquerque.mynotesapp.other.Constants.NOTE_TEXTUAL_DATETIME_PATTERN
import com.silvanoalbuquerque.mynotesapp.ui.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_create_note.*
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class CreateNoteFragment : Fragment(R.layout.fragment_create_note) {

    private val viewModel: MainViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TODO refactor that messy code

        textDateTime.text =
            SimpleDateFormat(NOTE_TEXTUAL_DATETIME_PATTERN, Locale.getDefault()).format(Date())

        imageBack.setOnClickListener {
            val backAction =
                CreateNoteFragmentDirections.actionCreateNoteFragmentToListNotesFragment()
            findNavController().navigate(backAction)
        }

        viewModel.finishedSavingNote.observe(viewLifecycleOwner) {
            if (it) {
                val backAction =
                    CreateNoteFragmentDirections.actionCreateNoteFragmentToListNotesFragment()
                findNavController().navigate(backAction)
            }
        }

        buttonSave.setOnClickListener {
            val title = inputNoteTitle.text.toString()
            if (title.isEmpty()) {
                Snackbar.make(it, R.string.alert_empty_note_title, Snackbar.LENGTH_LONG).show()

                return@setOnClickListener
            }

            val subtitle = inputNoteSubtitle.text.toString()
            val noteText = inputNote.text.toString()
            val dateTime = Calendar.getInstance()

            val note = Note(
                title = title,
                color = "#444444",
                datetime = dateTime,
                imagePath = "",
                subtitle = subtitle,
                noteText = noteText,
                webLink = ""
            )

            viewModel.insertNote(note)
        }
    }
}