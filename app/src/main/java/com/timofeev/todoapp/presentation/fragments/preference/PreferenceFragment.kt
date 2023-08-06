package com.timofeev.todoapp.presentation.fragments.preference

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import com.timofeev.todoapp.R
import com.timofeev.todoapp.domain.entities.Theme
import com.timofeev.todoapp.domain.entities.ThemeColor
import com.timofeev.todoapp.presentation.fragments.GlobalPrefs

class PreferenceFragment: PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {
  override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
    setPreferencesFromResource(R.xml.preference_screen, rootKey)
    preferenceScreen.sharedPreferences?.registerOnSharedPreferenceChangeListener(this)
  }

  override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
    val currentThemeKey = GlobalPrefs.SET_CURRENT_THEME_KEY
    val currentThemeColorKey = GlobalPrefs.SET_CURRENT_THEME_COLOR_KEY

    if (key == currentThemeKey) {
      val themePreference = findPreference<ListPreference>(currentThemeKey)

      when(themePreference?.value) {
        Theme.DAY.name -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        Theme.NIGHT.name -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        Theme.SYSTEM.name -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
      }
    }

    if (key == currentThemeColorKey) {
      val themeColorPreference = findPreference<ListPreference>(currentThemeColorKey)

      when(themeColorPreference?.value) {
        ThemeColor.TEAL.name -> activity?.theme?.applyStyle(R.style.Theme_Indigo, true)
        ThemeColor.INDIGO.name -> activity?.theme?.applyStyle(R.style.Theme_Indigo, true)
        ThemeColor.ORANGE.name -> activity?.theme?.applyStyle(R.style.Theme_Orange, true)
      }
      activity?.recreate()
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    preferenceScreen.sharedPreferences?.unregisterOnSharedPreferenceChangeListener(this)
  }
}