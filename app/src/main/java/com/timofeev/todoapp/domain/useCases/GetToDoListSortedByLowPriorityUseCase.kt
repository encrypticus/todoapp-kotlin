package com.timofeev.todoapp.domain.useCases

import androidx.lifecycle.LiveData
import com.timofeev.todoapp.domain.entities.ToDoItem
import com.timofeev.todoapp.domain.repository.ToDoListRepository

class GetToDoListSortedByLowPriorityUseCase(private val repository: ToDoListRepository) {
  fun getToDoListSortedByLowPriority(): LiveData<List<ToDoItem>> {
    return repository.getToDoListSortedByLowPriority()
  }
}