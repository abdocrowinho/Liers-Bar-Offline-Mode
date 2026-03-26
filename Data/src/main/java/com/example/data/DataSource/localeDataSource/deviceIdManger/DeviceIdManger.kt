package com.example.data.DataSource.localeDataSource.deviceIdManger
import android.content.Context
import java.util.UUID

object DeviceIdManager {
    private const val PREF_KEY = "device_id"
    private const val PREF_NAME = "device_prefs"

    fun getDeviceId(context: Context): String {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(PREF_KEY, null) ?: run {
            val newId = UUID.randomUUID().toString()
            prefs.edit().putString(PREF_KEY, newId).apply()
            newId
        }
    }
}