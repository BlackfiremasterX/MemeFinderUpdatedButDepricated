package com.arbonik.project

import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class RecAdapter(context: Context?, width: Double, height: Double, urls: ArrayList<Meme>) :
    RecyclerView.Adapter<RecAdapter.MyViewHolder>() {
    var width = width
    var height = height
    var context = context
    var urls = urls
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item, parent, false)
        return MyViewHolder(itemView)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val meme = urls[position]
        val url = meme.url

        holder.image1?.layoutParams?.width = width.toInt()
        holder.image1?.layoutParams?.height = height.toInt()
        holder.image1?.let { MainActivity().parse(it, url) }
        holder.meme_text?.text = meme.title!!.split(",")[0]
        try {
            val favourite_memes = JSONHelper.importFromJSON(context)
            if (favourite_memes?.contains(meme)!!) {
                holder.favouritesButton?.setImageResource(R.drawable.ic_baseline_star_24)
            } else {
                holder.favouritesButton?.setImageResource(R.drawable.ic_baseline_star_outline_24)
            }
        } catch (e: Exception) {
            holder.favouritesButton?.setImageResource(R.drawable.ic_baseline_star_outline_24)
        }
        holder.itemView.setOnClickListener {
            val intent = Intent(context, MemeActivity::class.java)
            intent.putExtra("id", meme.id)
            intent.putExtra("title", meme.title)
            intent.putExtra("description", meme.description)
            intent.putExtra("url", meme.url)
            context?.startActivity(intent)
        }
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image1: ImageView? = null
        var card_left: CardView? = null
        var meme_text: TextView? = null
        var favouritesButton: ImageButton? = null
        init {
            image1 = itemView.findViewById(R.id.image)
            card_left = itemView.findViewById(R.id.card_left)
            meme_text = itemView.findViewById(R.id.meme_text)
            favouritesButton = itemView.findViewById(R.id.izbrannoe_recycler)
        }
    }

    override fun getItemCount(): Int {
        return urls.size
    }
}

