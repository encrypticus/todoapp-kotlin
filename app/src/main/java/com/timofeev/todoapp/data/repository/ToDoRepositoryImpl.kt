package com.timofeev.todoapp.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.timofeev.todoapp.data.AppDatabase
import com.timofeev.todoapp.data.ToDoListMapper
import com.timofeev.todoapp.domain.entities.ToDoItem
import com.timofeev.todoapp.domain.repository.ToDoListRepository

class ToDoRepositoryImpl(application: Application) : ToDoListRepository {
  private val toDoDao = AppDatabase.getInstance(application).toDoDao()
  private val mapper = ToDoListMapper()

  override suspend fun addToDoItem(toDoItem: ToDoItem) {
    toDoDao.addToDoItem(mapper.mapEntityToDbModel(toDoItem))
  }

  override suspend fun editToDoItem(toDoItem: ToDoItem) {
    toDoDao.updateToDoItem(mapper.mapEntityToDbModel(toDoItem))
  }

  override suspend fun deleteToDoItem(toDoItem: ToDoItem) {
    toDoDao.deleteToDoItem(mapper.mapEntityToDbModel(toDoItem))
  }

  override suspend fun deleteAll() {
    toDoDao.deleteAll()
  }

  override fun searchToDoItem(searchQuery: String): LiveData<List<ToDoItem>> =
    MediatorLiveData<List<ToDoItem>>().apply {
      addSource(toDoDao.searchTodoItem(searchQuery)) {
        value = mapper.mapListDbModelToListEntity(it)
      }
    }

  override fun getToDoList(): LiveData<List<ToDoItem>> = MediatorLiveData<List<ToDoItem>>().apply {
    addSource(toDoDao.getAllData()) {
      value = mapper.mapListDbModelToListEntity(it)
    }
  }

  override fun getToDoListSortedByLowPriority(): LiveData<List<ToDoItem>> =
    MediatorLiveData<List<ToDoItem>>().apply {
      addSource(toDoDao.sortByLowPriority()) {
        value = mapper.mapListDbModelToListEntity(it)
      }
    }

  override fun getToDoListSortedByMediumPriority(): LiveData<List<ToDoItem>> =
    MediatorLiveData<List<ToDoItem>>().apply {
      addSource(toDoDao.sortByMediumPriority()) {
        value = mapper.mapListDbModelToListEntity(it)
      }
    }

  override fun getToDoListSortedByHighPriority(): LiveData<List<ToDoItem>> =
    MediatorLiveData<List<ToDoItem>>().apply {
      addSource(toDoDao.sortByHighPriority()) {
        value = mapper.mapListDbModelToListEntity(it)
      }
    }

//  override suspend fun getToDoItem(toDoItemId: Int): ToDoItem {
//    TODO("Not yet implemented")
//  }
}