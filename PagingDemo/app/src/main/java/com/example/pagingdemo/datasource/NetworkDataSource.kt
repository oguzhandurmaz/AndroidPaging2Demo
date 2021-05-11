package com.example.pagingdemo.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.example.pagingdemo.repo.NewsRepo
import com.example.pagingdemo.network.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NetworkDataSource<T>(private val repo: NewsRepo): PageKeyedDataSource<Int,T>(){

    val state = MutableLiveData<State>()

    private var reTryFunction: (() -> Unit)? = null

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, T>
    ) {
        state.postValue(State.LOADING)
        CoroutineScope(Dispatchers.IO).launch {
            val result = repo.getNews(1,params.requestedLoadSize)
            when(result){
                is Result.Success -> {
                    state.postValue(State.DONE)
                    //With PlaceHolder - PlaceHolder ile
                    //callback.onResult(result.value.articles as List<T>,0,result.value.totalResults,null,2)
                    //Without PlaceHolder - PlaceHoldersız
                    callback.onResult(result.value.articles as List<T>,null,2)
                }
                is Result.Error -> {
                    state.postValue(State.ERROR)
                    //Set Retry function as loadInitial for trigger when press Retry Button on screen
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
            val result = repo.getNews(params.key,params.requestedLoadSize)
            when(result){
                is Result.Success -> {
                    state.postValue(State.DONE)
                    //If there is no more data for load adjacentPAgeKey will be null
                    //Eğer Başka yüklenecek veri kalmadıysa adjacentPageKey null olacak.
                    callback.onResult(result.value.articles as List<T>,params.key + 1)
                }
                is Result.Error -> {
                    state.postValue(State.ERROR)
                    //Set Retry function as loadAfter for trigger when press Retry Button on screen
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
enum class State{
    DONE,LOADING,ERROR
}