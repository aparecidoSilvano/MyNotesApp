package com.silvanoalbuquerque.mynotesapp.ui.fragments

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.silvanoalbuquerque.mynotesapp.R
import com.silvanoalbuquerque.mynotesapp.db.entities.Note
import com.silvanoalbuquerque.mynotesapp.other.Constants.NOTE_TEXTUAL_DATETIME_PATTERN
import com.silvanoalbuquerque.mynotesapp.ui.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_create_note.*
import kotlinx.android.synthetic.main.layout_miscellaneous.*
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class CreateNoteFragment : Fragment(R.layout.fragment_create_note) {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var colorsPickersAvailable: List<ImageView>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUi()
        setupViewModelObservables()
    }

    private fun setupViewModelObservables() {
        viewModel.finishedSavingNote.observe(viewLifecycleOwner) {
            if (it) {
                backToNotesListView()
            }
        }

        viewModel.selectedColor.observe(viewLifecycleOwner) {
            val gradientDrawable = viewSubtitleIndicator.background as GradientDrawable
            gradientDrawable.setColor(Color.parseColor(it))
        }
    }

    private fun initUi() {
        textDateTime.text =
            SimpleDateFormat(NOTE_TEXTUAL_DATETIME_PATTERN, Locale.getDefault()).format(Date())

        buttonSave.setOnClickListener { createNote() }
        imageBack.setOnClickListener { backToNotesListView() }

        val bottomSheetBehavior: BottomSheetBehavior<LinearLayout> =
            BottomSheetBehavior.from(layoutMiscellaneous)

        layoutMiscellaneous.setOnClickListener {
            if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            } else {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }

        colorsPickersAvailable = listOf<ImageView>(
            firstImageColor,
            secondImageColor,
            thirdImageColor,
            fourthImageColor,
            fifthImageColor
        )

        handlePickColorClick()
    }

    private fun markSelectedColor(selectedIndex: Int) {
        for (i in colorsPickersAvailable.indices) {
            if (selectedIndex == i) {
                colorsPickersAvailable[i].setImageResource(R.drawable.ic_done)
            } else {
                colorsPickersAvailable[i].setImageResource(0)
            }
        }
    }

    private fun handlePickColorClick() {

        firstViewColor.setOnClickListener {
            markSelectedColor(0)
            viewModel.selectedColor.value = "#333333"
        }

        secondViewColor.setOnClickListener {
            markSelectedColor(1)
            viewModel.selectedColor.value = "#FDBE3B"
        }

        thirdImageColor.setOnClickListener {
            markSelectedColor(2)
            viewModel.selectedColor.value = "#FF4842"
        }

        fourthViewColor.setOnClickListener {
            markSelectedColor(3)
            viewModel.selectedColor.value = "#3A52FC"
        }

        fifthViewColor.setOnClickListener {
            markSelectedColor(4)
            viewModel.selectedColor.value = "#000000"
        }
    }

    private fun backToNotesListView() {
        val backAction =
            CreateNoteFragmentDirections.actionCreateNoteFragmentToListNotesFragment()
        findNavController().navigate(backAction)
    }

    private fun createNote() {
        val title = inputNoteTitle.text.toString()
        if (title.isEmpty()) {
            Snackbar.make(requireView(), R.string.alert_empty_note_title, Snackbar.LENGTH_LONG)
                .show()
            return
        }

        val subtitle = inputNoteSubtitle.text.toString()
        val noteText = inputNote.text.toString()
        val dateTime = Calendar.getInstance()

        val note = Note(
            title = title,
            color = viewModel.selectedColor.value ?: "333333",
            datetime = dateTime,
            imagePath = "",
            subtitle = subtitle,
            noteText = noteText,
            webLink = ""
        )

        viewModel.insertNote(note)
    }
}