package pe.com.creamos.catedrappv2.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import pe.com.creamos.catedrappv2.di.AppModule
import pe.com.creamos.catedrappv2.di.DaggerViewModelComponent
import pe.com.creamos.catedrappv2.model.CatedrappApiService
import pe.com.creamos.catedrappv2.model.CatedrappDatabase
import pe.com.creamos.catedrappv2.model.ResponseUser
import pe.com.creamos.catedrappv2.model.User
import pe.com.creamos.catedrappv2.util.SharePreferencesHelper
import javax.inject.Inject

class LoginViewModel(application: Application) : BaseViewModel(application) {

    constructor(application: Application, test: Boolean = true) : this(application) {
        injected = true
    }

    @Inject
    lateinit var catedrappService: CatedrappApiService

    @Inject
    lateinit var prefHelper: SharePreferencesHelper

    private val disposable = CompositeDisposable()
    private var injected = false
    val user = MutableLiveData<User>()
    val infoLoadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    private fun inject() {
        if (!injected) {
            DaggerViewModelComponent.builder()
                .appModule(AppModule(getApplication()))
                .build()
                .inject(this)
        }
    }

    fun service(user: User) {
        inject()
        //fetchFromRemote(user)
        storeUser(user)
    }

    private fun fetchFromRemote(user: User) {
        loading.value = true

        disposable.add(
            catedrappService.setUserInfo(user).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ResponseUser>() {
                    override fun onSuccess(userResponse: ResponseUser) {
                        userResponse.visitorId?.let {
                            user.visitorId = userResponse.visitorId
                            storeUser(user)
                        }
                    }

                    override fun onError(e: Throwable) {
                        infoLoadError.value = true
                        loading.value = false
                        e.printStackTrace()
                    }
                })
        )
    }

    private fun storeUser(user: User) {
        launch {
            val dao = CatedrappDatabase(getApplication()).catedrappDao()
            dao.deleteUser()
            dao.insertUser(user)
            userRetrieved(user)
        }
    }

    private fun userRetrieved(userResp: User) {
        user.value = userResp
        infoLoadError.value = false
        loading.value = false
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}