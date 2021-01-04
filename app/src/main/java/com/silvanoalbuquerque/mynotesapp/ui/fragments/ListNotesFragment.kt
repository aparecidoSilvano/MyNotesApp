package com.silvanoalbuquerque.mynotesapp.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.silvanoalbuquerque.mynotesapp.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_list_notes.*

@AndroidEntryPoint
class ListNotesFragment : Fragment(R.layout.fragment_list_notes) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonAddNoteMain.setOnClickListener {
            val action = ListNotesFragmentDirections.actionListNotesFragmentToCreateNoteFragment()
            findNavController().navigate(action)
        }
    }
}
