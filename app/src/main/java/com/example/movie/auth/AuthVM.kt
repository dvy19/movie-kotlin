package com.example.movie.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


sealed class RegisterState{
    object Idle : RegisterState()
    object Loading : RegisterState()
    data class Success(val data: RegisterResponse) : RegisterState()
    data class Error(val message: String) : RegisterState()
}
class AuthVM(application: Application): AndroidViewModel(application){

    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val registerState = _registerState.asStateFlow()

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


}