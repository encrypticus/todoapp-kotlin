package com.timofeev.todoapp.domain.useCases

import com.timofeev.todoapp.domain.entities.ToDoItem
import com.timofeev.todoapp.domain.repository.ToDoListRepository

class EditToDoItemUseCase(private val repository: ToDoListRepository) {
  suspend fun editToDoItem(toDoItem: ToDoItem) {
    repository.editToDoItem(toDoItem)
  }
}