package com.example.movie.community

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movie.movie.PopularMovie
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

sealed class GetAllPostsState{
    object Idle : GetAllPostsState()
    object Loading : GetAllPostsState()
    data class Success(val data: PostResponse) : GetAllPostsState()
    data class Error(val message: String) : GetAllPostsState()
}

sealed class GetMyPostsState{
    object Idle : GetMyPostsState()
    object Loading : GetMyPostsState()
    data class Success(val data: List<Post>) : GetMyPostsState()
    data class Error(val message: String) : GetMyPostsState()
}

sealed class GetAllSurveysState {
    object Idle : GetAllSurveysState()
    object Loading : GetAllSurveysState()
    data class Success(val data: AllSurveyResponse) : GetAllSurveysState()
    data class Error(val message: String) : GetAllSurveysState()
}


sealed class UserSurveyResponseState{
    object Idle : UserSurveyResponseState()
    object Loading : UserSurveyResponseState()
    data class Success(val data: List<UserSurveyResponse>) : UserSurveyResponseState()
    data class Error(val message: String) : UserSurveyResponseState()
}


class CommViewModel(
    private val repo: CommRepo
) : ViewModel() {


    private val _addPostState = MutableStateFlow<AddPostState>(AddPostState.Idle)
    val addPostState = _addPostState.asStateFlow()

    private val _getAllPostsState = MutableStateFlow<GetAllPostsState>(GetAllPostsState.Idle)
    val getAllPostsState = _getAllPostsState.asStateFlow()

    private val _getMyPostsState = MutableStateFlow<GetMyPostsState>(GetMyPostsState.Idle)
    val getMyPostsState = _getMyPostsState.asStateFlow()


    private val _getAllSurveysState = MutableStateFlow<GetAllSurveysState>(GetAllSurveysState.Idle)
    val getAllSurveysState = _getAllSurveysState.asStateFlow()

    private val _userSurveyResponseState = MutableStateFlow<UserSurveyResponseState>(UserSurveyResponseState.Idle)
    val userSurveyResponseState = _userSurveyResponseState.asStateFlow()







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

                Log.d("response",response.toString())
                Log.d("response",response.body().toString())

                Log.d("response",response.isSuccessful.toString())

                Log.d("response",response.code().toString())

                if(response.isSuccessful && response.body()!=null) {
                    _addPostState.value= AddPostState.Success(response.body()!!)
                }
                else{
                    _addPostState.value = AddPostState.Error(response.message())
                }
            }

            catch (e: Exception) {
                Log.d("response",e.toString())
                Log.d("response",e.message.toString())
                Log.d("response",e.stackTraceToString())
                _addPostState.value = AddPostState.Error(e.message ?: "Unknown error")
            }
        }

        }

    fun get_all_posts(
        category: String?
    ){
        _getAllPostsState.value = GetAllPostsState.Loading

        viewModelScope.launch {
            try {
                val response = repo.getAllPosts(
                    category=category
                )

                Log.d("response",response.toString())
                Log.d("response",response.body().toString())

                Log.d("response",response.isSuccessful.toString())

                Log.d("response",response.code().toString())


                if(response.isSuccessful && response.body()!=null) {
                    _getAllPostsState.value= GetAllPostsState.Success(response.body()!!)
                }
                else{
                    _getAllPostsState.value = GetAllPostsState.Error(response.message())
                }
            }

            catch (e: Exception) {

                Log.d("response",e.toString())
                Log.d("response",e.message.toString())
                Log.d("response",e.stackTraceToString())
                _getAllPostsState.value = GetAllPostsState.Error(e.message ?: "Unknown error")
            }
        }

    }


    fun get_my_posts(
        category: String?
    ){
        _getMyPostsState.value = GetMyPostsState.Loading

        viewModelScope.launch{

            _getMyPostsState.value = GetMyPostsState.Loading

            try {
                val response = repo.getMyPosts(
                    category=category
                )

                if(response.isSuccessful && response.body()!=null) {
                    _getMyPostsState.value= GetMyPostsState.Success(response.body()!!)
                }

                else{
                    _getMyPostsState.value = GetMyPostsState.Error(response.message())
                }

                }
            catch (e: Exception) {
                _getMyPostsState.value = GetMyPostsState.Error(e.message ?: "Unknown error")
            }
        }

        }

    fun get_all_surveys(){

        _getAllSurveysState.value = GetAllSurveysState.Loading

        viewModelScope.launch {

            try {
                val response = repo.getAllSurveys()
                Log.d("response",response.toString())
                Log.d("response",response.body().toString())
                Log.d("response",response.isSuccessful.toString())
                Log.d("response",response.code().toString())


                if(response.isSuccessful && response.body()!=null) {
                    _getAllSurveysState.value= GetAllSurveysState.Success(response.body()!!)
                }

                else{
                    _getAllSurveysState.value = GetAllSurveysState.Error(response.message())
                }

            }
            catch (e: Exception) {
                _getAllSurveysState.value = GetAllSurveysState.Error(e.message ?: "Unknown error")
            }
        }

    }

    fun user_survey_response(request: List<UserSurveyResponse>){
        _userSurveyResponseState.value = UserSurveyResponseState.Loading

        viewModelScope.launch {
            try {

                Log.d("response",request.toString())
                val response = repo.userSurveyResponse(request)

                Log.d("response",response.toString())
                Log.d("response",response.body().toString())
                Log.d("response",response.isSuccessful.toString())
                Log.d("response",response.code().toString())

                if(response.isSuccessful && response.body()!=null) {
                    _userSurveyResponseState.value= UserSurveyResponseState.Success(response.body()!!)
                    Log.d("response",response.body().toString())
                }

                else{
                    _userSurveyResponseState.value = UserSurveyResponseState.Error(response.message())
                }

                }
            catch (e: Exception) {

                Log.d("response",e.toString())
                Log.d("response",e.message.toString())
                Log.d("response",e.stackTraceToString())
                _userSurveyResponseState.value = UserSurveyResponseState.Error(e.message ?: "Unknown error")
            }

        }

    }






}



