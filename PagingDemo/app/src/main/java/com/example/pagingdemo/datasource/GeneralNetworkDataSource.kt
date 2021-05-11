package com.example.pagingdemo.datasource

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.example.pagingdemo.network.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GeneralNetworkDataSource<T>(private val function: suspend (page: Int,pageSize: Int) -> Result<List<T>>
): PageKeyedDataSource<Int, T>() {

    val state = MutableLiveData<State>()

    private var reTryFunction: (() -> Unit)? = null

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, T>
    ) {
        state.postValue(State.LOADING)
        CoroutineScope(Dispatchers.IO).launch {
            val result = function(1, params.requestedLoadSize)
                when (result) {
                    is Result.Success -> {
                        state.postValue(State.DONE)
                        //PlaceHolder ile
                        //callback.onResult(result.value.articles as List<T>,0,result.value.totalResults,null,2)
                        //PlaceHoldersız
                        callback.onResult(result.value, null, 2)
                    }
                    is Result.Error -> {
                        state.postValue(State.ERROR)
                        //Set Retry Function
                        setRetry {
                            loadInitial(params,callback)
                        }
                    }
            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, T>) {

        state.postValue(State.LOADING)
        CoroutineScope(Dispatchers.IO).launch {
            val result = function(params.key, params.requestedLoadSize)
            when (result) {
                is Result.Success -> {
                    state.postValue(State.DONE)
                    //Eğer Başka yüklenecek veri kalmadıysa adjacentPageKey null olacak.
                    callback.onResult(result.value as List<T>, params.key + 1)
                }
                is Result.Error -> {
                    state.postValue(State.ERROR)
                    //Set Retry Function
                    setRetry {
                        loadAfter(params,callback)
                    }
                }
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, T>) {

    }

    private fun setRetry(func: (() -> Unit)?){
        reTryFunction = func
    }

    fun reTry(){
        reTryFunction?.invoke()
    }
}