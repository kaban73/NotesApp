package com.example.notesapp.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.notesapp.databinding.ListItemBinding

class NotesListAdapter(
    private val editNoteUi: EditNoteUi
) : RecyclerView.Adapter<NoteViewHolder>() {
    private val list : MutableList<NoteUi> = mutableListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder =
        NoteViewHolder(ListItemBinding.inflate(LayoutInflater.from(parent.context)),
            editNoteUi)

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(list[position])
    }
    fun update(newList : List<NoteUi>) {
        val diffCallback = MyDiffCallback(list, newList)
        val diffUtilResult = DiffUtil.calculateDiff(diffCallback)
        list.clear().also { list.addAll(newList) }
        diffUtilResult.dispatchUpdatesTo(this)
    }

}
class MyDiffCallback(
    private val oldList : List<NoteUi>,
    private val newList : List<NoteUi>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int =
        oldList.size

    override fun getNewListSize(): Int =
        newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].areItemsTheSame(newList[newItemPosition])

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].areContentTheSame(newList[newItemPosition])

}

class NoteViewHolder(
    private val b : ListItemBinding,
    private val editNoteUi: EditNoteUi
) : RecyclerView.ViewHolder(b.root) {
    fun bind(noteUi: NoteUi) {
        noteUi.show(b.notesTitleTextView,b.notesDateTextView)
        itemView.setOnClickListener {
            editNoteUi.editNoteUi(noteUi)
        }
    }
}