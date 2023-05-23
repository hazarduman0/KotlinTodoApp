package com.example.notes.activity.notes

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notes.R
import com.example.notes.activity.addedit.AddEdit
import com.example.notes.adapters.MenuItemAdapter
import com.example.notes.databinding.ActivityMainBinding
import com.example.notes.models.Note
import com.example.notes.repository.DB
import com.example.notes.utils.CustomDividerItemDecoration


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainActivityViewModel by viewModels()
    private lateinit var db: DB

    private var flagDecoration = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.mainObject = this
        db = DB(this)

        viewModel.initSharedPreferences(this)
        viewModel.getAllNotes(db)
        viewModel.checkThemeStatus()

        viewModel.haveData.observe(this) { haveData ->
            if (haveData) {
                binding.noNoteInfo.visibility = View.GONE
                binding.noNoteImg.visibility = View.GONE
                binding.noNoteText.visibility = View.GONE
            } else {
                binding.noNoteInfo.visibility = View.VISIBLE
                binding.noNoteImg.visibility = View.VISIBLE
                binding.noNoteText.visibility = View.VISIBLE
            }
        }

        viewModel.noteList.observe(this) { noteList ->
            if (noteList.isEmpty()) {
                binding.mainScroll.visibility = View.GONE
            } else {
                binding.mainScroll.visibility = View.VISIBLE

                val adapter = MenuItemAdapter(noteList)
                binding.noteListView.layoutManager = LinearLayoutManager(this)
                binding.noteListView.adapter = adapter
                adapter.setOnItemClickListener(object : MenuItemAdapter.onItemClickListener {
                    override fun onItemClick(position: Int) {
                        onNoteItemSelect(noteList[position])
                    }
                })

                ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
                    0,
                    ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
                ) {
                    override fun onMove(
                        recyclerView: RecyclerView,
                        viewHolder: RecyclerView.ViewHolder,
                        target: RecyclerView.ViewHolder
                    ): Boolean {
                        return false
                    }

                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                        viewModel.deleteNote(db,adapter.getNoteAt(viewHolder.bindingAdapterPosition).nid!!)
                        viewModel.getAllNotes(db)
                        Toast.makeText(this@MainActivity, "Note deleted", Toast.LENGTH_SHORT).show()
                    }
                }).attachToRecyclerView(binding.noteListView)

                if (!flagDecoration) {
                    binding.noteListView.addItemDecoration(
                        CustomDividerItemDecoration(
                        this,
                        LinearLayoutManager.VERTICAL
                    )
                    )
                    flagDecoration = true
                }
            }
        }

        viewModel.themeImg.observe(this) { themeImg ->
            binding.themeButton.setImageResource(themeImg)
        }

        viewModel.searchBarVisibility.observe(this) { searchBarVisibility ->
            binding.apply {
                searchFrameLayout.isVisible = searchBarVisibility
                noNoteInfo.isVisible = !searchBarVisibility
                appHeader.isVisible = !searchBarVisibility
                searchButton.isVisible = !searchBarVisibility
                themeButton.isVisible = !searchBarVisibility
            }

        }

        viewModel.searchText.observe(this) { searchText ->
            binding.searchBar.setText(searchText)
        }


    }

    override fun onResume() {
        super.onResume()
        viewModel.closeSearchBar()
        viewModel.getAllNotes(db)
    }

    fun afterTextChanged(str: Editable?) {
        viewModel.searchNote(str.toString(), db)
    }

    fun onNoteItemSelect(note: Note) {
        val intent = Intent(this, AddEdit::class.java)
        intent.putExtra("nid", note.nid)
        startActivity(intent)
    }

    fun onFloatingActionButtonClick() {
        val intent = Intent(this, AddEdit::class.java)
        intent.putExtra("nid", -1)
        startActivity(intent)
    }

    fun searchButtonClick() {
        viewModel.searchButtonOnClick()
        viewModel.removeNoteList()
    }

    fun themeButtonClick() {
        viewModel.switchTheme()
    }

    fun clearButtonClick(view: View) {
        viewModel.clearButtonOnClick(this, view)
        viewModel.getAllNotes(db)
    }
}