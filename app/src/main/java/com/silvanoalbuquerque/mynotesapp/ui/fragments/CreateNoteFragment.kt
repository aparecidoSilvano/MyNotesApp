package com.silvanoalbuquerque.mynotesapp.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.silvanoalbuquerque.mynotesapp.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_create_note.*

@AndroidEntryPoint
class CreateNoteFragment : Fragment(R.layout.fragment_create_note) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageBack.setOnClickListener {
            val backAction = CreateNoteFragmentDirections.actionCreateNoteFragmentToListNotesFragment()
            findNavController().navigate(backAction)
        }
    }

}