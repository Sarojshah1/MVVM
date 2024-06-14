package com.example.mvvm.ViewModel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvm.Model.Note
import com.example.mvvm.Repository.NotesRepository
import com.example.mvvm.Repository.NotesRepositoryImpl

import kotlinx.coroutines.launch

class NotesViewModel : ViewModel() {

    private val notesRepository: NotesRepository = NotesRepositoryImpl()

    val notes = notesRepository.notes

    fun getNotes() {
        viewModelScope.launch {
            notesRepository.getNotes()
        }
    }

    fun addNote(note: Note) {
        viewModelScope.launch {
            notesRepository.addNote(note)
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch {
            notesRepository.updateNote(note)
        }
    }

    fun deleteNote(noteId: String) {
        viewModelScope.launch {
            notesRepository.deleteNote(noteId)
        }
    }
}
