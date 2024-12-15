package com.example.notes

import android.icu.text.CaseMap.Title
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.room.util.copy
import com.example.notes.data.Note
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject

class NotesViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val notesCollection = db.collection("notes")

    var notes = mutableStateOf<List<Note>>(emptyList())

    val noteList: List<Note> by notes

    init {
        fetchNotes()
    }

    fun fetchNotes() {
        db.collection("notes")
            .get()
            .addOnSuccessListener { snapshot ->
                val fetchedNotes = snapshot.documents.map { doc ->
                    val title = doc.get("title") as? String // Cast to String
                    val content = doc.get("content") as? String // Cast to String
                    Note(id = doc.id, title = title ?: "", content = content ?: "")
                }
                notes.value = fetchedNotes

            }
    }
//delete id??
    fun addNote(title: String, content: String) {
        val note = Note(id = "", title = "", content = "")
        db.collection("notes")
            .add(note)
            .addOnSuccessListener { documentReference ->
                note.id = documentReference.id
//                Log.i("Doc ID", documentReference.id)
//                Log.i("Note ID", note.id)
                notesCollection.document(documentReference.id).set(Note(id = documentReference.id, title = title, content = content))
                fetchNotes()
            }

    }

    fun updateNote(noteId: String, newTitle: String, newContent: String) {
        val updatedNote = Note(id = noteId, title = newTitle, content = newContent)
        notesCollection.document(noteId).set(updatedNote)
            .addOnSuccessListener {
                fetchNotes()
            }
    }

    fun deleteNote(noteId: String) {
        notesCollection.document(noteId).delete()
            .addOnSuccessListener {
                fetchNotes()
            }
    }
}