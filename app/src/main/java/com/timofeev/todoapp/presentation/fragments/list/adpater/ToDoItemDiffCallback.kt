package com.timofeev.todoapp.presentation.fragments.list.adpater

import androidx.recyclerview.widget.DiffUtil
import com.timofeev.todoapp.domain.entities.ToDoItem

class ToDoItemDiffCallback : DiffUtil.ItemCallback<ToDoItem>() {
  override fun areItemsTheSame(oldItem: ToDoItem, newItem: ToDoItem): Boolean {
    return oldItem.id == newItem.id
  }

  override fun areContentsTheSame(oldItem: ToDoItem, newItem: ToDoItem): Boolean {
    return oldItem == newItem
  }
}