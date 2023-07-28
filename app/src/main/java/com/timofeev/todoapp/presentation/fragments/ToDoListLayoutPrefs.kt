package com.timofeev.todoapp.presentation.fragments

import android.content.Context
import androidx.preference.PreferenceManager

private const val PREF_LAYOUT_KEY = "PREF_LAYOUT_KEY"

object ToDoListLayoutPrefs {
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
}