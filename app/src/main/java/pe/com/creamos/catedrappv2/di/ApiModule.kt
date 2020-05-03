package pe.com.creamos.catedrappv2.di

import dagger.Module
import dagger.Provides
import pe.com.creamos.catedrappv2.model.CatedrappAPI
import pe.com.creamos.catedrappv2.model.CatedrappApiService
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
open class ApiModule {

    private val BASE_URL = "http://142.93.53.51/catedrapp/v1/"

    @Provides
    fun provideCatedrappApi(): CatedrappAPI {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(CatedrappAPI::class.java)
    }

    @Provides
    open fun providesCatedrappApiService(): CatedrappApiService {
        return CatedrappApiService()
    }
}