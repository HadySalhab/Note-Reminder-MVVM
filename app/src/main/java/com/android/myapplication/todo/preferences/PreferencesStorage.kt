package com.android.myapplication.todo.preferences

import android.content.Context

private const val PREF_PERMISSION_KEY = "permissionKey"

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
}
