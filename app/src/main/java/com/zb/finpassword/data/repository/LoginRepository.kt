package com.zb.finpassword.data.repository

import com.nosugar.pref.PreferencesHelper
import com.zb.finpassword.data.api.ApiService

class LoginRepository(
    private val preferencesHelper: PreferencesHelper,
    private val apiService: ApiService
) {

    suspend fun getKey1(params: Map<String, String>) = apiService.getKey(params)

    suspend fun login(params: Map<String, String>) = apiService.login(params)







}