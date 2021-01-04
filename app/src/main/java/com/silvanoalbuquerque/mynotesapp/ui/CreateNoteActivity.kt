package com.silvanoalbuquerque.mynotesapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.silvanoalbuquerque.mynotesapp.R

class CreateNoteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_create_note)
    }
}