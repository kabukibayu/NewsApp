package com.example.newsapp.views


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager

import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.denzcoskun.imageslider.models.SlideModel
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.example.newsapp.adapter.NewsAdapter

import com.example.newsapp.model.News
import com.example.newsapp.model.Response
import com.example.newsapp.services.ApiService
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.newsapp.util.Utility


class HomeActivity : AppCompatActivity() {
    private val BASE_URL = "https://newsapi.org/v2/"
    private val TAG: String = "CHECK_RESPONSE"

    private lateinit var newRecyclerView: RecyclerView
    private lateinit var newArrayList : ArrayList<News>
    lateinit var news : ArrayList<News>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_home)

        var imageList = ArrayList<SlideModel>()

        for (i in 0 .. 4) {
            imageList.add(
                SlideModel(
                    "https://matrixread.com/wp-content/uploads/2020/10/loading-placeholder.png.webp",
                    "",
                    ScaleTypes.CENTER_CROP
                )
            )
        }
        val imageSlider = findViewById<ImageSlider>(R.id.image_slider)
        imageSlider.setImageList(imageList)

        newRecyclerView = findViewById(R.id.recycleid)
        newRecyclerView.layoutManager = LinearLayoutManager(this)
        newRecyclerView.setHasFixedSize(true)

        newArrayList = arrayListOf<News>()
        lifecycleScope.launch {

            getNewsData()
            try {
                while (newArrayList.size == 0){
                    delay(1000L)
                }

                //lewatin gambar yg null
                var x = 0
                var i = 0
                while (x < 4){
                    if (newArrayList[i+x].urlToImage != null){
                    imageList[i] = SlideModel(
                        newArrayList[i+x].urlToImage,  // Use the URL from the response if available
                        newArrayList[i+x].title,
                        ScaleTypes.CENTER_CROP
                    )
                        x += 1
                    }else{
                        i += 1
                    }

                }


                imageSlider.setImageList(imageList)
            }catch (e : Exception){
                Log.i(TAG, "${e}")
            }

        }


    }

    private suspend fun getNewsData() {
        try {
            val api = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)

            api.getNews().enqueue(object : Callback<Response> {
                override fun onResponse(
                    call: Call<Response>,
                    response: retrofit2.Response<Response>
                ) {
                    response.body()?.let {
                        val util = Utility()
                        for (article in it.articles) {
                            var tempArticle = article;
                            tempArticle.publishedAt = util.convretTime(tempArticle.publishedAt)
                            newArrayList.add(article)
                        }
                    }
                }


                override fun onFailure(call: Call<Response>, t: Throwable) {
                    Log.i(TAG, "FAILURE")
                }

            })
            var adapter = NewsAdapter(newArrayList)
            newRecyclerView.adapter = adapter
            adapter.setOnItemClickerListener(object : NewsAdapter.onItemClickerListener {
                override fun onItemClick(position: Int) {
                    Toast.makeText(
                        this@HomeActivity,
                        "${newArrayList[position].title}",
                        Toast.LENGTH_SHORT
                    ).show()
                val intent = Intent(this@HomeActivity, DetailActivity::class.java)
                intent.putExtra("titleNews",  newArrayList[position].title )
                intent.putExtra("source",  newArrayList[position].source.name )
                intent.putExtra("date",  newArrayList[position].publishedAt )
                intent.putExtra("url_img",  newArrayList[position].urlToImage )
                intent.putExtra("content",  newArrayList[position].content )
                intent.putExtra("url_article",  newArrayList[position].url )
                startActivity(intent)
                }
            })
        }catch (e: Exception){
            Log.i(TAG,"${e}")
        }

    }

}