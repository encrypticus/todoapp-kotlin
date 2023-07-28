package com.timofeev.todoapp.presentation

import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.Spinner
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.timofeev.todoapp.R
import com.timofeev.todoapp.domain.entities.Priority
import com.timofeev.todoapp.domain.entities.ToDoItem
import com.timofeev.todoapp.presentation.fragments.list.ListFragmentDirections

@BindingAdapter("visibility")
fun bindVisibility(view: View, visible: Boolean) {
  view.isVisible = visible
}

@BindingAdapter("navigateToAddFragment")
fun navigateToAddFragment(view: FloatingActionButton, navigate: Boolean) {
  view.setOnClickListener {
    if (navigate) {
      view.findNavController().navigate(R.id.action_listFragment_to_addFragment)
    }
  }
}

@BindingAdapter("navigateToUpdateFragment")
fun navigateToUpdateFragment(view: ConstraintLayout, toDoItem: ToDoItem) {
  view.setOnClickListener {
    val action = ListFragmentDirections.actionListFragmentToUpdateFragment(toDoItem)
    view.findNavController().navigate(action)
  }
}

@BindingAdapter("priority")
fun bindPriority(view: Spinner, priority: Priority) {
  when (priority) {
    Priority.LOW -> view.setSelection(0)
    Priority.MEDIUM -> view.setSelection(1)
    Priority.HIGH -> view.setSelection(2)
    else -> view.setSelection(0)
  }
}

@BindingAdapter("onSpinnerItemSelectedListener")
fun bindOnSpinnerItemSelectedListener(view: Spinner, listener: AdapterView.OnItemSelectedListener) {
  view.onItemSelectedListener = listener
}

@BindingAdapter("cardBgColor")
fun bindCardBgColor(cardView: CardView, toDoItem: ToDoItem) {
  Log.d("Priority", toDoItem.priority.name)
  when (toDoItem.priority) {
    Priority.LOW -> cardView.setCardBackgroundColor(cardView.context.getColor(R.color.green))
    Priority.MEDIUM -> cardView.setCardBackgroundColor(cardView.context.getColor(R.color.yellow))
    Priority.HIGH -> cardView.setCardBackgroundColor(cardView.context.getColor(R.color.red))
    else -> cardView.setCardBackgroundColor(cardView.context.getColor(R.color.green))
  }
}