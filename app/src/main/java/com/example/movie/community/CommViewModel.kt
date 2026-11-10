package com.example.movie.community

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


sealed class AddPostState{
    object Idle : AddPostState()
    object Loading : AddPostState()
    data class Success(val data: PostResponse) : AddPostState()
    data class Error(val message: String) : AddPostState()
}
class CommViewModel : ViewModel() {

    private val repo = CommRepo()

    private val _addPostState = MutableStateFlow<AddPostState>(AddPostState.Idle)
    val addPostState = _addPostState.asStateFlow()

    fun add_post(req: PostRequest){

        _addPostState.value = AddPostState.Loading

        viewModelScope.launch {
            try {
                val response = repo.addPost(req)

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



}