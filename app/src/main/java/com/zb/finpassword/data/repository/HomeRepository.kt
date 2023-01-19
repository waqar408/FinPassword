package com.zb.finpassword.data.repository

import com.nosugar.pref.PreferencesHelper
import com.zb.finpassword.data.api.ApiService

class HomeRepository(
    private val preferencesHelper: PreferencesHelper,
    private val apiService: ApiService
) {

    suspend fun getCategory(params: Map<String, String>) = apiService.getCategory(params)

    suspend fun login(params: Map<String, String>) = apiService.login(params)

    suspend fun getKey1(params: Map<String, String>) = apiService.getKey(params)



    fun logout() = preferencesHelper.clear()




}