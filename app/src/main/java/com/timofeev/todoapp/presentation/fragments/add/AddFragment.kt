package com.timofeev.todoapp.presentation.fragments.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.timofeev.todoapp.R
import com.timofeev.todoapp.data.models.ToDoItemDbModel
import com.timofeev.todoapp.data.viewmodel.ToDoViewModel
import com.timofeev.todoapp.databinding.FragmentAddBinding
import com.timofeev.todoapp.domain.entities.ToDoItem
import com.timofeev.todoapp.presentation.fragments.SharedViewModel

class AddFragment : Fragment(), MenuProvider {
  private var _binding: FragmentAddBinding? = null
  private val binding: FragmentAddBinding
    get() = _binding ?: throw RuntimeException("FragmentAddBinding == null")

  private val viewModel: ToDoViewModel by viewModels()
  private val sharedViewModel: SharedViewModel by viewModels()

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentAddBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    activity?.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
    binding.spinnerPriorities.onItemSelectedListener = sharedViewModel
  }

  override fun onDestroy() {
    super.onDestroy()
    _binding = null
  }

  override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
    menuInflater.inflate(R.menu.add_fragment_menu, menu)
  }

  override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
    if (menuItem.itemId == R.id.menu_add) {
      addToDoItem()
    }
    return true
  }

  private fun addToDoItem() {
    with(binding) {
      val title = etTitle.text.toString()
      val description = etDescription.text.toString()
      val priority = spinnerPriorities.selectedItem.toString()

      val validation = sharedViewModel.verifyDataFromUser(title, description)

      if (validation) {
        val toDoItem = ToDoItem(
          0,
          title,
          sharedViewModel.parsePriority(priority),
          description
        )

        viewModel.addToDoItem(toDoItem)
        findNavController().navigate(R.id.action_addFragment_to_listFragment)
      } else {
        Toast.makeText(
          requireContext(),
          R.string.fill_out_all_fields,
          Toast.LENGTH_SHORT
        ).show()
      }
    }
  }
}