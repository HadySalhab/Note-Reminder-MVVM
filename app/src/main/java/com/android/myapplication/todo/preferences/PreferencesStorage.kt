package com.android.myapplication.todo.preferences

import android.content.Context

private const val PREF_PERMISSION_KEY = "permissionKey"
private const val PREF_SPINNER_KEY = "spinnerKey"

object PreferencesStorage {
    fun getStoredPermission(context: Context): Boolean {
        val prefs = androidx.preference.PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getBoolean(PREF_PERMISSION_KEY, true)
    }

    fun setStoredPermission(context: Context, value: Boolean) {
        androidx.preference.PreferenceManager.getDefaultSharedPreferences(context)
            .edit()
            .putBoolean(PREF_PERMISSION_KEY, value)
            .apply()
    }
    fun getStoredPosition(context: Context): Int {
        val prefs = androidx.preference.PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getInt(PREF_SPINNER_KEY, 0)
    }

    fun setStoredPosition(context: Context, value: Int) {
        androidx.preference.PreferenceManager.getDefaultSharedPreferences(context)
            .edit()
            .putInt(PREF_SPINNER_KEY, value)
            .apply()
    }

}
