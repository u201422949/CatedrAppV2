package pe.com.creamos.catedrappv2.model

import io.reactivex.Single
import pe.com.creamos.catedrappv2.di.DaggerApiComponent
import javax.inject.Inject

class CatedrappApiService {

    @Inject
    lateinit var api: CatedrappAPI

    init {
        DaggerApiComponent.create().inject(this)
    }

    fun getAdditionalInformationList(): Single<ResponseInformation> {
        return api.getInformationList()
    }

    fun setUserInfo(user: User): Single<ResponseUser> {
        return api.setUserInfo(user)
    }
}