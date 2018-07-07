package xyz.mcomella.noncontactcallblocker.config

import android.os.Bundle
import android.support.v7.preference.PreferenceFragmentCompat
import xyz.mcomella.noncontactcallblocker.R

/** A screen that lets the user configure critical values like if the app is enabled. */
class ConfigurationFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) =
            setPreferencesFromResource(R.xml.prefs_configuration, rootKey)

    companion object {
        fun newInstance() = ConfigurationFragment()
    }
}
