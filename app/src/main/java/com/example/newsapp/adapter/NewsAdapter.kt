package com.example.newsapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.newsapp.R
import com.example.newsapp.model.News
import com.google.android.material.imageview.ShapeableImageView

class NewsAdapter(private val newsList :ArrayList<News>) :
    RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_news, parent,false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = newsList[position]
        val titleImage = holder.titleImage

        Glide.with(holder.itemView)
            .load(currentItem.urlToImage) // Replace with your image URL
            .placeholder(com.denzcoskun.imageslider.R.drawable.default_loading) // Placeholder image resource
            .error(com.denzcoskun.imageslider.R.drawable.default_error) // Error image resource
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(titleImage)

        holder.newsHeading.text = currentItem.title
    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val titleImage : ShapeableImageView = itemView.findViewById(R.id.title_image)
        val newsHeading : TextView = itemView.findViewById(R.id.newsheading)


    }
}