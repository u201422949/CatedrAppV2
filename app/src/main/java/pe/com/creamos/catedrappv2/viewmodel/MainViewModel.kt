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
import pe.com.creamos.catedrappv2.util.TypeScore
import pe.com.creamos.catedrappv2.util.ZONE_OFFSET
import java.time.LocalDateTime
import java.time.ZoneOffset
import javax.inject.Inject

class MainViewModel(application: Application) : BaseViewModel(application) {

    constructor(application: Application, test: Boolean = true) : this(application) {
        injected = true
    }

    @Inject
    lateinit var catedrAppService: CatedrAppApiService

    private val disposable = CompositeDisposable()
    private var injected = false

    val information = MutableLiveData<MutableList<AdditionalInformation>>()
    val question = MutableLiveData<List<QuestionAndOptions>>()
    val user = MutableLiveData<UserAndScore>()
    val completedGoal = MutableLiveData<Challenge>()
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
        fetchDataFromDatabase()
    }

    private fun fetchDataFromDatabase() {
        loading.value = true
        launch {
            val dao = CatedrAppDatabase(getApplication()).catedrappDao()
            val infoResult = dao.getInformationList()
            val questionResult = dao.getQuestionList()
            val userResult = dao.getUser()
            dataRetrieved(infoResult, questionResult, userResult)
        }
    }

    fun updateElementRead(element: Any, typeScore: TypeScore) {
        loading.value = true

        when (element) {
            is AdditionalInformation -> updateInfoRead(element)
            is Question -> updateQuestionRead(element, typeScore)
        }
    }

    private fun updateInfoRead(info: AdditionalInformation) {
        launch {
            info.wasRead = true
            info.updateDate = LocalDateTime.now(ZoneOffset.of(ZONE_OFFSET))

            CatedrAppDatabase(getApplication()).catedrappDao().updateInformation(info)
            loading.value = false
        }
    }

    private fun updateQuestionRead(question: Question, typeScore: TypeScore) {
        launch {
            if (typeScore == TypeScore.QUESTION)
                question.questionWasRead = true
            else
                question.answerWasRead = true

            question.updateDate = LocalDateTime.now(ZoneOffset.of(ZONE_OFFSET))
            CatedrAppDatabase(getApplication()).catedrappDao().updateQuestion(question)
            loading.value = false
        }
    }

    private fun dataRetrieved(
        infoRetrieveList: List<AdditionalInformation>,
        questionRetrieveList: List<QuestionAndOptions>,
        userRetrieve: UserAndScore
    ) {
        user.value = userRetrieve
        information.value = infoRetrieveList.toMutableList()
        question.value = questionRetrieveList
        infoLoadError.value = false
        loading.value = false
        completedGoal.value = null
    }


    fun updateScoreAndChallenge(typeScore: TypeScore, nullGoalsViewModel: Boolean) {
        loading.value = true
        launch {
            val dao = CatedrAppDatabase(getApplication()).catedrappDao()
            val challenge = dao.getChallengeList(typeScore.name)
            val userAndScore = user.value

            userAndScore?.score?.let {
                it.scoreValue = it.scoreValue!!.plus(typeScore.points)
                val modValue = it.scoreValue!! % 100
                it.level = ((it.scoreValue!! - modValue) / 100) + 1
                dao.updateScore(it)
            }

            challenge?.let {
                it.progress = it.progress?.plus(1)
                it.completed = it.total == it.progress
                dao.updateChallenge(it)

                if (it.completed!!)
                    completedGoal.value = it
            }

            if (nullGoalsViewModel)
                completedGoal.value = null

            user.value = userAndScore
            loading.value = false
        }
    }

    fun saveRatingOnRemote(rated: Int, message: String) {
        loading.value = true

        val ratedDate = LocalDateTime.now(ZoneOffset.of(ZONE_OFFSET))

        user.value?.user?.visitorId?.let {
            disposable.add(
                catedrAppService.setUserRating(Rating(it, rated, message, ratedDate))
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : DisposableSingleObserver<ResponseUser>() {
                        override fun onSuccess(t: ResponseUser) {
                            // TODO: Validar si se implementará en base de datos
                        }

                        override fun onError(e: Throwable) {
                            e.printStackTrace()
                        }
                    })
            )
        }
    }

    fun saveLog() {
        launch {
            CatedrAppDatabase(getApplication()).catedrappDao().insertLog(
                ActionLog(
                    1, "Sharing content", LocalDateTime.now(
                        ZoneOffset.of(
                            ZONE_OFFSET
                        )
                    )
                )
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}