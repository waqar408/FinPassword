package com.zb.finpassword.ui.forgot_pwd

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.zb.finpassword.R
import com.zb.finpassword.utils.setSafeOnClickListener
import kotlinx.android.synthetic.main.activity_forgot_pwd.*

class ForgotPwdActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_pwd)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        setUi()
    }
    fun setUi(){
        img_back.setSafeOnClickListener {
            onBackPressed()
        }
        btn_send.setSafeOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        finish()
    }
}