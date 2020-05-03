package pe.com.creamos.catedrappv2.di

import android.app.Application
import dagger.Module
import dagger.Provides
import pe.com.creamos.catedrappv2.util.SharePreferencesHelper
import javax.inject.Singleton

@Module
open class PrefsModule {

    @Provides
    @Singleton
    open fun providesSharedPreferences(app: Application): SharePreferencesHelper {
        return SharePreferencesHelper(app)
    }
}
