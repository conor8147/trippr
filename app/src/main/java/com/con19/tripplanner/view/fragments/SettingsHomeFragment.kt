package com.con19.tripplanner.view.fragments


import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.preference.EditTextPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.con19.tripplanner.R

/**
 * Fragment for the Settings Tab.
 */
class SettingsHomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layout = inflater.inflate(R.layout.fragment_settings_home, container, false)
        val transaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.linearLayout, PreferencesFragment())
        transaction.commit()
        return layout
    }
}


class PreferencesFragment: PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preference_screen)
        val bankEditTextPreference : EditTextPreference? = findPreference("bank_details") as EditTextPreference?
        bankEditTextPreference?.setOnBindEditTextListener {
            it.inputType = InputType.TYPE_CLASS_NUMBER
        }

    }
}