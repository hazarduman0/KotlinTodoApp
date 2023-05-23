package com.example.notes.activity.addedit

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.example.notes.R
import com.example.notes.databinding.ActivityAddEditBinding
import com.example.notes.models.Note
import com.example.notes.repository.DB
import java.time.LocalDateTime

class AddEdit : AppCompatActivity() {
    private lateinit var binding: ActivityAddEditBinding
    private val viewModel: AddEditViewModel by viewModels()
    private lateinit var db: DB
    private lateinit var builder : AlertDialog.Builder
    private var isEdit : Boolean = false
    private var nid : Int = -1
    private var txtTitle: String = ""
    private var txtDescription: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_edit)
        binding.addEditObject = this
        db = DB(this)

        nid = intent.getIntExtra("nid", -1)
        builder = AlertDialog.Builder(this)

        txtTitle = binding.editTextTitle.text.toString()
        txtDescription = binding.editTextDescription.text.toString()

        onScreenInit(nid)

    }

    fun deleteButtonClick(){
        showCustomDialog()
    }

    private fun showCustomDialog(){
        val customView = LayoutInflater.from(this).inflate(R.layout.custom_alert_dialog, null)
        builder.setView(customView)
        val dialog = builder.create()

        val btnDelete = customView.findViewById<Button>(R.id.alertDeleteButton)
        val btnCancel = customView.findViewById<Button>(R.id.alertCancelButton)

        btnDelete.setOnClickListener {
            viewModel.deleteNote(db,nid)
            dialog.dismiss()
            onBackPressedDispatcher.onBackPressed()
        }

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }


        dialog.show()

    }

    fun backClick() {
        onBackPressedDispatcher.onBackPressed()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun createOrLeave(title: String, description: String){
        if (title.isEmpty()) {
            onBackPressedDispatcher.onBackPressed()
            return
        }
        val now = LocalDateTime.now()
        val note: Note =
            Note(nid = null, title = title, description = description, date = now.toString())
        viewModel.saveNote(db, note)
        onBackPressedDispatcher.onBackPressed()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateOrLeave(title: String, description: String){
        if(txtTitle != title || txtDescription != description){
            val now = LocalDateTime.now()
            viewModel.updateNote(db, Note(nid = nid, title = title, description = description, now.toString()))
        }

        if(title.isEmpty() || (title.isEmpty() && description.isEmpty())){
            viewModel.deleteNote(db,nid)
        }

        onBackPressedDispatcher.onBackPressed()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onPause() {
        super.onPause()
        if(isEdit){
            updateOrLeave(binding.editTextTitle.text.toString(), binding.editTextDescription.text.toString())
        }
        else{
            createOrLeave(binding.editTextTitle.text.toString(), binding.editTextDescription.text.toString())
        }
    }

    fun onScreenInit(id: Int) {
        if (id == -1) {
            binding.btnDelete.visibility = View.GONE
            isEdit = false
        } else {
            binding.btnDelete.visibility = View.VISIBLE
            val note: Note = viewModel.getNote(id, db) ?: return
            binding.editTextTitle.setText(note.title)
            binding.editTextDescription.setText(note.description)
            isEdit = true
        }
    }
}