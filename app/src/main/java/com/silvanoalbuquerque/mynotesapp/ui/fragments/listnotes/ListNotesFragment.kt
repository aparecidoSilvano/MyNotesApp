package com.silvanoalbuquerque.mynotesapp.ui.fragments.listnotes

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.silvanoalbuquerque.mynotesapp.R
import com.silvanoalbuquerque.mynotesapp.adapters.NoteListener
import com.silvanoalbuquerque.mynotesapp.adapters.NotesAdapter
import com.silvanoalbuquerque.mynotesapp.other.Constants.DEFAULT_STAGGERED_GRID_SPAN_COUNT
import com.silvanoalbuquerque.mynotesapp.other.Constants.DELAY_TO_SCROLL_NOTES_LIST_TO_TOP
import com.silvanoalbuquerque.mynotesapp.other.Constants.NOTES_LIST_FIRST_ITEM_POSITION
import com.silvanoalbuquerque.mynotesapp.other.Constants.UNKNOWN_NOTE_KEY
import com.silvanoalbuquerque.mynotesapp.ui.viewmodels.ListNotesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_list_notes.*

@AndroidEntryPoint
class ListNotesFragment : Fragment(R.layout.fragment_list_notes) {

    private val viewModel: ListNotesViewModel by viewModels()
    private lateinit var notesAdapter: NotesAdapter
    private lateinit var rvLayoutManager: StaggeredGridLayoutManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUi()
        setupViewModelObservables()
    }

    private fun initUi() {
        buttonAddNoteMain.setOnClickListener {
            navigateToNoteFragment(UNKNOWN_NOTE_KEY)
        }

        rvLayoutManager = StaggeredGridLayoutManager(
            DEFAULT_STAGGERED_GRID_SPAN_COUNT,
            StaggeredGridLayoutManager.VERTICAL
        )

        setupRecyclerView()
    }

    private fun setupViewModelObservables() {
        viewModel.notes.observe(viewLifecycleOwner) { notes ->
            notesAdapter.submitList(notes)

            Handler(Looper.getMainLooper()).postDelayed(
                {
                    rvLayoutManager.smoothScrollToPosition(
                        notesRecyclerView,
                        null,
                        NOTES_LIST_FIRST_ITEM_POSITION
                    )
                }, DELAY_TO_SCROLL_NOTES_LIST_TO_TOP
            )
        }

        viewModel.navigateToNote.observe(viewLifecycleOwner) { note ->
            note?.let {
                navigateToNoteFragment(note.id!!)
            }
        }
    }

    private fun navigateToNoteFragment(id: Long) {
        val action =
            ListNotesFragmentDirections.actionListNotesFragmentToCreateNoteFragment(id)
        findNavController().navigate(action)

        viewModel.doneNavigating()
    }

    private fun setupRecyclerView() {
        notesAdapter = NotesAdapter(NoteListener { note ->
            viewModel.onNoteClicked(note)
        })

        notesRecyclerView.apply {
            adapter = notesAdapter
            layoutManager = rvLayoutManager
        }
    }
}
