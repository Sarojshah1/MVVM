package com.example.mvvm.Repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mvvm.Model.Note

import com.google.firebase.firestore.FirebaseFirestore

class NotesRepositoryImpl : NotesRepository {

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val _notes = MutableLiveData<Result<List<Note>>>()
    override val notes: LiveData<Result<List<Note>>> = _notes

    override fun getNotes() {
        firestore.collection("notes").get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val notesList = task.result?.toObjects(Note::class.java)
                _notes.value = Result.success(notesList ?: emptyList())
            } else {
                _notes.value = Result.failure(task.exception ?: Exception("Unknown error occurred"))
            }
        }
    }

    override fun addNote(note: Note) {
        firestore.collection("notes").add(note).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                getNotes()
            } else {
                _notes.value = Result.failure(task.exception ?: Exception("Unknown error occurred"))
            }
        }
    }

    override fun updateNote(note: Note) {
        firestore.collection("notes").document(note.id).set(note).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                getNotes()
            } else {
                _notes.value = Result.failure(task.exception ?: Exception("Unknown error occurred"))
            }
        }
    }

    override fun deleteNote(noteId: String) {
        firestore.collection("notes").document(noteId).delete().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                getNotes()
            } else {
                _notes.value = Result.failure(task.exception ?: Exception("Unknown error occurred"))
            }
        }
    }
}
