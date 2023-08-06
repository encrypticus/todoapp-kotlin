package com.timofeev.todoapp.presentation

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.timofeev.todoapp.R
import com.timofeev.todoapp.databinding.ActivityMainBinding
import com.timofeev.todoapp.domain.entities.Theme
import com.timofeev.todoapp.domain.entities.ThemeColor
import com.timofeev.todoapp.presentation.fragments.GlobalPrefs

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

  private lateinit var binding: ActivityMainBinding
  private lateinit var navBuilder: NavOptions.Builder

  private val navController by lazy {
    val navHostFragment = supportFragmentManager
      .findFragmentById(R.id.navHostFragment) as NavHostFragment
    navHostFragment.navController
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val currentTheme = GlobalPrefs.getStoredTheme(this)
    val currentThemeColor = GlobalPrefs.getStoredThemeColor(this)
    applySelectedTheme(currentTheme)
    applySelectedThemeColor(currentThemeColor)

    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)

    setupToolBar()
    setupNavBuilder()

    binding.navView.setNavigationItemSelectedListener(this)
  }

  private fun applySelectedThemeColor(currentThemeColor: String) {
    when(currentThemeColor) {
      ThemeColor.TEAL.name -> {
        theme.applyStyle(R.style.Theme_Teal, true)
      }
      ThemeColor.INDIGO.name -> {
        theme.applyStyle(R.style.Theme_Indigo, true)
      }
      ThemeColor.ORANGE.name -> {
        theme.applyStyle(R.style.Theme_Orange, true)
      }
    }
  }

  private fun applySelectedTheme(currentTheme: String) {
    when(currentTheme) {
      Theme.DAY.name -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
      Theme.NIGHT.name -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
      Theme.SYSTEM.name -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
    }
  }

  private fun setupToolBar() {
    setSupportActionBar(binding.toolbar)
    val appBarConfiguration = AppBarConfiguration(navController.graph, binding.drawerLayout)
    binding.toolbar.setupWithNavController(navController, appBarConfiguration)

    navController.addOnDestinationChangedListener { _, destination, _ ->
      if (destination.id != R.id.listFragment) {
        binding.toolbar.setNavigationIcon(R.drawable.ic_back)
      }
    }
  }

  private fun setupNavBuilder() {
    navBuilder = NavOptions.Builder().apply {
      setEnterAnim(R.anim.from_right)
      setExitAnim(R.anim.to_right)
      setPopEnterAnim(R.anim.from_right)
      setPopExitAnim(R.anim.to_right)
    }
  }

  override fun onNavigationItemSelected(item: MenuItem): Boolean {
    Log.d("MainActivity", "onNavigationItemSelected: ${item.itemId}")
    item.isChecked = true
    binding.drawerLayout.closeDrawers()

    when (item.itemId) {
      R.id.menu_preference -> {
        navController.navigate(R.id.preferenceFragment, null, navBuilder.build())
      }
    }

    return true
  }
}