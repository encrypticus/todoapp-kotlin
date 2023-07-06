package com.timofeev.todoapp.data

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.timofeev.todoapp.data.models.ToDoItemDbModel

@Database(entities = [ToDoItemDbModel::class], version = 1, exportSchema = false)
@TypeConverters(Converter::class)
abstract class AppDatabase: RoomDatabase() {
  abstract fun toDoDao(): ToDoDao

  companion object {
    private var INSTANCE: AppDatabase? = null
    private val LOCK = Any()
    private const val DB_NAME = "todo.db"

    fun getInstance(application: Application): AppDatabase {
      INSTANCE?.let {
        return it
      }
      synchronized(LOCK) {
        INSTANCE?.let {
          return it
        }
        val db = Room.databaseBuilder(
          application,
          AppDatabase::class.java,
          DB_NAME
        ).build()
        INSTANCE = db
        return db
      }
    }
  }
}