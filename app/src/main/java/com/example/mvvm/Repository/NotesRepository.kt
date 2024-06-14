package com.example.mvvm.Repository

import androidx.lifecycle.LiveData
import com.example.mvvm.Model.Note

interface NotesRepository {
    val notes: LiveData<Result<List<Note>>>
    fun getNotes()
    fun addNote(note: Note)
    fun updateNote(note: Note)
    fun deleteNote(noteId: String)
}