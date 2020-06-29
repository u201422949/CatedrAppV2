package pe.com.creamos.catedrappv2.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import pe.com.creamos.catedrappv2.R
import pe.com.creamos.catedrappv2.databinding.FragmentMenuGoalsItemBinding
import pe.com.creamos.catedrappv2.model.Challenge

class GoalsListAdapter(private val goalList: ArrayList<Challenge>) :
    RecyclerView.Adapter<GoalsListAdapter.GoalViewHolder>() {

    fun updateGoalList(newDogsList: List<Challenge>) {
        goalList.clear()
        goalList.addAll(newDogsList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view =
            DataBindingUtil.inflate<FragmentMenuGoalsItemBinding>(
                inflater,
                R.layout.fragment_menu_goals_item,
                parent,
                false
            )
        return GoalViewHolder(view)
    }

    override fun getItemCount() = goalList.size

    override fun onBindViewHolder(holder: GoalViewHolder, position: Int) {
        holder.view.challenge = goalList[position]
    }

    class GoalViewHolder(var view: FragmentMenuGoalsItemBinding) :
        RecyclerView.ViewHolder(view.root)

}