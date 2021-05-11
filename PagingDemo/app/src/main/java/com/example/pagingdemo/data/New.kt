package com.example.pagingdemo.data

import com.google.gson.annotations.SerializedName

data class New(
    val title: String,
    @SerializedName("urlToImage") val image: String,
    val description: String
)