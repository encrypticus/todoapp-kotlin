package com.timofeev.todoapp.data

import androidx.room.TypeConverter
import com.timofeev.todoapp.domain.entities.Priority

class Converter {

  @TypeConverter
  fun fromPriority(priority: Priority): String {
    return priority.name
  }

  @TypeConverter
  fun toPriority(priority: String): Priority {
    return Priority.valueOf(priority)
  }
}