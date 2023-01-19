package com.zb.finpassword.utils

import android.app.ProgressDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.pm.PackageManager
import android.util.Base64
import android.util.Log
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class Utilities(context: Context) {

    private val context: Context? = null
    var dialog: ProgressDialog? = null


    fun showProgressDialog(ctx: Context?, msg: String?) {
        dialog = ProgressDialog(ctx)
        dialog!!.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        dialog!!.setMessage(msg)
        dialog!!.setIndeterminate(true)
        dialog!!.setCanceledOnTouchOutside(false)
        dialog!!.show()
    }

    fun hideProgressDialog() {
        if (dialog != null && dialog!!.isShowing()) {
            dialog!!.cancel()
            dialog = null
        }
    }

    fun saveInt(context: Context, key: String?, value: Int) {

        val sharedPref = context.getSharedPreferences("fedsenseappshared", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putInt(key, value)
        editor.apply()
    }


    fun getInt(context: Context, key: String?): Int {
        val sharedPref = context.getSharedPreferences("fedsenseappshared", Context.MODE_PRIVATE)
        return sharedPref.getInt(key, 0)
    }


    fun saveString(
        context: Context, key: String, value: String
    ) {
        val sharedPref = context.getSharedPreferences("fedsenseappshared", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getString(context: Context, key: String): String {
        val sharedPref = context.getSharedPreferences("fedsenseappshared", Context.MODE_PRIVATE)
        return sharedPref.getString(key, "").toString()
    }


    fun clearSharedPref(context: Context) {
        val sharedPref =
            context.getSharedPreferences("fedsenseappshared", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.clear()
        editor.apply()
    }

    fun printHashKey(Context: Context) {
        try {
            val info = Context.packageManager.getPackageInfo(
                Context.packageName,
                PackageManager.GET_SIGNATURES
            )
            for (signature in info.signatures) {
                val md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val hashKey: String = String(Base64.encode(md.digest(), 0))
                Log.i(TAG, "printHashKey() Hash Key: $hashKey")
            }
        } catch (e: NoSuchAlgorithmException) {
            Log.e(TAG, "printHashKey()", e)
        } catch (e: Exception) {
            Log.e(TAG, "printHashKey()", e)
        }
    }

}