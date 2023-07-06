package com.timofeev.todoapp.domain.useCases

import com.timofeev.todoapp.domain.entities.ToDoItem
import com.timofeev.todoapp.domain.repository.ToDoListRepository

class DeleteToDoItemUseCase(private val repository: ToDoListRepository) {
  suspend fun deleteToDoItem(toDoItem: ToDoItem) {
    repository.deleteToDoItem(toDoItem)
  }
}