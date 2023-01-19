package com.zb.finpassword.ui.edit_profile

import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import com.zb.finpassword.R
import com.zb.finpassword.utils.setSafeOnClickListener
import kotlinx.android.synthetic.main.toolbar.*

class EditProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        setUi()
    }
    fun setUi(){
        setFullScreen()
        tv_title.text=getString(R.string.edit_profile)
        img_back.setSafeOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        finish()
    }
    fun setFullScreen(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
            window.statusBarColor = Color.TRANSPARENT

        }
    }
}