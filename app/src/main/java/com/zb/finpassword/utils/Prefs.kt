package com.zb.finpassword.utils

import android.content.Context
import android.content.SharedPreferences
import com.nosugar.pref.AppConstant


object Prefs {

    private var sharedPreferences: SharedPreferences? = null

    private fun openPrefs(context: Context) {
        sharedPreferences = context.getSharedPreferences("fin_password", Context.MODE_PRIVATE)
    }

  /*  fun setValue(context: Context, key: String, value: Any) {
        openPrefs(context)
        val prefsEdit: SharedPreferences.Editor? = sharedPreferences!!.edit()

        when (value) {
            is String -> prefsEdit?.putString(key, value)
            is Int -> prefsEdit?.putInt(key, value)
            is Boolean -> prefsEdit?.putBoolean(key, value)
            is Long -> prefsEdit?.putLong(key, value)
            is Float -> prefsEdit?.putFloat(key, value)
            else -> prefsEdit?.putString(key, Gson().toJson(value))
        }
        prefsEdit?.apply()
        sharedPreferences = null
    }*/


    fun getValue(context: Context, key: String, defaultValue: Any): Any {
        openPrefs(context)
        val result = when (defaultValue) {
            is String -> sharedPreferences?.getString(key, defaultValue).toString()
            is Int -> sharedPreferences?.getInt(key, defaultValue)
            is Boolean -> sharedPreferences?.getBoolean(key, defaultValue)
            is Long -> sharedPreferences?.getLong(key, defaultValue)
            is Float -> sharedPreferences?.getFloat(key, defaultValue)
            else -> ""
        }

        sharedPreferences = null
        return result ?: ""
    }

   /* fun getValue(context: Context, key: String, defaultValue: String): String? {
        openPrefs(context)
        val result = sharedPreferences!!.getString(key, defaultValue)
        sharedPreferences = null
        if (key == AppConstant.LAT || key == AppConstant.LONG) {
            if (result == "") {
                return "0.0"
            }
        }
        return result
    }*/

    fun getValue(context: Context, key: String, defaultValue: Int): Int {
        openPrefs(context)
        val result = sharedPreferences!!.getInt(key, defaultValue)
        sharedPreferences = null
        return result
    }

    fun getValue(context: Context, key: String, defaultValue: Boolean): Boolean {
        openPrefs(context)
        val result = sharedPreferences!!.getBoolean(key, defaultValue)
        sharedPreferences = null
        return result
    }
    fun setValue(context: Context, key: String, value: Any) {
        openPrefs(context)
        val prefsEdit: SharedPreferences.Editor? = sharedPreferences!!.edit()

        when (value) {
            is String -> prefsEdit?.putString(key, value)
            is Int -> prefsEdit?.putInt(key, value)
            is Boolean -> prefsEdit?.putBoolean(key, value)
            is Long -> prefsEdit?.putLong(key, value)
            is Float -> prefsEdit?.putFloat(key, value)
            else ->  error("Only primitive types can be stored with put() function.")
        }
        prefsEdit?.apply()
        sharedPreferences = null
    }







    fun removeValue(context: Context, key: String) {
        openPrefs(context)
        val editor = sharedPreferences!!.edit()
        editor.remove(key)
        editor.apply()
    }

    fun clearAllData(context: Context) {
        openPrefs(context)
        val editor = sharedPreferences!!.edit()
        editor.clear()
        editor.apply()
    }

    fun contain(context: Context, key: String): Boolean {
        openPrefs(context)
        return sharedPreferences!!.contains(key)
    }
}

