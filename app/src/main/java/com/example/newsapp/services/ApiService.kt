package com.example.newsapp.services

import com.example.newsapp.model.Response
import com.google.gson.GsonBuilder
import retrofit2.Call

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface ApiService {
    @GET("top-headlines?country=us&apiKey=49ebaba36b4042688be69c36269c593a")
    fun getNews(): Call<Response>
}