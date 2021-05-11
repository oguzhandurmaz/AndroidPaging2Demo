package com.example.pagingdemo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pagingdemo.IRecycler
import com.example.pagingdemo.databinding.ErrorItemBinding

class ErrorAdapter(private val callback: IRecycler): RecyclerView.Adapter<ErrorAdapter.ErrorViewHolder>() {

    inner class ErrorViewHolder(private val binding: ErrorItemBinding): RecyclerView.ViewHolder(binding.root){

        private val callback = this@ErrorAdapter.callback

        fun bind(){
            binding.btnReTry.setOnClickListener {
                callback.onRetry()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ErrorViewHolder {
        val binding = ErrorItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ErrorViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return 1
    }

    override fun onBindViewHolder(holder: ErrorViewHolder, position: Int) {
        holder.bind()
    }
}