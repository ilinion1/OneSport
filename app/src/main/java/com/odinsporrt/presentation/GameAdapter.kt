package com.odinsporrt.presentation

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.odinsporrt.R

class GameAdapter(private val context: Context): RecyclerView.Adapter<GameAdapter.RecordViewHolder>() {

    var betList = arrayListOf<String>()
    var resultList = arrayListOf<String>()
    var countList = arrayListOf<String>()

    class RecordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val count = itemView.findViewById<TextView>(R.id.tvCount)
        val bet = itemView.findViewById<TextView>(R.id.tvBet)
        val result = itemView.findViewById<TextView>(R.id.tvResult)
        val imCoin = itemView.findViewById<ImageView>(R.id.imCoin)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.game_item, parent, false)
        return RecordViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecordViewHolder, position: Int) {
        holder.count.text = countList[position]
        holder.bet.text = betList[position]
        holder.result.text = resultList[position]
        Glide.with(context).load(R.drawable.coin).into(holder.imCoin)
    }

    override fun getItemCount(): Int {
        return resultList.size
    }
}