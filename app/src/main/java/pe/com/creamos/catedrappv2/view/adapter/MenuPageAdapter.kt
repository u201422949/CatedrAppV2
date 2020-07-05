package pe.com.creamos.catedrappv2.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import pe.com.creamos.catedrappv2.view.fragment.MenuGoalsFragment

class MenuPageAdapter(fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> MenuGoalsFragment()
//            1 -> MenuMapFragment()
//            2 -> MenuGiftFragment()
            else -> Fragment()
        }
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> "Objetivos"
//            1 -> "Mapa"
//            2 -> "Canje"
            else -> ""
        }
    }

    override fun getCount(): Int = 1
}