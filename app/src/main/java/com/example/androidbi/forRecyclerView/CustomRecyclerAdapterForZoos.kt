package com.example.androidbi.forRecyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.androidbi.R

class CustomRecyclerAdapterForExams(private val namesE: List<String>,
                                    private val numbers: List<Int>,
                                    private val maxScores: List<Int>):
    RecyclerView.Adapter<CustomRecyclerAdapterForExams.MyViewHolder>()
{
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        val layoutItem: ConstraintLayout = itemView.findViewById(R.id.layoutItem)
        val textViewName: TextView = itemView.findViewById(R.id.textViewName)
        val textViewAddress: TextView = itemView.findViewById(R.id.textViewAddress)
        val textViewNum: TextView = itemView.findViewById(R.id.textViewNum)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder
    {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int)
    {
        holder.textViewName.text = namesE[position]
        holder.textViewAddress.text = numbers[position].toString()
        holder.textViewNum.text = maxScores[position].toString()
    }

    override fun getItemCount() = namesE.size
}