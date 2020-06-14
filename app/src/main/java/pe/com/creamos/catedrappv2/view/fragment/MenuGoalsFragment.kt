package pe.com.creamos.catedrappv2.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import pe.com.creamos.catedrappv2.R.layout.fragment_menu_goals
import pe.com.creamos.catedrappv2.databinding.FragmentMenuGoalsBinding
import pe.com.creamos.catedrappv2.viewmodel.MenuGoalsViewModel

/**
 * A simple [Fragment] subclass.
 */
class MenuGoalsFragment : Fragment() {

    private val TAG: String = MenuGoalsFragment::class.java.simpleName

    private lateinit var viewModel: MenuGoalsViewModel
    private lateinit var dataBinding: FragmentMenuGoalsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = DataBindingUtil.inflate(
            inflater,
            fragment_menu_goals,
            container,
            false
        )
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(MenuGoalsViewModel::class.java)
        viewModel.refresh()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.challenge.observe(viewLifecycleOwner, Observer { challenge ->
            challenge?.let {
                dataBinding.challenge = it
            }
        })
    }

}
