package com.dicoding.githubuser.fragment

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.dicoding.githubuser.R
import com.dicoding.githubuser.receiver.AlarmReceiver

class MyPreferanceFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var ALARM: String
    private lateinit var LANGUAGES: String

    private lateinit var alarmPreferance: SwitchPreference
    private lateinit var languagePreference: Preference
    private lateinit var alarmReceiver: AlarmReceiver

    override fun onCreatePreferences(bundle: Bundle?, string: String?) {
        addPreferencesFromResource(R.xml.preferences)
        init()
    }

    private fun init() {
        ALARM = resources.getString(R.string.key_alarm)
        LANGUAGES = resources.getString(R.string.key_languages)

        alarmPreferance = findPreference<SwitchPreference>(ALARM) as SwitchPreference
        languagePreference = findPreference<Preference>(LANGUAGES) as Preference
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == ALARM) {
            val alarm = sharedPreferences?.getBoolean(key, true)
            alarmReceiver = AlarmReceiver()
            if (alarm == true) {
                context?.let { alarmReceiver.setRepeatingAlarm(it, getString(R.string.reminder), getString(R.string.goBack)) }
            } else {
                context?.let { alarmReceiver.cancelRepeatingAlarm(it, AlarmReceiver.TYPE_REPEATING) }
            }
        }
        else if (key == LANGUAGES){
            val langIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(langIntent)
        }
    }
}