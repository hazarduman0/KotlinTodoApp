package com.example.notes.activity.notes

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.notes.models.Note
import com.example.notes.repository.DB
import com.example.notes.utils.enums.ThemeStatus
import com.example.notes.utils.extensions.getDrawableInt
import com.example.notes.utils.extensions.getThemeModeInt
import com.example.notes.utils.extensions.getThemeString
import com.example.notes.utils.extensions.themeEnum

class MainActivityViewModel : ViewModel() {

    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor

    var themeImg = MutableLiveData<Int>()
    var searchBarVisibility = MutableLiveData(false)
    var searchText = MutableLiveData("")
    var noteList = MutableLiveData<MutableList<Note>>()
    var haveData = MutableLiveData(false)

    fun initSharedPreferences(context: Context) {
        sharedPreferences = context.getSharedPreferences("notes", MODE_PRIVATE)
        editor = sharedPreferences.edit()
    }

    fun closeSearchBar(){
        searchBarVisibility.value = false
    }

    fun switchTheme() {
        val themeStatus = checkThemeStatus()

        //light -> dark -> system -> light
        if (themeStatus == ThemeStatus.DARK) {
            setThemeStatus(ThemeStatus.SYSTEM)
            setImg(ThemeStatus.SYSTEM)
        }
        if (themeStatus == ThemeStatus.LIGHT) {
            setThemeStatus(ThemeStatus.DARK)
            setImg(ThemeStatus.DARK)
        }
        if (themeStatus == ThemeStatus.SYSTEM) {
            setThemeStatus(ThemeStatus.LIGHT)
            setImg(ThemeStatus.LIGHT)
        }
    }

    fun searchNote(str : String, db : DB){
        noteList.value = db.search(str)
    }

    fun deleteNote(db: DB, id:Int){
        db.deleteNoteById(id)
    }

    fun checkThemeStatus(): ThemeStatus {
        val status = sharedPreferences.getString("themeStatus", "")
        if (status!!.isEmpty()) {
            setThemeStatus(ThemeStatus.SYSTEM)
            return status.themeEnum()
        }
        setImg(status.themeEnum())
        setThemeStatus(status.themeEnum())
        return status.themeEnum()
    }

    fun setThemeStatus(status: ThemeStatus) {
        editor.putString("themeStatus", status.getThemeString())
        editor.commit()
        AppCompatDelegate.setDefaultNightMode(status.getThemeModeInt())
    }


    fun setImg(themeStatus: ThemeStatus) {
        themeImg.value = themeStatus.getDrawableInt()
    }

    fun searchButtonOnClick() {
        searchBarVisibility.value = !searchBarVisibility.value!!
    }

    fun clearButtonOnClick(context: Context, view: View){
        searchBarVisibility.value = !searchBarVisibility.value!!

        searchText.value = ""

        try {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun removeNoteList(){
        noteList.value!!.clear()
    }

    fun getAllNotes(db: DB) {
        val notes = db.getAllNotes()
        noteList.value = notes
        haveData.value = notes.isNotEmpty()
    }
}