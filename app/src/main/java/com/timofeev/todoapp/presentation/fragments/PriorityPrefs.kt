package com.timofeev.todoapp.presentation.fragments

import android.content.Context
import androidx.preference.PreferenceManager
import com.timofeev.todoapp.domain.entities.Priority

private const val PREF_PRIORITY_KEY = "PREF_PRIORITY_KEY"

object PriorityPrefs {
  fun getStoredPriority(context: Context): String {
    return PreferenceManager.getDefaultSharedPreferences(context)
      .getString(PREF_PRIORITY_KEY, Priority.NONE.name) ?: Priority.NONE.name
  }

  fun setStoredPriority(context: Context, priority: String) {
    PreferenceManager.getDefaultSharedPreferences(context)
      .edit()
      .putString(PREF_PRIORITY_KEY, priority)
      .apply()
  }
}