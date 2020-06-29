package pe.com.creamos.catedrappv2.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_menu_goals.*
import pe.com.creamos.catedrappv2.R
import pe.com.creamos.catedrappv2.view.adapter.GoalsListAdapter
import pe.com.creamos.catedrappv2.viewmodel.MenuGoalsViewModel

/**
 * A simple [Fragment] subclass.
 */
class MenuGoalsFragment : Fragment() {

    @Suppress("PrivatePropertyName")
    private val TAG = MenuGoalsFragment::class.java.simpleName

    private lateinit var viewModel: MenuGoalsViewModel
    private val goalsAdapter = GoalsListAdapter(arrayListOf())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_menu_goals, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(MenuGoalsViewModel::class.java)
        viewModel.refresh()

        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = goalsAdapter
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.goals.observe(viewLifecycleOwner, Observer { challenge ->
            challenge?.let {
                goalsAdapter.updateGoalList(it)
            }
        })

        viewModel.infoLoadError.observe(viewLifecycleOwner, Observer { isError ->
            isError?.let {
                //TODO: Mostrar mensaje error carga de datos y salir de aplicación
            }
        })

        viewModel.loading.observe(viewLifecycleOwner, Observer { isLoading ->
            isLoading?.let {
                //TODO: Levantar loading
                Log.i(TAG, "Loading es: $it")
            }
        })
    }

}
