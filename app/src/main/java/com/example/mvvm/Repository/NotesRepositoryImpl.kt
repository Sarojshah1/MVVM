package com.example.mvvm.Repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mvvm.Model.Note

import com.google.firebase.database.*

class NotesRepositoryImpl : NotesRepository {

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val notesReference: DatabaseReference = database.getReference("notes")

    private val _notes = MutableLiveData<Result<List<Note>>>()
    override val notes: LiveData<Result<List<Note>>> = _notes

    override fun getNotes() {
        notesReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val notesList = mutableListOf<Note>()
                for (noteSnapshot in snapshot.children) {
                    val note = noteSnapshot.getValue(Note::class.java)
                    note?.let { notesList.add(it) }
                }
                _notes.value = Result.success(notesList)
            }

            override fun onCancelled(error: DatabaseError) {
                _notes.value = Result.failure(error.toException())
            }
        })
    }

    override fun addNote(note: Note) {
        val noteId = notesReference.push().key ?: ""
        notesReference.child(noteId).setValue(note)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    getNotes()
                } else {
                    _notes.value = Result.failure(task.exception ?: Exception("Unknown error occurred"))
                }
            }
    }

    override fun updateNote(note: Note) {
        notesReference.child(note.id).setValue(note)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    getNotes()
                } else {
                    _notes.value = Result.failure(task.exception ?: Exception("Unknown error occurred"))
                }
            }
    }

    override fun deleteNote(noteId: String) {
        notesReference.child(noteId).removeValue()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    getNotes()
                } else {
                    _notes.value = Result.failure(task.exception ?: Exception("Unknown error occurred"))
                }
            }
    }
}
