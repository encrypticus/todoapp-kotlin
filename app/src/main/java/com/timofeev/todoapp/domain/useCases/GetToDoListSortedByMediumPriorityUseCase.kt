package com.timofeev.todoapp.domain.useCases

import androidx.lifecycle.LiveData
import com.timofeev.todoapp.domain.entities.ToDoItem
import com.timofeev.todoapp.domain.repository.ToDoListRepository

class GetToDoListSortedByMediumPriorityUseCase(private val repository: ToDoListRepository) {
  fun getToDoListSortedByMediumPriority(): LiveData<List<ToDoItem>> {
    return repository.getToDoListSortedByMediumPriority()
  }
}