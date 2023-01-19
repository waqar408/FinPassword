package com.zb.finpassword.ui.sign_up.ViewModel


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squareup.moshi.Moshi

import com.zb.finpassword.data.models.KeyResponse
import com.zb.finpassword.data.models.LoginResponse
import com.zb.finpassword.data.models.SignUpResponse
import com.zb.finpassword.data.repository.LoginRepository
import com.zb.finpassword.data.repository.SignUpRepository
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


class SignUpViewModel(
    private val signUpRepository: SignUpRepository,
    private val networkHelper: NetworkHelper
) : ViewModel() {

    private val _keyResponse = SingleLiveEvent<Resource<KeyResponse>>()
    val keyResponse: LiveData<Resource<KeyResponse>>
        get() = _keyResponse

    private val _signUpResponse = SingleLiveEvent<Resource<SignUpResponse>>()
    val signUpResponse: LiveData<Resource<SignUpResponse>>
        get() = _signUpResponse



    fun getKey(params: Map<String, String>) {
        viewModelScope.launch {
            _keyResponse.postValue(loading(null))
            if (networkHelper.isNetworkConnected()) {
                try {

                    signUpRepository.getKey1(params).let {
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

    fun signUp(params: Map<String, String>) {
        viewModelScope.launch {
            _signUpResponse.postValue(loading(null))
            if (networkHelper.isNetworkConnected()) {
                try {

                    signUpRepository.signup(params).let {
                        if (it.isSuccessful) {
                            Log.e("apiStatus","success")

                            when (it.body()?.status) {
                                SUCCESS_CODE -> {

                                    _signUpResponse.postValue(Resource.success(it.body()))
                                }
                                else -> {
                                    _signUpResponse.postValue(it.body()?.status?.let { errorMessage ->
                                        Resource.error(
                                            errorMessage,
                                            null
                                        )
                                    })
                                }
                            }
                        } else {
                            Log.e("apiStatus","fail")
                            val errorResponse =
                                Moshi.Builder().build().adapter(SignUpResponse::class.java)
                                    .fromJson(it.errorBody()!!.string())
                            when {
                                it.code() == UNAUTHORIZED_TOKEN -> {
                                    _signUpResponse.postValue(
                                        Resource.unAuthorized(
                                            it.message(),
                                            null
                                        )
                                    )
                                }
                                it.code() == ERROR_CODE -> {
                                    _signUpResponse.postValue(errorResponse?.status?.let { errorMessage ->
                                        Resource.error(
                                            errorMessage,
                                            null
                                        )
                                    })
                                }
                                else -> {
                                    _signUpResponse.postValue(Resource.error(COMMON_ERROR, null))
                                }
                            }
                        }
                    }
                } catch (e: UnknownHostException) {
                    _signUpResponse.postValue(Resource.error(e.message.toString(), null))
                } catch (e: SocketTimeoutException) {
                    _signUpResponse.postValue(Resource.error(e.message.toString(), null))
                } catch (e: Exception) {
                    e.printStackTrace()
                    _signUpResponse.postValue(Resource.error(COMMON_ERROR, null))
                }
            } else {
                _signUpResponse.postValue(Resource.networkError(INTERNET_ERROR, null))
            }
        }
    }



}