package pe.com.creamos.catedrappv2.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.launch
import pe.com.creamos.catedrappv2.di.AppModule
import pe.com.creamos.catedrappv2.di.DaggerViewModelComponent
import pe.com.creamos.catedrappv2.model.AdditionalInformation
import pe.com.creamos.catedrappv2.model.CatedrappDatabase
import pe.com.creamos.catedrappv2.model.Challenge
import pe.com.creamos.catedrappv2.model.User
import javax.inject.Inject

class MenuGoalsViewModel(application: Application) : BaseViewModel(application) {

    constructor(application: Application, test: Boolean = true) : this(application) {
        injected = true;
    }

    private var injected = false

    val challenge = MutableLiveData<Challenge>()
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
        fetchChallengeFromDatabase(listOf())
    }

    private fun fetchChallengeFromDatabase(challengeList: List<Challenge>) {
        loading.value = true
        launch {
            val result = CatedrappDatabase(getApplication()).catedrappDao().getChallengeList()
            dataRetrieved(result)
        }
    }

    private fun dataRetrieved(challengeList: List<Challenge>) {
        challenge.value = challengeList[0]
        infoLoadError.value = false
        loading.value = false
    }

}