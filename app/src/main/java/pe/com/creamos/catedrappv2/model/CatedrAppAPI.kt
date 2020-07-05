package pe.com.creamos.catedrappv2.model

import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface CatedrAppAPI {

    @GET("data")
    fun getDataStructure(@Query("dataVersion") dataVersion: Int?): Single<ResponseDataStructure>

    @POST("visitor")
    fun setUserInfo(@Body user: User): Single<ResponseUser>

    @POST("rating")
    fun setUserRating(@Body rating: Rating): Single<ResponseUser>

}