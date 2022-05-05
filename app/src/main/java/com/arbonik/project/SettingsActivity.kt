package com.arbonik.project

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import kotlinx.android.synthetic.main.activity_main.*

class SettingsActivity : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener {
    val settingsFragment = SettingsFragment()
    var prefs: SharedPreferences? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)

        try {
            prefs = PreferenceManager.getDefaultSharedPreferences(applicationContext)
            if (prefs?.getInt("theme", 0) == AppCompatDelegate.MODE_NIGHT_YES) {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        } catch (e: java.lang.Exception) {
        }

        setSupportActionBar(toolbar)
        supportActionBar!!.setLogo(null)
        supportActionBar?.title = "Настройки"
        val context: Context = applicationContext
        prefs = PreferenceManager.getDefaultSharedPreferences(context)
        prefs?.registerOnSharedPreferenceChangeListener(this)

        if (savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.settings, settingsFragment)
                    .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        var list_themes: ListPreference? = null
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
            list_themes = findPreference("app_theme")
            list_themes?.summary = getDefaultSharedPreferences(context)?.getString("theme_summary", "")
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    @SuppressLint("CommitPrefEdits")
    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (sharedPreferences?.getString("app_theme", "") == "Тёмная") {
            sharedPreferences.edit()?.putString("theme_summary", "Тёмная")?.apply()
            sharedPreferences.edit()?.putInt("theme", AppCompatDelegate.MODE_NIGHT_YES)?.apply()
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
        else if (sharedPreferences?.getString("app_theme", "") == "Светлая") {
            sharedPreferences.edit()?.putString("theme_summary", "Светлая")?.apply()
            sharedPreferences.edit()?.putInt("theme", AppCompatDelegate.MODE_NIGHT_NO)?.apply()
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}