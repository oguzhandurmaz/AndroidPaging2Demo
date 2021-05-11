package com.example.pagingdemo.data

import com.example.pagingdemo.data.New

data class NewWrapper(
    val status: String,
    val totalResults: Int,
    val articles: List<New>
)