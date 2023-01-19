package com.nosugar.ui.base

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.zb.finpassword.R

import com.zb.finpassword.utils.MyCustomProgressDialog


@SuppressLint("Registered")
open class BaseFragment(@LayoutRes contentLayoutId: Int):Fragment(contentLayoutId) {
    public lateinit var progressDialog: MyCustomProgressDialog
    /**
     * Common method to perform initial stuffs
     * setting content view to activity, initialize binding object
     * initialize database object to use activity wide
     *
     * @param layoutId
     */
//    protected fun bindView(layoutId: Int, inflater: LayoutInflater, container: ViewGroup?) {
//        mBinding = DataBindingUtil.inflate(inflater, layoutId, container, false)
//    }

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
    open fun setStatusBarColor(fActivity: FragmentActivity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val w = fActivity.window
            w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            w.statusBarColor = resources.getColor(R.color.colorPrimary)
            w.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }

}