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
import com.silvanoalbuquerque.mynotesapp.other.Constants.COLOR_PICKER_FIFTH_INDEX
import com.silvanoalbuquerque.mynotesapp.other.Constants.COLOR_PICKER_FIFTH_VALUE
import com.silvanoalbuquerque.mynotesapp.other.Constants.COLOR_PICKER_FIRST_INDEX
import com.silvanoalbuquerque.mynotesapp.other.Constants.COLOR_PICKER_FIRST_VALUE
import com.silvanoalbuquerque.mynotesapp.other.Constants.COLOR_PICKER_FOURTH_INDEX
import com.silvanoalbuquerque.mynotesapp.other.Constants.COLOR_PICKER_FOURTH_VALUE
import com.silvanoalbuquerque.mynotesapp.other.Constants.COLOR_PICKER_SECOND_INDEX
import com.silvanoalbuquerque.mynotesapp.other.Constants.COLOR_PICKER_SECOND_VALUE
import com.silvanoalbuquerque.mynotesapp.other.Constants.COLOR_PICKER_THIRD_INDEX
import com.silvanoalbuquerque.mynotesapp.other.Constants.COLOR_PICKER_THIRD_VALUE
import com.silvanoalbuquerque.mynotesapp.other.Constants.NOTE_TEXTUAL_DATETIME_PATTERN
import com.silvanoalbuquerque.mynotesapp.other.Constants.SELECTED_COLOR_DEFAULT_IMAGE_RESOURCE
import com.silvanoalbuquerque.mynotesapp.ui.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_create_note.*
import kotlinx.android.synthetic.main.layout_miscellaneous.*
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class CreateNoteFragment : Fragment(R.layout.fragment_create_note) {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var availablePickers: List<ImageView>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUi()
        setupViewModelObservables()
    }

    private fun initUi() {
        availablePickers = getAvailablePickers()

        textDateTime.text =
            SimpleDateFormat(NOTE_TEXTUAL_DATETIME_PATTERN, Locale.getDefault()).format(Date())

        buttonSave.setOnClickListener { createNote() }
        imageBack.setOnClickListener { backToNotesListView() }
        handlePickColorClick()

        val bottomSheetBehavior: BottomSheetBehavior<LinearLayout> =
            BottomSheetBehavior.from(layoutMiscellaneous) as AutoCloseBottomSheetBehavior

        layoutMiscellaneous.setOnClickListener { handleBottomSheetClick(bottomSheetBehavior) }
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

    private fun handleBottomSheetClick(bottomSheetBehavior: BottomSheetBehavior<LinearLayout>) {
        if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        } else {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    private fun getAvailablePickers() = listOf<ImageView>(
        firstImageColor,
        secondImageColor,
        thirdImageColor,
        fourthImageColor,
        fifthImageColor
    )

    private fun handlePickColorClick() {
        firstViewColor.setOnClickListener {
            markSelectedColor(COLOR_PICKER_FIRST_INDEX)
            viewModel.setSelectedColor(COLOR_PICKER_FIRST_VALUE)
        }

        secondViewColor.setOnClickListener {
            markSelectedColor(COLOR_PICKER_SECOND_INDEX)
            viewModel.setSelectedColor(COLOR_PICKER_SECOND_VALUE)
        }

        thirdImageColor.setOnClickListener {
            markSelectedColor(COLOR_PICKER_THIRD_INDEX)
            viewModel.setSelectedColor(COLOR_PICKER_THIRD_VALUE)
        }

        fourthViewColor.setOnClickListener {
            markSelectedColor(COLOR_PICKER_FOURTH_INDEX)
            viewModel.setSelectedColor(COLOR_PICKER_FOURTH_VALUE)
        }

        fifthViewColor.setOnClickListener {
            markSelectedColor(COLOR_PICKER_FIFTH_INDEX)
            viewModel.setSelectedColor(COLOR_PICKER_FIFTH_VALUE)
        }
    }

    private fun markSelectedColor(selectedIndex: Int) {
        for (i in availablePickers.indices) {
            if (selectedIndex == i) {
                availablePickers[i].setImageResource(R.drawable.ic_done)
            } else {
                availablePickers[i].setImageResource(SELECTED_COLOR_DEFAULT_IMAGE_RESOURCE)
            }
        }
    }

    private fun backToNotesListView() {
        findNavController().popBackStack()
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
            color = viewModel.selectedColor.value ?: COLOR_PICKER_FIRST_VALUE,
            datetime = dateTime,
            imagePath = "",
            subtitle = subtitle,
            noteText = noteText,
            webLink = ""
        )

        viewModel.insertNote(note)
    }
}