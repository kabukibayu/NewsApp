package com.example.newsapp.views


import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager

import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.denzcoskun.imageslider.models.SlideModel
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemClickListener
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
import java.util.Locale


class HomeActivity : AppCompatActivity() {
    private val BASE_URL = "https://newsapi.org/v2/"
    private val TAG: String = "CHECK_RESPONSE"

    private lateinit var newRecyclerView: RecyclerView
    private lateinit var newArrayList : ArrayList<News>
    private lateinit var tempArrayList: ArrayList<News>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_home)
        val searchNews : EditText = findViewById(R.id.searchNews)
        val text1 : TextView = findViewById(R.id.text)
        val text2 : TextView = findViewById(R.id.text2)
        val imageSlider : ImageSlider = findViewById(R.id.image_slider)
        newRecyclerView = findViewById(R.id.recycleid)
        var layoutPos = newRecyclerView.layoutParams as RelativeLayout.LayoutParams

        searchNews.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                var searchText : String = s.toString()
                Log.i(TAG, searchText)
                Toast.makeText(this@HomeActivity,searchText, Toast.LENGTH_SHORT)
                if(searchText.isNotEmpty()){
                    tempArrayList.clear()
                    newArrayList.forEach{
                        if (it.title.lowercase(Locale.getDefault()).contains(searchText)){
                            Log.i(TAG, it.title.lowercase(Locale.getDefault()))
                            tempArrayList.add(it)
                        }
                    }
                    text1.visibility = View.GONE
                    text2.visibility = View.GONE
                    imageSlider.visibility = View.GONE
                    layoutPos.addRule(RelativeLayout.BELOW, R.id.space_3)
                    newRecyclerView.layoutParams = layoutPos
                    newRecyclerView.adapter!!.notifyDataSetChanged()
                }else{
                    tempArrayList.clear()
                    tempArrayList.addAll(newArrayList)
                    text1.visibility = View.VISIBLE
                    text2.visibility = View.VISIBLE
                    imageSlider.visibility = View.VISIBLE
                    layoutPos.addRule(RelativeLayout.BELOW, R.id.space_5)
                    newRecyclerView.layoutParams = layoutPos
                    newRecyclerView.adapter!!.notifyDataSetChanged()
                }
            }

            override fun afterTextChanged(s: Editable?) {


            }
        })

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

        imageSlider.setImageList(imageList)


        newRecyclerView.layoutManager = LinearLayoutManager(this)
        newRecyclerView.setHasFixedSize(true)

        newArrayList = arrayListOf<News>()
        tempArrayList = arrayListOf<News>()
        lifecycleScope.launch {

            getNewsData()
            try {
                while (newArrayList.size == 0){
                    delay(1000L)
                }

                for (i in 0 .. 4){
                    imageList[i] = SlideModel(
                        newArrayList[i].urlToImage ?: "@drawable/error" ,  // Use the URL from the response if available
                        newArrayList[i].title,
                        ScaleTypes.CENTER_CROP

                        )
                }



                imageSlider.setImageList(imageList)
                imageSlider.setItemClickListener(object : ItemClickListener {
                    override fun doubleClick(position: Int) {
                    }

                    override fun onItemSelected(position: Int) {
                        goToDetailSlider(position)
                    }
                })

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
                        tempArrayList.addAll(newArrayList)
                    }
                }
                override fun onFailure(call: Call<Response>, t: Throwable) {
                    Log.i(TAG, "FAILURE")
                }

            })



            var adapter = NewsAdapter(tempArrayList)
            newRecyclerView.adapter = adapter
            adapter.setOnItemClickerListener(object : NewsAdapter.onItemClickerListener {
                override fun onItemClick(position: Int) {
                    goToDetail(position)
                }
            })
        }catch (e: Exception){
            Log.i(TAG,"${e}")
        }

    }

    fun goToDetail(position : Int){
        val intent = Intent(this@HomeActivity, DetailActivity::class.java)
        intent.putExtra("titleNews",  tempArrayList[position].title )
        intent.putExtra("source",  tempArrayList[position].source.name )
        intent.putExtra("date",  tempArrayList[position].publishedAt )
        intent.putExtra("url_img",  tempArrayList[position].urlToImage )
        intent.putExtra("content",  tempArrayList[position].content )
        intent.putExtra("url_article",  tempArrayList[position].url )
        startActivity(intent)
    }
    fun goToDetailSlider(position : Int){
        val intent = Intent(this@HomeActivity, DetailActivity::class.java)
        intent.putExtra("titleNews",  newArrayList[position].title )
        intent.putExtra("source",  newArrayList[position].source.name )
        intent.putExtra("date",  newArrayList[position].publishedAt )
        intent.putExtra("url_img",  newArrayList[position].urlToImage )
        intent.putExtra("content",  newArrayList[position].content )
        intent.putExtra("url_article",  newArrayList[position].url )
        startActivity(intent)
    }

}