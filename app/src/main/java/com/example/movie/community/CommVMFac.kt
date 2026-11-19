package com.example.movie.community


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class CommVMFac(
    private val repository: CommRepo
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CommViewModel::class.java)) {
            return CommViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}