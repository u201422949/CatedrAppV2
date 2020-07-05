package pe.com.creamos.catedrappv2

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.plugins.RxJavaPlugins
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.*
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import pe.com.creamos.catedrappv2.di.AppModule
import pe.com.creamos.catedrappv2.di.DaggerViewModelComponent
import pe.com.creamos.catedrappv2.model.*
import pe.com.creamos.catedrappv2.util.SharePreferencesHelper
import pe.com.creamos.catedrappv2.util.TypeScore
import pe.com.creamos.catedrappv2.viewmodel.SplashViewModel
import java.util.concurrent.Executor

@ExperimentalCoroutinesApi
class SplashViewModelTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @Mock
    lateinit var apiService: CatedrAppApiService

    @Mock
    lateinit var prefsHelper: SharePreferencesHelper

    private val application = Mockito.mock(Application::class.java)
    private var splashViewModel = SplashViewModel(application, true)
    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        DaggerViewModelComponent.builder()
            .appModule(AppModule(application))
            .apiModule(ApiModuleTest(apiService))
            .prefsModule(PrefsModuleTest(prefsHelper))
            .build()
            .inject(splashViewModel)
    }

    @Before
    fun setUpRxSchedulers() {
        val immediate = object : Scheduler() {
            override fun createWorker(): Worker {
                return ExecutorScheduler.ExecutorWorker(Executor { it.run() }, true)
            }
        }

        RxJavaPlugins.setInitNewThreadSchedulerHandler { immediate }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { immediate }
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun clearDispatchers() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun getDataSuccess() {
        val data = ResponseDataStructure(
            "",
            "1.0",
            listOf(
                ResponseInformation(
                    1,
                    "papa.jpg",
                    "Juan Pablo II",
                    "Descripción acerca de Juan Pablo Segundo"
                )
            ),
            listOf(
                ResponseQuestion(
                    1,
                    "virgen.png",
                    "¿Cómo se le conoce popularmente a Nuestra Señora de la Evangelización?",
                    "Es conocida popularmente como la \"Patrona de facto del Perú\"",
                    2,
                    listOf(
                        ResponseOption(1, "1", "Santa Rosa de Lima"),
                        ResponseOption(2, "2", "Patrona de facto del Perú"),
                        ResponseOption(3, "3", "Virgen Mariana"),
                        ResponseOption(4, "4", "Ninguna de las anteriores")
                    )
                )
            ),
            listOf(
                ResponseChallenge(
                    1,
                    TypeScore.QUESTION_RIGHT.name,
                    50,
                    "Preguntas Correctas",
                    "Responde 1 preguntas correctas durante el recorrido",
                    1,
                    0
                )
            )
        )

        val testSingle = Single.just(data)
        val dataVersion = prefsHelper.getDataVersion()

        Mockito.`when`(apiService.getDataStructure(dataVersion)).thenReturn(testSingle)
        splashViewModel.service()

        Assert.assertEquals(true, splashViewModel.dataOk.value)
        Assert.assertEquals(false, splashViewModel.dataLoadError.value)
        Assert.assertEquals(false, splashViewModel.loading.value)
    }

    @Test
    fun getDataFailure() {
        val dataVersion = prefsHelper.getDataVersion()
        val testSingle = Single.error<ResponseDataStructure>(Throwable())

        Mockito.`when`(apiService.getDataStructure(dataVersion)).thenReturn(testSingle)
        splashViewModel.service()

        Assert.assertEquals(false, splashViewModel.dataOk.value)
        Assert.assertEquals(true, splashViewModel.dataLoadError.value)
        Assert.assertEquals(false, splashViewModel.loading.value)
    }
}