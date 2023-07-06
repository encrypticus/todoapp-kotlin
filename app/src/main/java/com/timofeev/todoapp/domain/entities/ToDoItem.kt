package com.timofeev.todoapp.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ToDoItem(
  val id: Int,
  val title: String,
  val priority: Priority,
  val description: String,
): Parcelable