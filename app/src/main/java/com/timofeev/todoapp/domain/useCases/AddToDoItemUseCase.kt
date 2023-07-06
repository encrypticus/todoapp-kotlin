package com.timofeev.todoapp.domain.useCases

import com.timofeev.todoapp.domain.entities.ToDoItem
import com.timofeev.todoapp.domain.repository.ToDoListRepository

class AddToDoItemUseCase(private val repository: ToDoListRepository) {
  suspend fun addToDoItem(toDoItem: ToDoItem) {
    repository.addToDoItem(toDoItem)
  }
}