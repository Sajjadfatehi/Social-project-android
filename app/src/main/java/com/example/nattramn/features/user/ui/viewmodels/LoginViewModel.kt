package com.example.nattramn.features.user.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nattramn.core.resource.Resource
import com.example.nattramn.features.user.data.AuthRepository
import com.example.nattramn.features.user.data.models.AuthRequest
import com.example.nattramn.features.user.data.models.AuthResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val userRepository = AuthRepository.getInstance()

    var emailValidation = MutableLiveData<Resource<String>>()
    var passwordValidation = MutableLiveData<Resource<String>>()

    val loginResult = MutableLiveData<Resource<AuthResponse>>()

    fun loginUser(authRequest: AuthRequest) {
        loginResult.value = Resource.loading(null)

        viewModelScope.launch(Dispatchers.IO) {
            loginResult.postValue(userRepository.loginUser(authRequest))
        }
    }

}