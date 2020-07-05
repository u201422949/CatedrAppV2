package pe.com.creamos.catedrappv2.di

import dagger.Module
import dagger.Provides
import pe.com.creamos.catedrappv2.model.CatedrAppAPI
import pe.com.creamos.catedrappv2.model.CatedrAppApiService
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
open class ApiModule {

    private val BASE_URL = "http://a8f5137f0651.ngrok.io/api/v1/"

    @Provides
    fun provideCatedrappApi(): CatedrAppAPI {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(CatedrAppAPI::class.java)
    }

    @Provides
    open fun providesCatedrappApiService(): CatedrAppApiService {
        return CatedrAppApiService()
    }
}