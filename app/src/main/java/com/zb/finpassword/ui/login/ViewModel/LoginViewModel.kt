package com.zb.finpassword.ui.login.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squareup.moshi.Moshi

import com.zb.finpassword.data.models.KeyResponse
import com.zb.finpassword.data.models.LoginResponse
import com.zb.finpassword.data.repository.LoginRepository
import com.zb.finpassword.utils.Constant
import com.zb.finpassword.utils.Constant.Companion.COMMON_ERROR
import com.zb.finpassword.utils.Constant.Companion.ERROR_CODE
import com.zb.finpassword.utils.Constant.Companion.INTERNET_ERROR
import com.zb.finpassword.utils.Constant.Companion.SUCCESS_CODE
import com.zb.finpassword.utils.Constant.Companion.UNAUTHORIZED_TOKEN
import com.zb.finpassword.utils.NetworkHelper
import com.zb.finpassword.utils.Resource
import com.zb.finpassword.utils.Resource.Companion.loading
import com.zb.finpassword.utils.SingleLiveEvent
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import java.net.UnknownHostException


class LoginViewModel(
    private val loginRepository: LoginRepository,
    private val networkHelper: NetworkHelper
) : ViewModel() {

    private val _keyResponse = SingleLiveEvent<Resource<KeyResponse>>()
    val keyResponse: LiveData<Resource<KeyResponse>>
        get() = _keyResponse

    private val _loginResponse = SingleLiveEvent<Resource<LoginResponse>>()
    val loginResponse: LiveData<Resource<LoginResponse>>
        get() = _loginResponse



    fun getKey(params: Map<String, String>) {
        viewModelScope.launch {
            _keyResponse.postValue(loading(null))
            if (networkHelper.isNetworkConnected()) {
                try {

                    loginRepository.getKey1(params).let {
                        if (it.isSuccessful) {
                            when (it.body()?.status) {
                                SUCCESS_CODE -> {

                                    _keyResponse.postValue(Resource.success(it.body()))
                                }
                                else -> {
                                    _keyResponse.postValue(it.body()?.status?.let { errorMessage ->
                                        Resource.error(
                                            errorMessage,
                                            null
                                        )
                                    })
                                }
                            }
                        } else {
                            val errorResponse =
                                Moshi.Builder().build().adapter(KeyResponse::class.java)
                                    .fromJson(it.errorBody()!!.string())
                            when {
                                it.code() == UNAUTHORIZED_TOKEN -> {
                                    _keyResponse.postValue(
                                        Resource.unAuthorized(
                                            it.message(),
                                            null
                                        )
                                    )
                                }
                                it.code() == ERROR_CODE -> {
                                    _keyResponse.postValue(errorResponse?.status?.let { errorMessage ->
                                        Resource.error(
                                            errorMessage,
                                            null
                                        )
                                    })
                                }
                                else -> {
                                    _keyResponse.postValue(Resource.error(COMMON_ERROR, null))
                                }
                            }
                        }
                    }
                } catch (e: UnknownHostException) {
                    _keyResponse.postValue(Resource.error(e.message.toString(), null))
                } catch (e: SocketTimeoutException) {
                    _keyResponse.postValue(Resource.error(e.message.toString(), null))
                } catch (e: Exception) {
                    e.printStackTrace()
                    _keyResponse.postValue(Resource.error(COMMON_ERROR, null))
                }
            } else {
                _keyResponse.postValue(Resource.networkError(INTERNET_ERROR, null))
            }
        }
    }

    fun login(params: Map<String, String>) {
        viewModelScope.launch {
            _loginResponse.postValue(loading(null))
            if (networkHelper.isNetworkConnected()) {
                try {

                    loginRepository.login(params).let {
                        if (it.isSuccessful) {
                            when (it.body()?.status) {
                                SUCCESS_CODE -> {

                                    _loginResponse.postValue(Resource.success(it.body()))
                                }
                                else -> {
                                    _loginResponse.postValue(it.body()?.status?.let { errorMessage ->
                                        Resource.error(
                                            errorMessage,
                                            null
                                        )
                                    })
                                }
                            }
                        } else {
                            val errorResponse =
                                Moshi.Builder().build().adapter(LoginResponse::class.java)
                                    .fromJson(it.errorBody()!!.string())
                            when {
                                it.code() == UNAUTHORIZED_TOKEN -> {
                                    _loginResponse.postValue(
                                        Resource.unAuthorized(
                                            it.message(),
                                            null
                                        )
                                    )
                                }
                                it.code() == ERROR_CODE -> {
                                    _loginResponse.postValue(errorResponse?.status?.let { errorMessage ->
                                        Resource.error(
                                            errorMessage,
                                            null
                                        )
                                    })
                                }
                                else -> {
                                    _loginResponse.postValue(Resource.error(COMMON_ERROR, null))
                                }
                            }
                        }
                    }
                } catch (e: UnknownHostException) {
                    _loginResponse.postValue(Resource.error(e.message.toString(), null))
                } catch (e: SocketTimeoutException) {
                    _loginResponse.postValue(Resource.error(e.message.toString(), null))
                } catch (e: Exception) {
                    e.printStackTrace()
                    _loginResponse.postValue(Resource.error(COMMON_ERROR, null))
                }
            } else {
                _loginResponse.postValue(Resource.networkError(INTERNET_ERROR, null))
            }
        }
    }



}