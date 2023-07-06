package com.timofeev.todoapp.domain.useCases

import com.timofeev.todoapp.domain.repository.ToDoListRepository

class DeleteAllUseCase(private val repository: ToDoListRepository) {
  suspend fun deleteAll() {
    repository.deleteAll()
  }
}