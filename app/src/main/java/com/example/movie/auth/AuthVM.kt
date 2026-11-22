package com.example.movie.auth

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody


sealed class GetAllUsersState{
    object Idle : GetAllUsersState()
    object Loading : GetAllUsersState()
    data class Success(val data: List<User>) : GetAllUsersState()
    data class Error(val message: String) : GetAllUsersState()
}

sealed class RegisterState{
    object Idle : RegisterState()
    object Loading : RegisterState()
    data class Success(val data: RegisterResponse) : RegisterState()
    data class Error(val message: String) : RegisterState()
}

sealed class CreateUserState{
    object Idle : CreateUserState()
    object Loading : CreateUserState()
    data class Success(val data: USerProfileRes) : CreateUserState()
    data class Error(val message: String) : CreateUserState()
}



class AuthVM(application: Application): AndroidViewModel(application){

    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val registerState = _registerState.asStateFlow()

    private val _createUserState = MutableStateFlow<CreateUserState>(CreateUserState.Idle)
    val createUserState = _createUserState.asStateFlow()

    private val _getAllUsersState = MutableStateFlow<GetAllUsersState>(GetAllUsersState.Idle)
    val getAllUsersState = _getAllUsersState.asStateFlow()



    val sessionManager= SessionManager(application)

    val repo=AuthRepo(sessionManager)


    fun registerUser(
        request: RegisterReq
    ){

        _registerState.value = RegisterState.Loading

        viewModelScope.launch {
            try {
                val response = repo.register(request)

                if(response.isSuccessful && response.body()!=null) {
                    _registerState.value= RegisterState.Success(response.body()!!)

                    sessionManager.saveTokens(response.body()!!.tokens.access)
                    sessionManager.saveRole(response.body()!!.role)


                }
                else{
                    _registerState.value = RegisterState.Error(response.message())
                }
            }

            catch (e: Exception) {
                _registerState.value = RegisterState.Error(e.message ?: "Unknown error")

                }
        }
    }

    fun loginUser(
        request: LoginReq
    ){
        _registerState.value = RegisterState.Loading

        viewModelScope.launch{
            try {
                val response = repo.login(request)

                if(response.isSuccessful && response.body()!=null) {
                    _registerState.value = RegisterState.Success(response.body()!!)

                    sessionManager.saveTokens(response.body()!!.tokens.access)
                    sessionManager.saveRole(response.body()!!.role)
                }
                else{
                    _registerState.value = RegisterState.Error(response.message())
                }
            }
            catch (e: Exception) {
                _registerState.value = RegisterState.Error(e.message ?: "Unknown error")

            }
        }
    }

    fun createUserProfile(
        name:String,
        gender:String,
        city:String,
        profile: MultipartBody.Part?
    ){
        _createUserState.value = CreateUserState.Loading

        viewModelScope.launch {
            try {
                val response = repo.createUser(
                    name=name,
                    gender=gender,
                    city=city,
                    profile=profile
                )

                if(response.isSuccessful && response.body()!=null) {
                    _createUserState.value = CreateUserState.Success(response.body()!!)

                    Log.d("m","success")

                    Log.d("m",response.body().toString())






                }
                else{

                    Log.d("m","error")

                    _createUserState.value = CreateUserState.Error(response.message())

                    }

            }
            catch (e: Exception) {
                Log.d("m",e.message.toString())
                Log.d("m",e.toString())
                Log.d("m",e.stackTraceToString())

                _createUserState.value = CreateUserState.Error(e.message ?: "Unknown error")
                }
        }

    }

    fun getAllUsers(){
        _getAllUsersState.value = GetAllUsersState.Loading
        viewModelScope.launch {
            try {
                val response = repo.getAllUsers()
                if(response.isSuccessful && response.body()!=null) {
                    _getAllUsersState.value = GetAllUsersState.Success(response.body()!!)
                }
                else{
                    _getAllUsersState.value = GetAllUsersState.Error(response.message())
                }
            }
            catch (e: Exception) {
                _getAllUsersState.value = GetAllUsersState.Error(e.message ?: "Unknown error")
            }
        }

    }


}