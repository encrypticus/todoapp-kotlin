package com.timofeev.todoapp.presentation.fragments.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.timofeev.todoapp.R
import com.timofeev.todoapp.databinding.FragmentListBinding
import com.timofeev.todoapp.domain.entities.Priority
import com.timofeev.todoapp.domain.entities.ToDoItem
import com.timofeev.todoapp.presentation.fragments.GlobalPrefs
import com.timofeev.todoapp.presentation.fragments.ToDoViewModel
import com.timofeev.todoapp.presentation.fragments.list.adpater.ToDoListAdapter

class ListFragment : Fragment(), MenuProvider, SearchView.OnQueryTextListener {

  private var isGridView = false
  private lateinit var storedPriority: String
  private var isDeleteWithoutConfirmation = false

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

    isGridView = GlobalPrefs.getStoredLayout(requireContext())
    storedPriority = GlobalPrefs.getStoredPriority(requireContext())
    isDeleteWithoutConfirmation = GlobalPrefs.getStoredConfirmDelete(requireContext())

    setupRecyclerView()
    observeToDoList(storedPriority)
    setupSwipeListener(binding.recyclerView)
  }

  override fun onDestroy() {
    super.onDestroy()
    _binding = null
  }

  /**
   * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
   * MENU
   * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
   */
  override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
    menuInflater.inflate(R.menu.list_fragment_menu, menu)
    updateChangeViewItemIcon(menu.findItem(R.id.menu_change_view))
    setupSearchView(menu)
  }

  override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
    when (menuItem.itemId) {
      R.id.menu_priority_low -> {
        selectItem(Priority.LOW.name)
      }

      R.id.menu_priority_medium -> {
        selectItem(Priority.MEDIUM.name)
      }

      R.id.menu_priority_high -> {
        selectItem(Priority.HIGH.name)
      }

      R.id.menu_delete_all -> confirmRemoval()

      R.id.menu_reset_sorting -> {
        selectItem(Priority.NONE.name)
      }

      R.id.menu_change_view -> {
        isGridView = !isGridView
        GlobalPrefs.setStoredLayout(requireContext(), isGridView)
        switchRecyclerViewLayout()
        updateChangeViewItemIcon(menuItem)
      }
    }
    return true
  }

  private fun selectItem(priority: String) {
    GlobalPrefs.setStoredPriority(requireContext(), priority)
    observeToDoList(priority)
  }

  private fun updateChangeViewItemIcon(item: MenuItem) {
    val icon = if (isGridView) R.drawable.ic_listview else R.drawable.ic_gridview
    item.setIcon(icon)
  }

  /**
   * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
   * SEARCH
   * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
   */

  private fun setupSearchView(menu: Menu) {
    val search = menu.findItem(R.id.menu_search)
    val searchView = search.actionView as? SearchView
    searchView?.isSubmitButtonEnabled = true
    searchView?.setOnQueryTextListener(this)
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

  /**
   * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
   * RECYCLER VIEW
   * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
   */

  private fun switchRecyclerViewLayout() {
    with(binding.recyclerView) {
      layoutManager = if (isGridView) {
        StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
      } else {
        StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
      }
    }
  }

  private fun setupRecyclerView() {
    switchRecyclerViewLayout()
    with(binding.recyclerView) {
      adapter = toDoListAdapter
      itemAnimator = DefaultItemAnimator().apply {
        addDuration = 200
      }
    }
  }

  /**
   * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
   * RECYCLER VIEW
   * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
   */

  private fun observeToDoList(priority: String) {
    val toDoList = when (priority) {
      Priority.LOW.name -> {
        toDoViewModel.toDoListSortedByLowPriority
      }

      Priority.MEDIUM.name -> {
        toDoViewModel.toDoListSortedByMediumPriority
      }

      Priority.HIGH.name -> {
        toDoViewModel.toDoListSortedByHighPriority
      }

      else -> {
        toDoViewModel.toDoList
      }
    }
    toDoList.observe(viewLifecycleOwner) {
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

  private fun confirmItemRemoval(
    toDoItem: ToDoItem,
    recyclerView: RecyclerView,
    viewHolder: RecyclerView.ViewHolder,
    onConfirmCallback: () -> Unit
  ) {
    with(AlertDialog.Builder(requireContext())) {
      setPositiveButton(R.string.ok) { _, _ ->
        onConfirmCallback()
      }
      setNegativeButton(R.string.cancel) { _, _ ->
        recyclerView.adapter?.notifyItemChanged(viewHolder.adapterPosition)
      }
      setOnCancelListener {
        recyclerView.adapter?.notifyItemChanged(viewHolder.adapterPosition)
      }
      setTitle(
        String.format(
          getString(R.string.delete),
          "'${toDoItem.title}'"
        )
      )
      setMessage(
        String.format(
          getString(R.string.confirm_deletion_item),
          "'${toDoItem.title}'"
        )
      )
      create().show()
    }
  }

  private fun onDeleteItemCallback(
    toDoItem: ToDoItem,
    viewHolder: RecyclerView.ViewHolder
  ) {
    storedPriority = GlobalPrefs.getStoredPriority(requireContext())

    toDoViewModel.deleteToDoItem(toDoItem)
    restoreDeletedToDoItem(viewHolder.itemView, toDoItem)
//    observeToDoList(storedPriority)
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
        if (isDeleteWithoutConfirmation) {
          onDeleteItemCallback(toDoItem, viewHolder)
        } else {
          confirmItemRemoval(toDoItem, recyclerView, viewHolder) {
            onDeleteItemCallback(toDoItem, viewHolder)
          }
        }
      }

    }
    val itemTouchHelper = ItemTouchHelper(callback)
    itemTouchHelper.attachToRecyclerView(recyclerView)
  }

  private fun restoreDeletedToDoItem(view: View, deletedItem: ToDoItem) {
    val snackBar = Snackbar.make(
      view,
      String.format(getString(R.string.deleted), "'${deletedItem.title}'"),
      5000
    )
    snackBar.setAction(R.string.restore) {
      toDoViewModel.addToDoItem(deletedItem)
      storedPriority = GlobalPrefs.getStoredPriority(requireContext())
//      observeToDoList(storedPriority)
    }
    snackBar.show()
  }

}