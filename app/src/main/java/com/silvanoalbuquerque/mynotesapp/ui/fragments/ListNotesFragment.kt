package com.silvanoalbuquerque.mynotesapp.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.silvanoalbuquerque.mynotesapp.R
import com.silvanoalbuquerque.mynotesapp.adapters.NotesAdapter
import com.silvanoalbuquerque.mynotesapp.ui.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_list_notes.*

@AndroidEntryPoint
class ListNotesFragment : Fragment(R.layout.fragment_list_notes) {

    private val viewModel: MainViewModel by viewModels()
    lateinit var notesAdapter: NotesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonAddNoteMain.setOnClickListener {
            val action = ListNotesFragmentDirections.actionListNotesFragmentToCreateNoteFragment()
            findNavController().navigate(action)
        }

        viewModel.notes.observe(viewLifecycleOwner) { notes ->
            notesAdapter.submitList(notes)
        }

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        notesAdapter = NotesAdapter()
        notesRecyclerView.apply {
            adapter = notesAdapter
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        }
    }
}
