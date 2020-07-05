package pe.com.creamos.catedrappv2

import pe.com.creamos.catedrappv2.di.ApiModule
import pe.com.creamos.catedrappv2.model.CatedrAppApiService

class ApiModuleTest(private val mockService: CatedrAppApiService) : ApiModule() {

    override fun providesCatedrappApiService(): CatedrAppApiService {
        return mockService
    }
}