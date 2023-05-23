package com.example.notes.activity.addedit

import androidx.lifecycle.ViewModel
import com.example.notes.models.Note
import com.example.notes.repository.DB

class AddEditViewModel : ViewModel() {

    fun getNote(id:Int, db: DB): Note? {
        return db.getNoteById(id)
    }

    fun saveNote(db:DB, note: Note){
        db.insertNote(note)
    }

    fun updateNote(db: DB, note: Note){
        db.updateNote(note)
    }

    fun deleteNote(db: DB, id:Int){
        db.deleteNoteById(id)
    }
}