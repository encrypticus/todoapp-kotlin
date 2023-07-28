package com.timofeev.todoapp.presentation.fragments.update

import android.app.AlertDialog
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
import androidx.navigation.fragment.navArgs
import com.timofeev.todoapp.R
import com.timofeev.todoapp.presentation.fragments.ToDoViewModel
import com.timofeev.todoapp.databinding.FragmentUpdateBinding
import com.timofeev.todoapp.domain.entities.ToDoItem
import com.timofeev.todoapp.presentation.fragments.SharedViewModel

class UpdateFragment : Fragment(), MenuProvider {
  private var _binding: FragmentUpdateBinding? = null
  private val binding: FragmentUpdateBinding
    get() = _binding ?: throw RuntimeException("FragmentUpdateBinding == null")
  private val args by navArgs<UpdateFragmentArgs>()
  private val sharedViewModel: SharedViewModel by viewModels()
  private val toDoViewModel: ToDoViewModel by viewModels()

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentUpdateBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    setupValues()
    setupOptionsMenu()
  }

  override fun onDestroy() {
    super.onDestroy()
    _binding = null
  }

  override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
    menuInflater.inflate(R.menu.update_fragment_menu, menu)
  }

  override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
    when (menuItem.itemId) {
      R.id.menu_save -> updateItem()
      R.id.menu_delete -> confirmItemRemoval()
    }
    return true
  }

  private fun confirmItemRemoval() {
    with(AlertDialog.Builder(requireContext())) {
      setPositiveButton(R.string.ok) { _, _ ->
        toDoViewModel.deleteToDoItem(args.currentItem)
        findNavController().popBackStack()
      }
      setNegativeButton(R.string.cancel) { _, _ -> }
      setTitle(
        String.format(getString(R.string.delete),
          "'${args.currentItem.title}'")
      )
      setMessage(
        String.format(getString(R.string.confirm_deletion_item),
        "'${args.currentItem.title}'")
      )
      create().show()
    }
  }

  private fun updateItem() {
    val title = binding.etCurrentTitle.text.toString()
    val description = binding.etCurrentDescription.text.toString()
    val priority = binding.spinnerCurrentPriorities.selectedItem.toString()
    val validation = sharedViewModel.verifyDataFromUser(title, description)

    if (validation) {
      val updatedItem = ToDoItem(
        args.currentItem.id,
        title,
        sharedViewModel.parsePriority(priority),
        description
      )
      toDoViewModel.updateToDoItem(updatedItem)
      findNavController().navigate(R.id.action_updateFragment_to_listFragment)
    } else {
      Toast.makeText(
        requireContext(),
        R.string.fill_out_all_fields,
        Toast.LENGTH_SHORT
      ).show()
    }
  }

  private fun setupOptionsMenu() {
    activity?.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
  }

  private fun setupValues() {
    with(binding) {
      arguments = args
      onSpinnerItemSelectedListener = sharedViewModel
      lifecycleOwner = viewLifecycleOwner
    }
  }
}