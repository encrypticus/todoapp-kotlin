package com.timofeev.todoapp.domain.useCases

import androidx.lifecycle.LiveData
import com.timofeev.todoapp.domain.entities.ToDoItem
import com.timofeev.todoapp.domain.repository.ToDoListRepository

class GetToDoListSortedByHighPriorityUseCase(private val repository: ToDoListRepository) {
  fun getToDoListSortedByHighPriority(): LiveData<List<ToDoItem>> {
    return repository.getToDoListSortedByHighPriority()
  }
}