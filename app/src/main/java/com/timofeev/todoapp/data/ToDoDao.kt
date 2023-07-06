package com.timofeev.todoapp.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.timofeev.todoapp.data.models.ToDoItemDbModel

@Dao
interface ToDoDao {

  @Query("SELECT * FROM todo_table ORDER BY id")
  fun getAllData(): LiveData<List<ToDoItemDbModel>>

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  suspend fun addToDoItem(toDoItemDbModel: ToDoItemDbModel)

  @Update
  suspend fun updateToDoItem(toDoItemDbModel: ToDoItemDbModel)

  @Delete
  suspend fun deleteToDoItem(toDoItemDbModel: ToDoItemDbModel)

  @Query("DELETE from todo_table")
  suspend fun deleteAll()

  @Query("SELECT * FROM todo_table WHERE title LIKE :searchQuery")
  fun searchTodoItem(searchQuery: String): LiveData<List<ToDoItemDbModel>>

  @Query("SELECT * FROM todo_table ORDER BY CASE WHEN priority LIKE 'L%' THEN 1 WHEN priority LIKE 'M%' THEN 2 WHEN priority LIKE 'H%' THEN 3 END")
  fun sortByLowPriority(): LiveData<List<ToDoItemDbModel>>

  @Query("SELECT * FROM todo_table ORDER BY CASE WHEN priority LIKE 'M%' THEN 1 WHEN priority LIKE 'L%' THEN 2 WHEN priority LIKE 'H%' THEN 3 END")
  fun sortByMediumPriority(): LiveData<List<ToDoItemDbModel>>

  @Query("SELECT * FROM todo_table ORDER BY CASE WHEN priority LIKE 'H%' THEN 1 WHEN priority LIKE 'L%' THEN 2 WHEN priority LIKE 'M%' THEN 3 END")
  fun sortByHighPriority(): LiveData<List<ToDoItemDbModel>>
}