package com.zb.finpassword.ui.splash

import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.nosugar.pref.AppConstant
import com.zb.finpassword.R
import com.zb.finpassword.ui.dashboard.DashboardActivity
import com.zb.finpassword.ui.login.LoginActivity
import com.zb.finpassword.utils.Prefs
import com.zb.finpassword.utils.start
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        setUi()
    }
    fun setUi(){
        setFullScreen()
        if (Prefs.getValue(this@SplashActivity, AppConstant.API_KEY, "")!!.equals("")) {

            GlobalScope.launch {
                delay(3000)
                start<LoginActivity>(isFinish = true)
            }
        }else{
            GlobalScope.launch {
                delay(3000)
                start<DashboardActivity>(isFinish = true)
            }
        }
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