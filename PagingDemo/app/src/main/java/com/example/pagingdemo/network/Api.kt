package com.example.pagingdemo.network

import com.example.pagingdemo.data.NewWrapper
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private val gson = GsonBuilder()
    //.setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
    .setLenient()
    .create()

private val retrofit =Retrofit.Builder()
    .baseUrl("https://newsapi.org/v2/")
    .addConverterFactory(GsonConverterFactory.create(gson))
    .build()

object Api{
    val service: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}

interface ApiService{

    @GET("everything?q=sports&apiKey=yourNewsApiKey")
    suspend fun getNewsAsync(@Query("page") page: Int,@Query("pageSize") pageSize: Int): NewWrapper
}