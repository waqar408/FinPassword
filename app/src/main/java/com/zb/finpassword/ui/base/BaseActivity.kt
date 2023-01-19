package com.zb.finpassword.ui.base

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.zb.finpassword.R
import com.zb.finpassword.utils.MyCustomProgressDialog
import dev.b3nedikt.app_locale.AppLocale


import io.github.inflationx.viewpump.ViewPumpContextWrapper

abstract class BaseActivity : AppCompatActivity() {

    public lateinit var progressDialog: MyCustomProgressDialog

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap( AppLocale.wrap(newBase)))
    }

    override fun getResources(): Resources {
        return AppLocale.wrap(baseContext).resources
    }
    protected fun setTransparentStatusBarOnly() {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = Color.TRANSPARENT
        // this lines ensure only the status-bar to become transparent without affecting the nav-bar
        window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    }

    open fun setStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val w = window
            w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            w.statusBarColor = resources.getColor(R.color.white)
            w.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }
    /**
     * Common method to show loading indication while performing long operation or network calls
     *
     * @param context   activity or fragment activity context
     */
    protected fun showLoader(context: Context) {
        if (::progressDialog.isInitialized) {
            if (progressDialog.isShowing)
                progressDialog.dismiss()
        }
        progressDialog = MyCustomProgressDialog(context)
        progressDialog.setCancelable(false)
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        progressDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        progressDialog.setContentView(R.layout.layout_progress_indicator)
        progressDialog.window?.setDimAmount(0.75f)
        progressDialog.show()
    }

    /**
     * Common method to dismiss loading indication
     */
    protected fun dismissLoader() {
        if (::progressDialog.isInitialized)
            progressDialog.dismiss()
    }


    /**
     * make sure to dispose dialogs and other heavy objects before destroying activity
     * it will help to prevent window leaks, memory leaks etc..
     */
    override fun onStop() {
        super.onStop()
        if (::progressDialog.isInitialized) {
            if (progressDialog.isShowing)
                progressDialog.dismiss()
        }
    }
}