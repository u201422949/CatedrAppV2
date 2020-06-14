package pe.com.creamos.catedrappv2.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import pe.com.creamos.catedrappv2.di.AppModule
import pe.com.creamos.catedrappv2.di.DaggerViewModelComponent
import pe.com.creamos.catedrappv2.model.*
import pe.com.creamos.catedrappv2.util.SharePreferencesHelper
import javax.inject.Inject

class MainViewModel(application: Application) : BaseViewModel(application) {

    constructor(application: Application, test: Boolean = true) : this(application) {
        injected = true
    }

    @Inject
    lateinit var catedrappService: CatedrappApiService

    @Inject
    lateinit var prefHelper: SharePreferencesHelper

    private val disposable = CompositeDisposable()
    private var injected = false
    private var versionData = false

    val information = MutableLiveData<List<AdditionalInformation>>()
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

    fun refresh() {
        inject()

       // if (versionData) fetchInfoFromDatabase() else fetchInfoFromRemote()

        fetchUserFromDatabase(listOf())
    }

    private fun fetchInfoFromRemote() {
        loading.value = true
        disposable.add(
            catedrappService.getAdditionalInformationList()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ResponseInformation>() {
                    override fun onSuccess(infoList: ResponseInformation) {
                        infoList.additionalInformationList?.let {
                            prefHelper.setInfoVersion("1.0")
                            fetchUserFromDatabase(it)
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

    private fun fetchInfoFromDatabase() {
        loading.value = true
        launch {
            val result = CatedrappDatabase(getApplication()).catedrappDao().getInformationList()
            Toast.makeText(getApplication(), "RETRIEVE FROM DATABASE", Toast.LENGTH_SHORT).show()
            fetchUserFromDatabase(result)
        }
    }

    private fun fetchUserFromDatabase(infoList: List<AdditionalInformation>) {
        loading.value = true
        launch {
            val result = CatedrappDatabase(getApplication()).catedrappDao().getUser()
            dataRetrieved(infoList, result)
        }
    }

    private fun dataRetrieved(infoList: List<AdditionalInformation>, userRetrieve: User) {
        user.value = userRetrieve
        information.value = infoList
        infoLoadError.value = false
        loading.value = false
    }

    fun updateChallengeToDatabase() {
        loading.value = true
        launch {
            val result = CatedrappDatabase(getApplication()).catedrappDao().updateChallenge()
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}