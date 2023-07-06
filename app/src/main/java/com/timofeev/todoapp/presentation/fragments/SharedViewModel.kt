package com.timofeev.todoapp.presentation.fragments

import android.app.Application
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import com.timofeev.todoapp.R
import com.timofeev.todoapp.domain.entities.Priority

class SharedViewModel(private val application: Application) : AndroidViewModel(application),
  AdapterView.OnItemSelectedListener {
  private val priorityLow = application.resources.getStringArray(R.array.priorities)[PRIORITY_LOW]
  private val priorityMedium =
    application.resources.getStringArray(R.array.priorities)[PRIORITY_MEDIUM]
  private val priorityHigh = application.resources.getStringArray(R.array.priorities)[PRIORITY_HIGH]

  fun verifyDataFromUser(title: String, description: String): Boolean {
    return if (TextUtils.isEmpty(title) || TextUtils.isEmpty(description)) {
      false
    } else {
      !(title.isEmpty() || description.isEmpty())
    }
  }

  fun parsePriority(priority: String): Priority {
    return when (priority) {
      priorityLow -> Priority.LOW
      priorityMedium -> Priority.MEDIUM
      priorityHigh -> Priority.HIGH
      else -> Priority.LOW
    }
  }

  override fun onItemSelected(parent: AdapterView<*>?, view: View?, positon: Int, id: Long) {
    view as TextView?
    when (positon) {
      PRIORITY_LOW -> {
        view?.setTextColor(
          ContextCompat.getColor(
            application,
            R.color.green
          )
        )
      }

      PRIORITY_MEDIUM -> {
        view?.setTextColor(
          ContextCompat.getColor(
            application,
            R.color.yellow
          )
        )
      }

      PRIORITY_HIGH -> {
        view?.setTextColor(
          ContextCompat.getColor(
            application,
            R.color.red
          )
        )
      }
    }
  }

  override fun onNothingSelected(parent: AdapterView<*>?) {
    TODO("Not yet implemented")
  }

  companion object {
    private const val PRIORITY_LOW = 0
    private const val PRIORITY_MEDIUM = 1
    private const val PRIORITY_HIGH = 2
  }

}