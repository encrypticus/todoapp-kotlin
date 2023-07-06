package com.timofeev.todoapp.presentation.fragments.list

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.timofeev.todoapp.R
import com.timofeev.todoapp.data.models.ToDoItemDbModel
import com.timofeev.todoapp.data.viewmodel.ToDoViewModel
import com.timofeev.todoapp.databinding.FragmentListBinding
import com.timofeev.todoapp.domain.entities.ToDoItem
import com.timofeev.todoapp.presentation.fragments.list.adpater.ToDoListAdapter
import jp.wasabeef.recyclerview.animators.FlipInTopXAnimator

class ListFragment : Fragment(), MenuProvider, SearchView.OnQueryTextListener {
  private var _binding: FragmentListBinding? = null
  private val binding: FragmentListBinding
    get() = _binding ?: throw RuntimeException("FragmentListBinding == null")

  private val toDoListAdapter: ToDoListAdapter by lazy { ToDoListAdapter() }
  private val toDoViewModel: ToDoViewModel by viewModels()

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    _binding = FragmentListBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    activity?.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
    binding.lifecycleOwner = viewLifecycleOwner

    setupRecyclerView()
    observeViewModel()
    setupSwipeListener(binding.recyclerView)
  }

  override fun onDestroy() {
    super.onDestroy()
    _binding = null
  }

  override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
    menuInflater.inflate(R.menu.list_fragment_menu, menu)
    setupSearchView(menu)
  }

  private fun setupSearchView(menu: Menu) {
    val search = menu.findItem(R.id.menu_search)
    val searchView = search.actionView as? SearchView
    searchView?.isSubmitButtonEnabled = true
    searchView?.setOnQueryTextListener(this)
  }

  override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
    when (menuItem.itemId) {
      R.id.menu_priority_low -> {
        toDoViewModel.toDoListSortedByLowPriority.observe(viewLifecycleOwner) {
          toDoListAdapter.submitList(it)
        }
      }
      R.id.menu_priority_medium -> {
        toDoViewModel.toDoListSortedByMediumPriority.observe(viewLifecycleOwner) {
          toDoListAdapter.submitList(it)
        }
      }
      R.id.menu_priority_high -> {
        toDoViewModel.toDoListSortedByHighPriority.observe(viewLifecycleOwner) {
          toDoListAdapter.submitList(it)
        }
      }
      R.id.menu_delete_all -> confirmRemoval()
    }
    return true
  }

  override fun onQueryTextSubmit(query: String?): Boolean {
    if (query != null) {
      searchThroughDatabase(query)
    }
    return true
  }

  override fun onQueryTextChange(query: String?): Boolean {
    if (query != null) {
      searchThroughDatabase(query)
    }
    return true
  }

  // Custom methods
  private fun searchThroughDatabase(query: String) {
    val searchQuery = "%$query%"
    toDoViewModel.searchToDoItem(searchQuery).observe(viewLifecycleOwner) {
      it?.let {
        toDoListAdapter.submitList(it)
      }
    }
  }

  private fun setupRecyclerView() {
    with(binding.recyclerView) {
      adapter = toDoListAdapter
      layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
      itemAnimator = FlipInTopXAnimator().apply {
        addDuration = 300
      }
    }
  }

  private fun observeViewModel() {
    toDoViewModel.toDoList.observe(viewLifecycleOwner) {
      toDoListAdapter.submitList(it)
      showNoDataViews(it)
    }
  }

  private fun showNoDataViews(toDoList: List<ToDoItem>) {
    binding.visibility = toDoList.isEmpty()
  }

  private fun confirmRemoval() {
    with(AlertDialog.Builder(requireContext())) {
      setPositiveButton(R.string.ok) { _, _ ->
        toDoViewModel.deleteAll()
      }
      setNegativeButton(R.string.cancel) { _, _ -> }
      setTitle(R.string.delete_everything)
      setMessage(R.string.confirm_deletion_of_everything)
      create().show()
    }
  }

  private fun setupSwipeListener(recyclerView: RecyclerView) {
    val callback = object : ItemTouchHelper.SimpleCallback(
      0,
      ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
    ) {
      override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
      ): Boolean {
        return false
      }

      override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val toDoItem = toDoListAdapter.currentList[viewHolder.adapterPosition]
        toDoViewModel.deleteToDoItem(toDoItem)
        restoreDeletedToDoItem(viewHolder.itemView, toDoItem)
      }
    }
    val itemTouchHelper = ItemTouchHelper(callback)
    itemTouchHelper.attachToRecyclerView(recyclerView)
  }

  private fun restoreDeletedToDoItem(view: View, deletedItem: ToDoItem) {
    val snackBar = Snackbar.make(
      view,
      String.format(getString(R.string.deleted), "'${deletedItem.title}'"),
      Snackbar.LENGTH_LONG
    )
    snackBar.setAction(R.string.restore) {
      toDoViewModel.addToDoItem(deletedItem)
    }
    snackBar.show()
  }

}