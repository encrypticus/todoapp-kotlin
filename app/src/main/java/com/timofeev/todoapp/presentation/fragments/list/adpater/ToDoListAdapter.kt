package com.timofeev.todoapp.presentation.fragments.list.adpater

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.timofeev.todoapp.databinding.RowLayoutBinding
import com.timofeev.todoapp.domain.entities.ToDoItem

class ToDoListAdapter : ListAdapter<ToDoItem, ToDoItemViewHolder>(ToDoItemDiffCallback()) {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoItemViewHolder {
    val binding = RowLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return ToDoItemViewHolder(binding)
  }

  override fun onBindViewHolder(holder: ToDoItemViewHolder, position: Int) {
    holder.binding.toDoItem = getItem(position)
  }

}