package com.example.pagingdemo.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.pagingdemo.data.New
import com.example.pagingdemo.datasource.GeneralNetworkDataSourceFactory
import com.example.pagingdemo.datasource.NetworkDataSource
import com.example.pagingdemo.datasource.NetworkDataSourceFactory
import com.example.pagingdemo.datasource.State
import com.example.pagingdemo.network.Result.*
import com.example.pagingdemo.repo.NewsRepo

class MainViewModel(application: Application) : AndroidViewModel(application) {

    var news: LiveData<PagedList<New>>

    private val networkDataSourceFactory: NetworkDataSourceFactory<New>

    private val generalNetworkDataSourceFactory: GeneralNetworkDataSourceFactory<New>

    companion object {
        private const val PAGE_SIZE = 25
    }

    init {
        val repo = NewsRepo()
        //Generic Type
        networkDataSourceFactory = NetworkDataSourceFactory<New>(repo)
        val config = PagedList.Config.Builder()
            .setPageSize(PAGE_SIZE)
            .setInitialLoadSizeHint(PAGE_SIZE * 3)
            .setEnablePlaceholders(true)
            .build()
        news = LivePagedListBuilder(networkDataSourceFactory,config).build()

        //General Network DataSource
        //Generic Function
        generalNetworkDataSourceFactory = GeneralNetworkDataSourceFactory<New> { page, pageSize ->
            //Call Http Request and Return Data to GeneralNetworkSource
            val result = repo.getNews(page, pageSize)
            when (result) {
                is Success -> {
                    return@GeneralNetworkDataSourceFactory Success(result.value.articles)
                }
            }
            return@GeneralNetworkDataSourceFactory Error(message = null)
        }

        val configGeneral = PagedList.Config.Builder()
            .setPageSize(PAGE_SIZE)
            .setInitialLoadSizeHint(PAGE_SIZE * 3)
            .setEnablePlaceholders(true)
            .build()
        //UnComment To Use
        //news = LivePagedListBuilder(generalNetworkDataSourceFactory, configGeneral).build()
    }

    fun getState(): LiveData<State> = Transformations.switchMap<NetworkDataSource<New>, State>(
        networkDataSourceFactory.networkDataSource, NetworkDataSource<New>::state
    )

    fun reTry() {
        networkDataSourceFactory.networkDataSource.value?.reTry()
    }

    fun invalidate() {
        networkDataSourceFactory.networkDataSource.value?.invalidate()
    }

    fun getStateGeneral(): LiveData<State> =
        Transformations.switchMap(generalNetworkDataSourceFactory.generalNetworkDataSource) {
            it.state
        }

    fun reTryGeneral() = generalNetworkDataSourceFactory.generalNetworkDataSource.value?.reTry()

    fun invalidateGeneral() =
        generalNetworkDataSourceFactory.generalNetworkDataSource.value?.invalidate()
}