package pe.com.creamos.catedrappv2.model

import io.reactivex.Single
import pe.com.creamos.catedrappv2.di.DaggerApiComponent
import javax.inject.Inject

class CatedrAppApiService {

    @Inject
    lateinit var api: CatedrAppAPI

    init {
        DaggerApiComponent.create().inject(this)
    }

    fun getDataStructure(dataVersion: String?): Single<ResponseDataStructure> {
        return api.getDataStructure(dataVersion)
    }

    fun setUserInfo(user: User): Single<ResponseUser> {
        return api.setUserInfo(user)
    }

    fun setUserRating(rating: Rating): Single<ResponseUser> {
        return api.setUserRating(rating)
    }
}