package com.example.pagingdemo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.pagingdemo.data.New
import com.example.pagingdemo.databinding.NewItemBinding

class NewsAdapter: PagedListAdapter<New, NewsAdapter.NewsViewHolder>(
    NewsDiffUtil()
) {


    class NewsViewHolder(private val binding: NewItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(data: New?){

            if (data == null){
                binding.txtTitle.text = "PlaceHolder"
                binding.txtDescription.text = "PlaceHolder"
                return
            }
            binding.txtTitle.text = data.title
            binding.txtDescription.text = data.description

        }
    }

    class NewsDiffUtil: DiffUtil.ItemCallback<New>(){
        override fun areItemsTheSame(oldItem: New, newItem: New): Boolean {
            return newItem.title == oldItem.title
        }

        override fun areContentsTheSame(oldItem: New, newItem: New): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = NewItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}