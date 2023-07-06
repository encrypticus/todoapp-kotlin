package com.timofeev.todoapp.presentation.fragments.list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.timofeev.todoapp.data.repository.ToDoRepositoryImpl
import com.timofeev.todoapp.domain.useCases.GetToDoListSortedByHighPriorityUseCase
import com.timofeev.todoapp.domain.useCases.GetToDoListSortedByLowPriorityUseCase
import com.timofeev.todoapp.domain.useCases.GetToDoListSortedByMediumPriorityUseCase
import com.timofeev.todoapp.domain.useCases.GetToDoListUseCase

class ToDoListViewModal(application: Application): AndroidViewModel(application) {
  private val repository: ToDoRepositoryImpl = ToDoRepositoryImpl(application)

  private val getToDoListUseCase = GetToDoListUseCase(repository)
  private val getToDoListSortedByLowPriorityUseCase =
    GetToDoListSortedByLowPriorityUseCase(repository)
  private val getToDoListSortedByMediumPriorityUseCase =
    GetToDoListSortedByMediumPriorityUseCase(repository)
  private val getToDoListSortedByHighPriorityUseCase =
    GetToDoListSortedByHighPriorityUseCase(repository)
}