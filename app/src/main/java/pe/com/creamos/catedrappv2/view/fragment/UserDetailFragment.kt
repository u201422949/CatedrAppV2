package pe.com.creamos.catedrappv2.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import pe.com.creamos.catedrappv2.R
import pe.com.creamos.catedrappv2.databinding.FragmentUserDetailBinding

/**
 * A simple [Fragment] subclass.
 */
class UserDetailFragment : Fragment() {

    private lateinit var dataBinding: FragmentUserDetailBinding

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
        return dataBinding.root
    }

}
