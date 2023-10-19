package com.example.newsapp.model

data class News(
    val source: Source,
    val author: String?,
    val title: String,
    val description: String?,
    val url: String,
    val urlToImage: String?,
    var publishedAt: String,
    val content: String?)