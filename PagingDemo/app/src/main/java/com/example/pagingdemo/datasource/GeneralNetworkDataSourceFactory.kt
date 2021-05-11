package com.example.pagingdemo.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.pagingdemo.network.Result
import kotlin.reflect.KSuspendFunction2

class GeneralNetworkDataSourceFactory<T>(private val function: suspend (page: Int,pageSize: Int) -> Result<List<T>>
): DataSource.Factory<Int,T>() {

    val generalNetworkDataSource = MutableLiveData<GeneralNetworkDataSource<T>>()
    override fun create(): DataSource<Int, T> {
        val temp = GeneralNetworkDataSource<T>(function)
        generalNetworkDataSource.postValue(temp)
        return temp
    }
}