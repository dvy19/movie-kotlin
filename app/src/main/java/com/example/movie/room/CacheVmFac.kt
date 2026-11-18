package com.example.movie.room



import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class CacheVmFac(
    private val repository: CacheRepo
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CacheViewModel::class.java)) {
            return CacheViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}