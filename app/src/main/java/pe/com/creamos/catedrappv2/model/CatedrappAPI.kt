package pe.com.creamos.catedrappv2.model

import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface CatedrappAPI {

    @GET("information")
    fun getInformationList(): Single<ResponseInformation>

    @POST("visitor")
    fun setUserInfo(@Body user: User): Single<ResponseUser>
    abstract fun setUserInfo(): Single<ResponseUser>

}