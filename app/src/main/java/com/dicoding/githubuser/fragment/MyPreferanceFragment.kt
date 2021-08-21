package com.dicoding.githubuser.fragment

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.dicoding.githubuser.R
import com.dicoding.githubuser.receiver.AlarmReceiver
import com.loopj.android.http.AsyncHttpClient.log

class MyPreferanceFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var ALARM: String
    private lateinit var LANGUAGES: String

    private lateinit var alarmPreferance: SwitchPreference
    private lateinit var languagePreference: Preference
    private lateinit var alarmReceiver: AlarmReceiver


    companion object {
        private const val DEFAULT_VALUE = "default_value"
    }

    override fun onCreatePreferences(bundle: Bundle?, string: String?) {
        addPreferencesFromResource(R.xml.preferences)
        init()

        log.d("MyPref", LANGUAGES.toString())
        log.d("MyPref", ALARM.toString())
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
        log.d("MyKey", key.toString())
        if (key == ALARM) {
            val alarm = sharedPreferences?.getBoolean(key, true)
            alarmReceiver = AlarmReceiver()
            log.d("MyAlarm", alarm.toString())
            if (alarm == true) {
                log.d("MyContext", alarm.toString())
                context?.let { alarmReceiver.setRepeatingAlarm(it, getString(R.string.reminder), getString(R.string.goBack)) }
            } else {
                log.d("MyContext", alarm.toString())
                context?.let { alarmReceiver.cancelRepeatingAlarm(it, AlarmReceiver.TYPE_REPEATING) }
            }
        }
//        else if (key == LANGUAGES){
//            val langIntnt = Intent(Settings.ACTION_LOCALE_SETTINGS)
//            startActivity(langIntnt)
//        }
    }
}