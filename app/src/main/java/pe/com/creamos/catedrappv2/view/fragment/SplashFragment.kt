package pe.com.creamos.catedrappv2.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_splash.*
import pe.com.creamos.catedrappv2.R
import pe.com.creamos.catedrappv2.R.layout.fragment_splash
import pe.com.creamos.catedrappv2.util.safeLet
import pe.com.creamos.catedrappv2.view.activity.MainActivity
import pe.com.creamos.catedrappv2.viewmodel.SplashViewModel

/**
 * A simple [Fragment] subclass.
 */
class SplashFragment : Fragment() {

    private lateinit var viewModel: SplashViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).supportActionBar?.hide()

        viewModel = ViewModelProviders.of(this).get(SplashViewModel::class.java)
        viewModel.service()
        observeViewModel(view)
    }

    private fun observeViewModel(view: View?) {
        viewModel.dataOk.observe(viewLifecycleOwner, Observer { isOk ->
            safeLet(isOk, view) { ok, v ->
                if (ok)
                    Navigation.findNavController(v)
                        .navigate(SplashFragmentDirections.actionSplashFragmentToLoginFragment())
            }
        })

        viewModel.dataLoadError.observe(viewLifecycleOwner, Observer { isError ->
            isError?.let {
                if (it) Toast.makeText(
                    context,
                    R.string.splash_text_error_service_data,
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

        viewModel.loading.observe(viewLifecycleOwner, Observer { isLoading ->
            isLoading?.let {
                progressSplash.visibility = if (it) View.VISIBLE else View.GONE
            }
        })
    }
}
