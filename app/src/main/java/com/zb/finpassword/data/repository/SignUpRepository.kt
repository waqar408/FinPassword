package com.zb.finpassword.data.repository

import com.nosugar.pref.PreferencesHelper
import com.zb.finpassword.data.api.ApiService

class SignUpRepository(
    private val preferencesHelper: PreferencesHelper,
    private val apiService: ApiService
) {

    suspend fun getKey1(params: Map<String, String>) = apiService.getKey(params)

    suspend fun signup(params: Map<String, String>) = apiService.signup(params)







}