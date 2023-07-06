package com.timofeev.todoapp.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.timofeev.todoapp.domain.entities.Priority

@Entity(tableName = "todo_table")
data class ToDoItemDbModel(
  @PrimaryKey(autoGenerate = true)
  var id: Int,
  var title: String,
  var priority: Priority,
  var description: String
)