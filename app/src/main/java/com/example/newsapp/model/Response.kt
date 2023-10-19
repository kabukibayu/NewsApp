package com.example.newsapp.model

data class Response(
    val status: String,
    val totalResults: Int,
    val articles: List<News>
)