/* Copyright (C) 2018 Michael Comella
 *
 * This file is part of NonContactCallBlocker.
 *
 *  NonContactCallBlocker is free software: you can redistribute it
 *  and/or modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation, either version 3 of
 *  the License, or (at your option) any later version.
 *
 *  NonContactCallBlocker is distributed in the hope that it will be
 *  useful, but WITHOUT ANY WARRANTY; without even the implied
 *  warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *  See the GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with NonContactCallBlocker.  If not, see
 *  <https://www.gnu.org/licenses/>. */

package xyz.mcomella.noncontactcallblocker.config

import android.content.SharedPreferences
import android.os.Bundle
import android.os.StrictMode
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import xyz.mcomella.noncontactcallblocker.R
import xyz.mcomella.noncontactcallblocker.ext.resetAfter
import xyz.mcomella.noncontactcallblocker.ext.serviceLocator

/** A screen that lets the user configure critical values like if the app is enabled. */
class ConfigurationFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var globalEnablePreference: SwitchPreference

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        // Strict mode override: this layout reflects what's on disk so we might as well block.
        StrictMode.allowThreadDiskWrites().resetAfter {
            setPreferencesFromResource(R.xml.prefs_configuration, rootKey)
        }
        globalEnablePreference = findPreference(context!!.getString(R.string.key_global_enable))!!
        context!!.serviceLocator.config.sharedPrefs.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPrefs: SharedPreferences, changedKey: String) {
        val context = context ?: return
        if (changedKey == context.getString(R.string.key_global_enable)) {
            globalEnablePreference.isChecked = sharedPrefs.getBoolean(changedKey, false)
        }
    }

    companion object {
        fun newInstance() = ConfigurationFragment()
    }
}
