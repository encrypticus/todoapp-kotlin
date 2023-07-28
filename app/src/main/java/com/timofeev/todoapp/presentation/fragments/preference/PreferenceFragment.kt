package com.timofeev.todoapp.presentation.fragments.preference

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.timofeev.todoapp.R

class PreferenceFragment: PreferenceFragmentCompat() {
  override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
    setPreferencesFromResource(R.xml.preference_screen, rootKey)
  }
}