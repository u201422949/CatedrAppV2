package pe.com.creamos.catedrappv2.util

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.core.content.edit

class SharePreferencesHelper {

    companion object {

        private const val PREF_INFO_VERSION = "PREF_INFO_VERSION"
        private var prefs: SharedPreferences? = null

        @Volatile
        private var instance: SharePreferencesHelper? = null
        private val LOCK = Any()

        operator fun invoke(context: Context): SharePreferencesHelper = instance ?: synchronized(
            LOCK
        ) {
            instance ?: buildHelper(context).also {
                instance = it
            }
        }

        private fun buildHelper(context: Context): SharePreferencesHelper {
            prefs = PreferenceManager.getDefaultSharedPreferences(context)
            return SharePreferencesHelper()
        }
    }

    fun setInfoVersion(version: String) {
        prefs?.edit(commit = true) {
            putString(PREF_INFO_VERSION, version)
        }
    }

    fun getInfoVersion() = prefs?.getString(PREF_INFO_VERSION, "1.0")

}
