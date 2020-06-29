package pe.com.creamos.catedrappv2.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import kotlinx.android.synthetic.main.fragment_user_detail.*
import kotlinx.android.synthetic.main.fragment_user_detail.view.*
import pe.com.creamos.catedrappv2.R
import pe.com.creamos.catedrappv2.databinding.FragmentUserDetailBinding

/**
 * A simple [Fragment] subclass.
 */
class UserDetailFragment : Fragment(), View.OnClickListener {

    private lateinit var dataBinding: FragmentUserDetailBinding
    private val args: UserDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_user_detail,
            container,
            false
        )

        val view = dataBinding.root
        view.closeFab.setOnClickListener(this)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        dataBinding.user = args.userArgs
        dataBinding.score = args.scoreArgs
    }

    override fun onClick(view: View?) {
        view?.let {
            if (it == (closeFab)) {
                Navigation.findNavController(it)
                    .navigate(UserDetailFragmentDirections.actionUserDetailFragmentToMainFragment())
            }
        }
    }
}
