package com.example.newsapp.views

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.newsapp.R
import com.google.android.material.imageview.ShapeableImageView

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val titleNews : TextView = findViewById(R.id.title)
        val sourceNews : TextView = findViewById(R.id.source)
        val dateNews : TextView = findViewById(R.id.date)
        val titleImage : ShapeableImageView = findViewById(R.id.title_image)
        val content : TextView = findViewById(R.id.content)
        val link : TextView = findViewById(R.id.link)

        val bundle : Bundle? = intent.extras

        titleNews.text = bundle?.getString("titleNews")
        sourceNews.text = bundle?.getString("source")
        dateNews.text = bundle?.getString("date")
        content.text = bundle?.getString("content")

        Glide.with(titleImage)
            .load(bundle?.getString("url_img")) // Replace with your image URL
            .placeholder(com.denzcoskun.imageslider.R.drawable.default_loading) // Placeholder image resource
            .error(com.denzcoskun.imageslider.R.drawable.default_error) // Error image resource
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(titleImage)

        link.setOnClickListener {
            val url = bundle?.getString("url_article") // Replace with your URL
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }
    }
}