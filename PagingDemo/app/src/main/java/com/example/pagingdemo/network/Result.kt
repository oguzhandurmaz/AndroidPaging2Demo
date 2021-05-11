package com.example.pagingdemo.network


sealed class Result<out T>{
    object Loading: Result<Nothing>()
    data class Success<out T>(val value: T): Result<T>()
    data class Error(val message: String?,val exception: Throwable? = null): Result<Nothing>()
}