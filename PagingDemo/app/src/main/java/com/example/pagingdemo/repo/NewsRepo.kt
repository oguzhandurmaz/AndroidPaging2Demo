package com.example.pagingdemo.repo

import com.example.pagingdemo.data.NewWrapper
import com.example.pagingdemo.network.Api
import com.example.pagingdemo.network.Result

class NewsRepo {


    suspend fun getNews(page: Int,pageSize: Int): Result<NewWrapper>{
        return try{
            val result = Api.service.getNewsAsync(page,pageSize)
            Result.Success(result)
        }catch (e: Throwable){
            Result.Error(e.message)
        }
    }
}