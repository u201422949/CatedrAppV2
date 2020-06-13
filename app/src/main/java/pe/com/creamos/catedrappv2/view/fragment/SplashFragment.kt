package pe.com.creamos.catedrappv2.view.fragment

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import pe.com.creamos.catedrappv2.R.layout.fragment_splash
import pe.com.creamos.catedrappv2.view.activity.MainActivity

/**
 * A simple [Fragment] subclass.
 */
class SplashFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).supportActionBar?.hide()
        setUpTimerSplash(view)
    }

    private fun setUpTimerSplash(view: View) {
        val handler = Handler()
        handler.postDelayed({
            Navigation.findNavController(view)
                .navigate(SplashFragmentDirections.actionSplashFragmentToLoginFragment())
        }, 3000)
    }
}
