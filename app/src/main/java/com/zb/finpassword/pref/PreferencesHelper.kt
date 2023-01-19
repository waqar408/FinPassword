package com.nosugar.pref




interface PreferencesHelper {
    fun isLoggedIn(): Boolean
    fun setIsLoggedIn()
    fun setToken(token: String)
    fun getToken(): String?
    fun setDiscount(coupon: String)
    fun getDiscount(): String?
  /*  fun setUser(user: String)
    fun getUser(): LoginResponse.Data?*/
    fun setKey(type: String)
    fun getKey(): String?
    fun setUserId(userId: String)
    fun getUserId(): String?
    fun setNotification(notification: String)
    fun getNotification(): String?

    fun clear()
}