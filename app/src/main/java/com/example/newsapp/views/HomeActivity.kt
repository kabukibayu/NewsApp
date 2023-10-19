package com.example.newsapp.views


import android.os.Bundle
import android.util.Log
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
import kotlin.math.log


class HomeActivity : AppCompatActivity() {
    private val BASE_URL = "https://newsapi.org/v2/"
    private val TAG: String = "CHECK_RESPONSE"

    private lateinit var newRecyclerView: RecyclerView
    private lateinit var newArrayList : ArrayList<News>
    private lateinit var articleResponse : ArrayList<News>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_home)

        var imageList = ArrayList<SlideModel>()

        for (i in 0 .. 3) {
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

            val newsData = getNewsData()
            while (newsData.size == 0){
                delay(1000L)
            }
            try{
                for (i in 0 until 4) {
                    imageList[i] = SlideModel(
                        newsData[i].urlToImage,  // Use the URL from the response if available
                        newsData[i].title,
                        ScaleTypes.CENTER_CROP
                    )
                }
            }
            catch (e : Exception){
                Log.i(TAG, "${e}")
            }

            imageSlider.setImageList(imageList)
        }


    }

    private suspend fun getNewsData() : List<News> {
        val api = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)

        api.getNews().enqueue(object : Callback<Response>{
            override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {
                response.body()?.let {
                    for (article in it.articles){
                        newArrayList.add(article)
                    }
                }
            }


            override fun onFailure(call: Call<Response>, t: Throwable) {
                Log.i(TAG, "FAILURE")
            }

        })

        newRecyclerView.adapter = NewsAdapter(newArrayList)
        return newArrayList;
    }

}