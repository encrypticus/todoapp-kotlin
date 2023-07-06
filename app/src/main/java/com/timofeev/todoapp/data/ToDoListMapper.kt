package com.timofeev.todoapp.data

import com.timofeev.todoapp.data.models.ToDoItemDbModel
import com.timofeev.todoapp.domain.entities.ToDoItem

class ToDoListMapper {
  fun mapEntityToDbModel(toDoItem: ToDoItem): ToDoItemDbModel = ToDoItemDbModel(
    id = toDoItem.id,
    title = toDoItem.title,
    priority = toDoItem.priority,
    description = toDoItem.description
  )

  fun mapDbModelToEntity(toDoItemDbModel: ToDoItemDbModel): ToDoItem = ToDoItem(
    id = toDoItemDbModel.id,
    title = toDoItemDbModel.title,
    priority = toDoItemDbModel.priority,
    description = toDoItemDbModel.description
  )

  fun mapListDbModelToListEntity(list: List<ToDoItemDbModel>) = list.map {
    mapDbModelToEntity(it)
  }
}