package com.example.mvvm.ui.activity


import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvm.Adapter.NotesAdapter
import com.example.mvvm.R
import com.example.mvvm.ViewModel.NotesViewModel

import com.google.android.material.floatingactionbutton.FloatingActionButton

class NotesActivity : AppCompatActivity() {

    private val notesViewModel: NotesViewModel by viewModels()
    private lateinit var notesAdapter: NotesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes)

        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        val fabAddNote: FloatingActionButton = findViewById(R.id.fab_add_note)

        notesAdapter = NotesAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = notesAdapter

        notesViewModel.notes.observe(this, Observer { result ->
            result.onSuccess { notes ->
                notesAdapter.submitList(notes)
            }
            result.onFailure { exception ->
            Toast.makeText(applicationContext, exception.message, Toast.LENGTH_SHORT).show()
            }
        })

        fabAddNote.setOnClickListener {
            // Show dialog to add note
        }

        notesViewModel.getNotes()
    }
}
