package pe.com.creamos.catedrappv2.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentPagerAdapter
import kotlinx.android.synthetic.main.fragment_menu.*

import pe.com.creamos.catedrappv2.R
import pe.com.creamos.catedrappv2.view.adapter.MenuPageAdapter

/**
 * A simple [Fragment] subclass.
 */
class MenuFragment : Fragment() {

    private lateinit var menuPagerAdapter: FragmentPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        menuPagerAdapter = MenuPageAdapter(childFragmentManager)
        viewPager.adapter = menuPagerAdapter
        tabLayout.setupWithViewPager(viewPager)
    }

}
