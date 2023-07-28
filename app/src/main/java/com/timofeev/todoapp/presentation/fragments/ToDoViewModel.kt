package com.timofeev.todoapp.presentation.fragments

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.timofeev.todoapp.data.repository.ToDoRepositoryImpl
import com.timofeev.todoapp.domain.entities.ToDoItem
import com.timofeev.todoapp.domain.useCases.AddToDoItemUseCase
import com.timofeev.todoapp.domain.useCases.DeleteAllUseCase
import com.timofeev.todoapp.domain.useCases.DeleteToDoItemUseCase
import com.timofeev.todoapp.domain.useCases.EditToDoItemUseCase
import com.timofeev.todoapp.domain.useCases.GetToDoListSortedByHighPriorityUseCase
import com.timofeev.todoapp.domain.useCases.GetToDoListSortedByLowPriorityUseCase
import com.timofeev.todoapp.domain.useCases.GetToDoListSortedByMediumPriorityUseCase
import com.timofeev.todoapp.domain.useCases.GetToDoListUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ToDoViewModel(application: Application) : AndroidViewModel(application) {
  private val repository: ToDoRepositoryImpl = ToDoRepositoryImpl(application)

  private val getToDoListUseCase = GetToDoListUseCase(repository)

  private val getToDoListSortedByLowPriorityUseCase =
    GetToDoListSortedByLowPriorityUseCase(repository)

  private val getToDoListSortedByMediumPriorityUseCase =
    GetToDoListSortedByMediumPriorityUseCase(repository)

  private val getToDoListSortedByHighPriorityUseCase =
    GetToDoListSortedByHighPriorityUseCase(repository)

  private val addToDoItemUseCase = AddToDoItemUseCase(repository)
  private val editToDoItemUseCase = EditToDoItemUseCase(repository)
  private val deleteToDoItemUseCase = DeleteToDoItemUseCase(repository)
  private val deleteAllUseCase = DeleteAllUseCase(repository)

  val toDoList = getToDoListUseCase.getToDoList()

  val toDoListSortedByLowPriority =
    getToDoListSortedByLowPriorityUseCase.getToDoListSortedByLowPriority()

  val toDoListSortedByMediumPriority =
    getToDoListSortedByMediumPriorityUseCase.getToDoListSortedByMediumPriority()

  val toDoListSortedByHighPriority =
    getToDoListSortedByHighPriorityUseCase.getToDoListSortedByHighPriority()

  fun addToDoItem(toDoItem: ToDoItem) {
    viewModelScope.launch(Dispatchers.IO) {
      addToDoItemUseCase.addToDoItem(toDoItem)
    }
  }

  fun updateToDoItem(toDoItem: ToDoItem) {
    viewModelScope.launch(Dispatchers.IO) {
      editToDoItemUseCase.editToDoItem(toDoItem)
    }
  }

  fun deleteToDoItem(toDoItem: ToDoItem) {
    viewModelScope.launch(Dispatchers.IO) {
      deleteToDoItemUseCase.deleteToDoItem(toDoItem)
    }
  }

  fun deleteAll() {
    viewModelScope.launch(Dispatchers.IO) {
      deleteAllUseCase.deleteAll()
    }
  }

  fun searchToDoItem(searchQuery: String): LiveData<List<ToDoItem>> {
    return repository.searchToDoItem(searchQuery)
  }
}