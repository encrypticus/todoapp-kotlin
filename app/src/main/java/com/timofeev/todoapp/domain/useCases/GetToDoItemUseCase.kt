package com.timofeev.todoapp.domain.useCases

import com.timofeev.todoapp.domain.entities.ToDoItem
import com.timofeev.todoapp.domain.repository.ToDoListRepository

class GetToDoItemUseCase(private val repository: ToDoListRepository) {
//  suspend fun getToDoItemUseCase(toDoItemId: Int): ToDoItem {
//    return repository.getToDoItem(toDoItemId)
//  }
}