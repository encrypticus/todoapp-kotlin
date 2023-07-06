package com.timofeev.todoapp.domain.repository

import androidx.lifecycle.LiveData
import com.timofeev.todoapp.domain.entities.ToDoItem

interface ToDoListRepository {
  fun getToDoList(): LiveData<List<ToDoItem>>

  fun getToDoListSortedByLowPriority(): LiveData<List<ToDoItem>>

  fun getToDoListSortedByMediumPriority(): LiveData<List<ToDoItem>>

  fun getToDoListSortedByHighPriority(): LiveData<List<ToDoItem>>

  fun searchToDoItem(searchQuery: String): LiveData<List<ToDoItem>>

  suspend fun addToDoItem(toDoItem: ToDoItem)

  suspend fun deleteToDoItem(toDoItem: ToDoItem)

  suspend fun editToDoItem(toDoItem: ToDoItem)

  suspend fun deleteAll()

//  suspend fun getToDoItem(toDoItemId: Int): ToDoItem
}