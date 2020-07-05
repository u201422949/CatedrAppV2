package pe.com.creamos.catedrappv2.util

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.core.content.edit

class SharePreferencesHelper {

    companion object {
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

    fun setDataVersion(version: Int) {
        prefs?.edit(commit = true) {
            putInt(PREF_DATA_VERSION, version)
        }
    }

    fun getDataVersion() = prefs?.getInt(PREF_DATA_VERSION, PREF_INITIAL_DATA_VERSION)

}
