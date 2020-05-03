package pe.com.creamos.catedrappv2.di

import dagger.Component
import pe.com.creamos.catedrappv2.model.CatedrappApiService

@Component(modules = [ApiModule::class])
interface ApiComponent {

    fun inject(service: CatedrappApiService)
}