package com.example.pagingdemo

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ConcatAdapter
import com.example.pagingdemo.adapter.ErrorAdapter
import com.example.pagingdemo.adapter.LoadingAdapter
import com.example.pagingdemo.adapter.NewsAdapter
import com.example.pagingdemo.databinding.ActivityMainBinding
import com.example.pagingdemo.datasource.NetworkDataSource
import com.example.pagingdemo.datasource.State
import com.example.pagingdemo.viewmodel.MainViewModel


class MainActivity : AppCompatActivity(), IRecycler{
    override fun onRetry() {
        viewModel.reTry()
        //viewModel.reTryGeneral()
    }

    private val viewModel: MainViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding

    private var isSwipeRefresh = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.swipeRefresh.setOnRefreshListener {
            isSwipeRefresh = true
            viewModel.invalidate()
            //viewModel.invalidateGeneral()
        }

        val adapter = NewsAdapter()
        val loadingAdapter = LoadingAdapter()
        val errorAdapter = ErrorAdapter(this)
        //Concat Adapter for showing Loading and Error end of recyclerview
        val concatAdapter = ConcatAdapter()

        //Observe DataSource State for Show Loading or Error State
        viewModel.getState().observe(this, Observer {
            when(it){
                State.LOADING -> {
                    if(!isSwipeRefresh){
                        concatAdapter.addAdapter(loadingAdapter)
                    }

                }
                State.DONE -> {
                    hideRefreshing()
                    concatAdapter.removeAdapter(loadingAdapter)
                }
                State.ERROR -> {
                    hideRefreshing()
                    concatAdapter.removeAdapter(loadingAdapter)
                    concatAdapter.addAdapter(errorAdapter)
                }
            }
        })

        binding.recyclerViewNews.adapter = concatAdapter

        //Observe First Initial value and set it to NewsAdapter
        viewModel.news.observe(this, Observer {
            Log.d("Main","Veri uzunluğu: ${it.loadedCount} ${it.lastKey}")
            concatAdapter.addAdapter(adapter)
            adapter.submitList(it)
        })


    }

    private fun hideRefreshing(){
        binding.swipeRefresh.isRefreshing = false
        isSwipeRefresh = false
    }
}