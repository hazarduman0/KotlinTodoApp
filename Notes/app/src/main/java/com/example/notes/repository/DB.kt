package com.example.notes.repository

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.notes.models.Note

class DB(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "notes.db"
        private const val TABLE_NAME = "notes_table"
        private const val COLUMN_NID = "nid"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_DESCRIPTION = "description"
        private const val COLUMN_DATE = "date"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = "CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_NID INTEGER PRIMARY KEY," +
                "$COLUMN_TITLE TEXT," +
                "$COLUMN_DESCRIPTION TEXT," +
                "$COLUMN_DATE TEXT" +
                ")"
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertNote(note: Note) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, note.title)
            put(COLUMN_DESCRIPTION, note.description)
            put(COLUMN_DATE, note.date)
        }
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun getNoteById(nid: Int): Note? {
        val selectQuery = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_NID = ?"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, arrayOf(nid.toString()))
        var note: Note? = null
        if (cursor.moveToFirst()) {
            val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
            val description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION))
            val date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE))
            note = Note(nid, title, description, date)
        }
        cursor.close()
        db.close()
        return note
    }

    fun updateNote(note: Note) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, note.title)
            put(COLUMN_DESCRIPTION, note.description)
            put(COLUMN_DATE, note.date)
        }
        db.update(TABLE_NAME, values, "$COLUMN_NID = ?", arrayOf(note.nid.toString()))
        db.close()
    }

    fun deleteNoteById(nid: Int) {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, "$COLUMN_NID = ?", arrayOf(nid.toString()))
        db.close()
    }

    fun getAllNotes(): MutableList<Note> {
        val notesList = mutableListOf<Note>()
        val selectAllQuery = "SELECT * FROM $TABLE_NAME ORDER BY $COLUMN_DATE DESC"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectAllQuery, null)
        if (cursor.moveToFirst()) {
            do {
                val nid = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_NID))
                val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
                val description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION))
                val date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE))
                val note = Note(nid, title, description, date)
                notesList.add(note)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return notesList
    }

    fun search(searchTerm: String): MutableList<Note> {
        val notesList = mutableListOf<Note>()
        val selectQuery =
            "SELECT * FROM $TABLE_NAME WHERE $COLUMN_TITLE LIKE '%$searchTerm%' OR $COLUMN_DESCRIPTION LIKE '%$searchTerm%' ORDER BY $COLUMN_DATE DESC"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()) {
            do {
                val nid = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_NID))
                val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
                val description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION))
                val date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE))
                val note = Note(nid, title, description, date)
                notesList.add(note)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return notesList
    }
}