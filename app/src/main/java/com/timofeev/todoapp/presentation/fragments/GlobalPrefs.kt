package com.timofeev.todoapp.presentation.fragments

import android.content.Context
import androidx.preference.PreferenceManager
import com.timofeev.todoapp.domain.entities.Priority

private const val CONFIRM_DELETE_ITEM_KEY = "PREF_CONFIRM_DELETE_ITEM_KEY"
private const val PREF_LAYOUT_KEY = "PREF_LAYOUT_KEY"
private const val PREF_PRIORITY_KEY = "PREF_PRIORITY_KEY"

object GlobalPrefs {

  fun getStoredConfirmDelete(context: Context): Boolean {
    return PreferenceManager.getDefaultSharedPreferences(context)
      .getBoolean(CONFIRM_DELETE_ITEM_KEY, false)
  }

  fun getStoredLayout(context: Context): Boolean {
    return PreferenceManager.getDefaultSharedPreferences(context)
      .getBoolean(PREF_LAYOUT_KEY, false)
  }

  fun setStoredLayout(context: Context, isGridView: Boolean) {
    PreferenceManager.getDefaultSharedPreferences(context)
      .edit()
      .putBoolean(PREF_LAYOUT_KEY, isGridView)
      .apply()
  }

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