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
import pe.com.creamos.catedrappv2.model.*
import pe.com.creamos.catedrappv2.util.PREF_INITIAL_DATA_VERSION
import pe.com.creamos.catedrappv2.util.SharePreferencesHelper
import javax.inject.Inject

class SplashViewModel(application: Application) : BaseViewModel(application) {

    constructor(application: Application, test: Boolean) : this(application) {
        injected = test
    }

    @Inject
    lateinit var catedrAppService: CatedrAppApiService

    @Inject
    lateinit var prefHelper: SharePreferencesHelper

    private val disposable = CompositeDisposable()
    private var injected = false
    private var attempts = 0
    val dataOk = MutableLiveData<Boolean>()
    val dataLoadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    private fun inject() {
        if (!injected) {
            DaggerViewModelComponent.builder()
                .appModule(AppModule(getApplication()))
                .build()
                .inject(this)
        }
    }

    fun service() {
        inject()
        fetchFromRemote()
    }

    private fun fetchFromRemote() {
        val dataVersion = prefHelper.getDataVersion()

        loading.value = true

        disposable.add(
            catedrAppService.getDataStructure(dataVersion)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ResponseDataStructure>() {
                    override fun onSuccess(t: ResponseDataStructure) {
                        t.dataVersion?.let { prefHelper.setDataVersion(it) }
                        storeData(t)
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()

                        if ((dataVersion == PREF_INITIAL_DATA_VERSION) && attempts < 3) {
                            attempts++
                            fetchFromRemote()
                        } else {
                            //TODO: Depurar cuando el servicio ya se encuentre funcionando
//                            prefHelper.setDataVersion("1.0")
//                            val responseDataStructure = ResponseDataStructure(
//                                "",
//                                "1.0",
//                                listOf(
//                                    ResponseInformation(
//                                        1,
//                                        "papa.jpg",
//                                        "Juan Pablo II",
//                                        "Descripción acerca de Juan Pablo Segundo"
//                                    )
//                                ),
//                                listOf(
//                                    ResponseQuestion(
//                                        1,
//                                        "virgen.png",
//                                        "¿Cómo se le conoce popularmente a Nuestra Señora de la Evangelización?",
//                                        "Es conocida popularmente como la \"Patrona de facto del Perú\"",
//                                        2,
//                                        listOf(
//                                            ResponseOption(1, "1", "Santa Rosa de Lima"),
//                                            ResponseOption(2, "2", "Patrona de facto del Perú"),
//                                            ResponseOption(3, "3", "Virgen Mariana"),
//                                            ResponseOption(4, "4", "Ninguna de las anteriores")
//                                        )
//                                    )
//                                ),
//                                listOf(
//                                    ResponseChallenge(
//                                        1,
//                                        TypeScore.QUESTION_RIGHT.name,
//                                        50,
//                                        "Preguntas Correctas",
//                                        "Responde 1 preguntas correctas durante el recorrido",
//                                        1,
//                                        0
//                                    )
//                                )
//                            )
//
//                            storeData(responseDataStructure)

                            //TODO: Descomentar cuando el servicio ya se encuentre funcionando
                            dataLoadError.value = true
                            loading.value = false
                        }
                    }
                })
        )
    }

    private fun storeData(response: ResponseDataStructure) {
        launch {
            val dao = CatedrAppDatabase(getApplication()).catedrappDao()

            response.additionalInformationList?.let {
                val infoList = mapAdditionalInformation(it)
                dao.deleteAdditionalInformation()
                dao.insertInformation(*infoList.toTypedArray())
            }

            response.questionList?.let {
                val questionList = mapQuestion(it)
                val optionList = mapOption(it)
                dao.deleteQuestion()
                dao.insertQuestion(*questionList.toTypedArray())
                dao.deleteOption()
                dao.insertOption(*optionList.toTypedArray())
            }

            response.challengeList?.let {
                val challengeList = mapChallenge(it)
                dao.deleteChallenge()
                dao.insertChallenge(*challengeList.toTypedArray())
            }

            dataRetrieved()
        }
    }

    private fun dataRetrieved() {
        dataOk.value = true
        dataLoadError.value = false
        loading.value = false
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}