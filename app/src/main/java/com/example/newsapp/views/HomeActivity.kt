package com.example.newsapp.views


import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
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
    lateinit var imageId :  Array<String>
    lateinit var heading : Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_home)

        val imageList = ArrayList<SlideModel>()

        imageList.add(SlideModel("https://image.cnbcfm.com/api/v1/image/107318650-16975648322023-10-17t174449z_1027750339_rc2hu3akstu3_rtrmadp_0_usa-congress-speaker.jpeg?v=1697627401&w=1920&h=1080", "5 things to know before the stock market opens Wednesday - CNBC", ScaleTypes.CENTER_CROP))
        imageList.add(SlideModel("https://image.cnbcfm.com/api/v1/image/107067836-16535928912022-05-26t191907z_565636100_rc26fu90vqsg_rtrmadp_0_morganstanley-agm.jpeg?v=1697632575&w=1920&h=1080", "Morgan Stanley beats estimates on trading, but shares dip as wealth management disappoints - CNBC", ScaleTypes.CENTER_CROP))
        imageList.add(SlideModel("https://res.cloudinary.com/graham-media-group/image/upload/f_auto/q_auto/c_thumb,w_700/v1/media/gmg/RQ6XHISJ5NHFPLYBJT6JP7HG3E.jpg?_a=ATAPphC0", "19 Michigan Rite Aid stores to close after bankruptcy filing; 9 in Metro Detroit - WDIV ClickOnDetroit", ScaleTypes.CENTER_CROP))

        val imageSlider = findViewById<ImageSlider>(R.id.image_slider)
        imageSlider.setImageList(imageList)

        imageId = arrayOf("https://image.cnbcfm.com/api/v1/image/107318650-16975648322023-10-17t174449z_1027750339_rc2hu3akstu3_rtrmadp_0_usa-congress-speaker.jpeg?v=1697627401&w=1920&h=1080",
            "https://image.cnbcfm.com/api/v1/image/107067836-16535928912022-05-26t191907z_565636100_rc26fu90vqsg_rtrmadp_0_morganstanley-agm.jpeg?v=1697632575&w=1920&h=1080",
            "https://res.cloudinary.com/graham-media-group/image/upload/f_auto/q_auto/c_thumb,w_700/v1/media/gmg/RQ6XHISJ5NHFPLYBJT6JP7HG3E.jpg?_a=ATAPphC0")

        heading = arrayOf("5 things to know before the stock market opens Wednesday - CNBC", "Morgan Stanley beats estimates on trading, but shares dip as wealth management disappoints - CNBC", "19 Michigan Rite Aid stores to close after bankruptcy filing; 9 in Metro Detroit - WDIV ClickOnDetroit")

        newRecyclerView = findViewById(R.id.recycleid)
        newRecyclerView.layoutManager = LinearLayoutManager(this)
        newRecyclerView.setHasFixedSize(true)

        newArrayList = arrayListOf<News>()


        getNewsData()


    }

    private fun getNewsData(){
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

    }

}