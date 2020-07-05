package pe.com.creamos.catedrappv2

import android.app.Application
import pe.com.creamos.catedrappv2.di.PrefsModule
import pe.com.creamos.catedrappv2.util.SharePreferencesHelper

class PrefsModuleTest(private val mockPreferences: SharePreferencesHelper) : PrefsModule() {

    override fun providesSharedPreferences(app: Application): SharePreferencesHelper {
        return mockPreferences
    }
}