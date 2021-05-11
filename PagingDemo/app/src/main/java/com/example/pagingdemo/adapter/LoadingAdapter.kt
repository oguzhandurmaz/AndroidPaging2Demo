package com.example.pagingdemo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pagingdemo.databinding.LoadingItemBinding

class LoadingAdapter: RecyclerView.Adapter<LoadingAdapter.LoadingViewHolder>() {

    class LoadingViewHolder(private val binding: LoadingItemBinding): RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoadingViewHolder {
        val binding = LoadingItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return LoadingViewHolder(
            binding
        )
    }

    override fun getItemCount(): Int {
        return 1
    }

    override fun onBindViewHolder(holder: LoadingViewHolder, position: Int) {

    }
}