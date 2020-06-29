package pe.com.creamos.catedrappv2.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.launch
import pe.com.creamos.catedrappv2.di.AppModule
import pe.com.creamos.catedrappv2.di.DaggerViewModelComponent
import pe.com.creamos.catedrappv2.model.CatedrAppDatabase
import pe.com.creamos.catedrappv2.model.Challenge

class MenuGoalsViewModel(application: Application) : BaseViewModel(application) {

    constructor(application: Application, test: Boolean = true) : this(application) {
        injected = true;
    }

    private var injected = false

    val goals = MutableLiveData<List<Challenge>>()
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
        fetchChallengeFromDatabase()
    }

    private fun fetchChallengeFromDatabase() {
        loading.value = true
        launch {
            val result = CatedrAppDatabase(getApplication()).catedrappDao().getChallengeList()
            dataRetrieved(result)
        }
    }

    private fun dataRetrieved(challengeList: List<Challenge>) {
        goals.value = challengeList
        infoLoadError.value = false
        loading.value = false
    }

}