package com.example.pagingdemo.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.pagingdemo.repo.NewsRepo

class NetworkDataSourceFactory<T>(private val repo: NewsRepo): DataSource.Factory<Int,T>() {

    val networkDataSource = MutableLiveData<NetworkDataSource<T>>()

    override fun create(): DataSource<Int, T> {
        val temp = NetworkDataSource<T>(repo)
        networkDataSource.postValue(temp)
        return temp
    }
}