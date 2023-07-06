package com.timofeev.todoapp.domain.useCases

import androidx.lifecycle.LiveData
import com.timofeev.todoapp.domain.entities.ToDoItem
import com.timofeev.todoapp.domain.repository.ToDoListRepository

class GetToDoListUseCase(private val toDoListRepository: ToDoListRepository) {
  fun getToDoList(): LiveData<List<ToDoItem>> {
    return toDoListRepository.getToDoList()
  }
}