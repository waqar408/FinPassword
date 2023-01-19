package com.nosugar.pref

import android.content.Context
import android.content.SharedPreferences
import com.squareup.moshi.Moshi

import com.zb.finpassword.utils.Constant

class PreferencesHelperImpl constructor(context: Context) : PreferencesHelper {

    private var mPrefs: SharedPreferences =
        context.getSharedPreferences(Constant.APP_SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)

    companion object {
        const val IS_LOGGEN_IN = "is_logged_in"
        const val TOKEN = "token"
        const val USER = "user"
        const val DISCOUNT = "discount"
        const val KEY = "key"
        const val USER_ID = "user_id"
        const val STUDENT_USER = "student_user"
        const val TUTOR_USER = "tutor_user"
        const val NOTIFICATION = "notification"

    }

    override fun isLoggedIn(): Boolean {
        return mPrefs.getBoolean(IS_LOGGEN_IN, false)
    }

    override fun setIsLoggedIn() {
        mPrefs.edit().putBoolean(IS_LOGGEN_IN, true).apply()
    }

    override fun setToken(token: String) {
        mPrefs.edit().putString(TOKEN, token).apply()
    }

    override fun getToken(): String? {
        return mPrefs.getString(TOKEN, null)
    }

    override fun setKey(key: String) {
        mPrefs.edit().putString(KEY, key).apply()
    }

    override fun getKey(): String? {
        return mPrefs.getString(KEY, null)
    }

    override fun setNotification(notification: String) {
        mPrefs.edit().putString(NOTIFICATION, notification).apply()
    }

    override fun getNotification(): String? {
        return mPrefs.getString(NOTIFICATION, null)
    }

    override fun setUserId(userId: String) {
        mPrefs.edit().putString(USER_ID, userId).apply()
    }

    override fun getUserId(): String? {
        return mPrefs.getString(USER_ID, null)
    }

    override fun setDiscount(discount: String) {
        mPrefs.edit().putString(DISCOUNT, discount).apply()
    }

    override fun getDiscount(): String {
        return mPrefs.getString(DISCOUNT, "").toString()
    }
   /* override fun getToken(): String? {
        val token = mPrefs.getString(TOKEN, null)
        return token?.let {
            val tokenResponse =
                Moshi.Builder().build().adapter(TokenResponse::class.java).fromJson(token)
            tokenResponse?.accessToken
        }
    }*/


   /* override fun setUser(user: String) {
        mPrefs.edit().putString(USER, user).apply()
    }


    override fun getUser(): LoginResponse.Data? {
        val userJson = mPrefs.getString(USER, null)
        return userJson?.let {
            Moshi.Builder().build().adapter(LoginResponse.Data::class.java).fromJson(it)
        }
    }
*/




    override fun clear() {
        mPrefs.edit().clear().apply()
    }
}