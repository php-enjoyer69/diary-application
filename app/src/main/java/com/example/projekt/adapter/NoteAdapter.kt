package com.example.projekt.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projekt.data.entity.Note
import com.example.projekt.databinding.ItemNotesBinding
import java.text.SimpleDateFormat

//adapter class for managing and binding data to a RecyclerView that displays a list of notes
class NoteAdapter(private val mNotes: List<Note>, private val listener: OnNoteClickListener): RecyclerView.Adapter<NoteAdapter.ViewHolder>(){

    //an interface to handle click events on notes
    interface OnNoteClickListener{
        fun onNoteClick(note: Note)
        fun onNoteLongClick(note: Note)
    }
    //ViewHolder class for managing individual note views within the RecyclerView
    inner class ViewHolder(private val binding: ItemNotesBinding): RecyclerView.ViewHolder(binding.root){

        init {
            //set click listener for the root view
            binding.apply {
                root.setOnClickListener {
                    val position = adapterPosition
                    if(position != RecyclerView.NO_POSITION){
                        val note = mNotes[position]
                        listener.onNoteClick(note)
                    }
                }
                //set long click listener for the root view
                root.setOnLongClickListener {
                    val position = adapterPosition
                    if(position != RecyclerView.NO_POSITION){
                        val note = mNotes[position]
                        listener.onNoteLongClick(note)
                    }
                    true
                }
            }
        }
        //binds the data from a note to the corresponding views in the layout
        fun bind(note: Note){
            binding.apply {
                titleNote.text = note.title
                contentNote.text = note.content
                val formatter = SimpleDateFormat("dd/MM/yyyy")
                dateNote.text = formatter.format(note.date)
            }
        }
    }

    //inflates the item layout and creates the ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemNotesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    //returns number of items held by adapter
    override fun getItemCount(): Int {
        return mNotes.size
    }

    //binds the data at the specified position to the ViewHolder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(mNotes[position]){
            holder.bind(this)
        }
    }
}