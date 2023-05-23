package com.example.notes.adapters

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.notes.R
import com.example.notes.models.Note
import com.example.notes.utils.extensions.formatDate

class MenuItemAdapter(private val notes: MutableList<Note>) : RecyclerView.Adapter<MenuItemAdapter.ViewHolder>(){

    private lateinit var mListener : onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }

    fun getNoteAt(position: Int): Note {
        return notes[position]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)
        return ViewHolder(view, mListener)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note = notes[position]
        holder.noteItemHeader.text = note.title
        holder.noteItemDescription.text = setDescription(note.description)
        holder.noteItemDate.text = note.date.formatDate()
    }

    private fun setDescription(desc: String) : String {
        return desc.ifEmpty { "No description" }
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    inner class ViewHolder(itemView: View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView) {
        val noteItemHeader : TextView = itemView.findViewById(R.id.noteItemHeader)
        val noteItemDate : TextView = itemView.findViewById(R.id.noteItemDate)
        val noteItemDescription : TextView = itemView.findViewById(R.id.noteItemDescription)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(absoluteAdapterPosition)
            }
        }
    }
}
