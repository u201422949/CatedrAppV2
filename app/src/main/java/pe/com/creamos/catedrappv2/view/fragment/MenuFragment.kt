package pe.com.creamos.catedrappv2.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_menu.*
import kotlinx.android.synthetic.main.fragment_menu.view.*
import pe.com.creamos.catedrappv2.R.layout.fragment_menu
import pe.com.creamos.catedrappv2.view.adapter.MenuPageAdapter

/**
 * A simple [Fragment] subclass.
 */
class MenuFragment : Fragment(), View.OnClickListener {

    private lateinit var menuPagerAdapter: FragmentPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(fragment_menu, container, false)
        view.closeFab.setOnClickListener(this)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        menuPagerAdapter = MenuPageAdapter(childFragmentManager)
        viewPager.adapter = menuPagerAdapter
        tabLayout.setupWithViewPager(viewPager)
    }

    override fun onClick(view: View?) {
        view?.let {
            if (it == (closeFab)) {
                Navigation.findNavController(it)
                    .navigate(MenuFragmentDirections.actionMenuFragmentToMainFragment())
            }
        }
    }

}
