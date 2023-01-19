package com.zb.finpassword.data.repository

import com.nosugar.pref.PreferencesHelper
import com.zb.finpassword.data.api.ApiService

class ListingRepository(
    private val preferencesHelper: PreferencesHelper,
    private val apiService: ApiService
) {




    fun logout() = preferencesHelper.clear()




}