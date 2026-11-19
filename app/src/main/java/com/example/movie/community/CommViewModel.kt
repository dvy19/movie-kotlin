package com.example.movie.community

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody


sealed class AddPostState{
    object Idle : AddPostState()
    object Loading : AddPostState()
    data class Success(val data: PostResponse) : AddPostState()
    data class Error(val message: String) : AddPostState()
}
class CommViewModel(
    private val repo: CommRepo
) : ViewModel() {


    private val _addPostState = MutableStateFlow<AddPostState>(AddPostState.Idle)
    val addPostState = _addPostState.asStateFlow()

    fun add_post(
        title:String,
        content:String,
        image: MultipartBody.Part?,
        category:String
    ){

        _addPostState.value = AddPostState.Loading

        viewModelScope.launch {
            try {
                val response = repo.addPost(
                    title=title,
                    content=content,
                    image=image,
                    category=category
                )

                if(response.isSuccessful && response.body()!=null) {
                    _addPostState.value= AddPostState.Success(response.body()!!)
                }
                else{
                    _addPostState.value = AddPostState.Error(response.message())
                }
            }

            catch (e: Exception) {
                _addPostState.value = AddPostState.Error(e.message ?: "Unknown error")
            }
        }

        }

    }



