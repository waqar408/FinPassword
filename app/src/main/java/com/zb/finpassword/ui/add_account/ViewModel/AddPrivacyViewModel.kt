package com.zb.finpassword.ui.add_account.ViewModel

import com.zb.finpassword.data.models.AddPrivacyDataResponse
import com.zb.finpassword.data.repository.AddPrivacyRepository


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


class AddPrivacyViewModel(
    private val addPrivacyRepository: AddPrivacyRepository,
    private val networkHelper: NetworkHelper
) : ViewModel() {



    private val _addPrivacyResponse = SingleLiveEvent<Resource<AddPrivacyDataResponse>>()
    val addPrivacyResponse: LiveData<Resource<AddPrivacyDataResponse>>
        get() = _addPrivacyResponse




    fun addPrivacyData(params: Map<String, String>) {
        viewModelScope.launch {
            _addPrivacyResponse.postValue(loading(null))
            if (networkHelper.isNetworkConnected()) {
                try {

                    addPrivacyRepository.addPrivacyData(params).let {
                        if (it.isSuccessful) {
                            _addPrivacyResponse.postValue(Resource.success(it.body()))

                            /*when (it.body()?.status) {
                                SUCCESS_CODE -> {

                                }
                                else -> {
                                    _addPrivacyResponse.postValue(it.body()?.status?.let { errorMessage ->
                                        Resource.error(
                                            errorMessage,
                                            null
                                        )
                                    })
                                }
                            }*/
                        } else {
                            val errorResponse =
                                Moshi.Builder().build().adapter(LoginResponse::class.java)
                                    .fromJson(it.errorBody()!!.string())
                            when {
                                it.code() == UNAUTHORIZED_TOKEN -> {
                                    _addPrivacyResponse.postValue(
                                        Resource.unAuthorized(
                                            it.message(),
                                            null
                                        )
                                    )
                                }
                                it.code() == ERROR_CODE -> {
                                    _addPrivacyResponse.postValue(errorResponse?.status?.let { errorMessage ->
                                        Resource.error(
                                            errorMessage,
                                            null
                                        )
                                    })
                                }
                                else -> {
                                    _addPrivacyResponse.postValue(Resource.error(COMMON_ERROR, null))
                                }
                            }
                        }
                    }
                } catch (e: UnknownHostException) {
                    _addPrivacyResponse.postValue(Resource.error(e.message.toString(), null))
                } catch (e: SocketTimeoutException) {
                    _addPrivacyResponse.postValue(Resource.error(e.message.toString(), null))
                } catch (e: Exception) {
                    e.printStackTrace()
                    _addPrivacyResponse.postValue(Resource.error(COMMON_ERROR, null))
                }
            } else {
                _addPrivacyResponse.postValue(Resource.networkError(INTERNET_ERROR, null))
            }
        }
    }



}