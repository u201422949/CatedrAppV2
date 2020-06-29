package pe.com.creamos.catedrappv2.di

import dagger.Component
import pe.com.creamos.catedrappv2.viewmodel.LoginViewModel
import pe.com.creamos.catedrappv2.viewmodel.MainViewModel
import pe.com.creamos.catedrappv2.viewmodel.MenuGoalsViewModel
import pe.com.creamos.catedrappv2.viewmodel.SplashViewModel
import javax.inject.Singleton

@Singleton
@Component(modules = [ApiModule::class, AppModule::class, PrefsModule::class])
interface ViewModelComponent {

    fun inject(splashViewModel: SplashViewModel)
    fun inject(loginViewModel: LoginViewModel)
    fun inject(mainViewModel: MainViewModel)
    fun inject(menuGoalsViewModel: MenuGoalsViewModel)
}