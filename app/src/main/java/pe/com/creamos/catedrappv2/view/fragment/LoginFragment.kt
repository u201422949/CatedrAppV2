package pe.com.creamos.catedrappv2.view.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_login.*
import pe.com.creamos.catedrappv2.R
import pe.com.creamos.catedrappv2.R.layout.fragment_login
import pe.com.creamos.catedrappv2.model.Challenge
import pe.com.creamos.catedrappv2.model.Score
import pe.com.creamos.catedrappv2.model.User
import pe.com.creamos.catedrappv2.util.validateMail
import pe.com.creamos.catedrappv2.view.activity.MainActivity
import pe.com.creamos.catedrappv2.viewmodel.LoginViewModel

/**
 * A simple [Fragment] subclass.
 */
class LoginFragment : Fragment(), OnClickListener, TextWatcher {

    private lateinit var viewModel: LoginViewModel
    private lateinit var textMail: String
    private lateinit var textNickname: String
    private lateinit var textAge: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).supportActionBar?.hide()

        btnLogin.setOnClickListener(this)
        edtMail.addTextChangedListener(this)
        edtNickname.addTextChangedListener(this)
        edtAge.addTextChangedListener(this)

        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)

    }

    override fun onClick(v: View?) {
        if (!validateMail(textMail)) {
            Toast.makeText(context, R.string.login_text_error_mail, Toast.LENGTH_LONG).show()
            return
        }

        val challenge = Challenge(
            "Preguntas Correctas",
            "Responde 2 preguntas correctas durante el recorrido",
            2,
            0
        )

        viewModel.service(User(1, textMail, textNickname, textAge.toInt(), Score(10, "10", 1)), challenge)
        observeViewModel(v)
    }

    override fun afterTextChanged(s: Editable?) {
        textMail = edtMail.text.toString()
        textNickname = edtNickname.text.toString()
        textAge = edtAge.text.toString()

        btnLogin.isEnabled = !(textMail.isEmpty() || textNickname.isEmpty() || textAge.isEmpty())
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

    private fun observeViewModel(view: View?) {
        viewModel.user.observe(viewLifecycleOwner, Observer { user ->
            user?.let {
                view?.let {
                    Navigation.findNavController(it)
                        .navigate(LoginFragmentDirections.actionLoginFragmentToMainFragment())
                }
            }
        })

        viewModel.infoLoadError.observe(viewLifecycleOwner, Observer { isError ->
            isError?.let {
                if (it) Toast.makeText(
                    context,
                    R.string.login_text_error_service_user,
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

        viewModel.loading.observe(viewLifecycleOwner, Observer { isLoading ->
            isLoading?.let {
                linearProgressLogin.visibility = if (it) View.VISIBLE else View.GONE
            }
        })
    }
}
