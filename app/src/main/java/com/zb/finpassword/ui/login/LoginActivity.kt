package com.zb.finpassword.ui.login

import android.app.ProgressDialog
import android.graphics.Color
import android.hardware.biometrics.BiometricManager
import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.nosugar.pref.AppConstant
import com.zb.deepstudy.enums.Status
import com.zb.finpassword.R
import com.zb.finpassword.Retrofit.RetroClient
import com.zb.finpassword.data.models.KeyResponse
import com.zb.finpassword.ui.base.BaseActivity
import com.zb.finpassword.ui.dashboard.DashboardActivity
import com.zb.finpassword.ui.forgot_pwd.ForgotPwdActivity
import com.zb.finpassword.ui.login.ViewModel.LoginViewModel
import com.zb.finpassword.ui.sign_up.SignUpActivity
import com.zb.finpassword.utils.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.dialog_common.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executor


class LoginActivity : BaseActivity() {

    private val loginViewModel: LoginViewModel by viewModel()
    var apiKey=""
    private lateinit var executor: Executor
    private lateinit var biometricManager: androidx.biometric.BiometricPrompt
    private lateinit var biometricPrompt: androidx.biometric.BiometricPrompt
    private lateinit var promptInfo: androidx.biometric.BiometricPrompt.PromptInfo
    var userName = ""
    var pwd = ""
    val utilities  = Utilities(this@LoginActivity)
    var getName = ""
    var getPassword = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        setUi()
        executor = ContextCompat.getMainExecutor(this)



       biometricPrompt = androidx.biometric.BiometricPrompt(this@LoginActivity,executor,object : androidx.biometric.BiometricPrompt.AuthenticationCallback(){
           override fun onAuthenticationSucceeded(result: androidx.biometric.BiometricPrompt.AuthenticationResult) {
               super.onAuthenticationSucceeded(result)
               getName = utilities.getString(this@LoginActivity,"name")
               getPassword = utilities.getString(this@LoginActivity,"password")
               if (getName.equals(""))
               {
                   if (getPassword.equals(""))
                   {
                       Toast.makeText(this@LoginActivity,"You Must Login First",Toast.LENGTH_SHORT).show()
                   }

               }else{
                   Toast.makeText(this@LoginActivity,"Auth Success",Toast.LENGTH_SHORT).show()
                   callKeyApi()
               }
           }

           override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
               super.onAuthenticationError(errorCode, errString)
               Toast.makeText(this@LoginActivity,"Auth Error",Toast.LENGTH_SHORT).show()

           }

           override fun onAuthenticationFailed() {
               super.onAuthenticationFailed()
               Toast.makeText(this@LoginActivity,"Auth Failed",Toast.LENGTH_SHORT).show()

           }
       })

        promptInfo = androidx.biometric.BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric login for my app")
            .setSubtitle("Log in using your biometric credential")
            .setNegativeButtonText("Use account password")
            .build()
    }

    fun setUi() {
        progressDialog = MyCustomProgressDialog(this)

        setFullScreen()

        btn_log_in.setSafeOnClickListener {
           if(validate()){
               /*userName = edt_user_name.asString()
               pwd = edt_pwd.asString()
               utilities.saveString(this@LoginActivity,"name",userName)
               utilities.saveString(this@LoginActivity,"password",pwd)*/
               callKeyApi()
           }
        }
        tv_forgot_pwd.setSafeOnClickListener {
            start<ForgotPwdActivity>()
        }
        tv_no_acc.setSafeOnClickListener {
            start<SignUpActivity>()
        }
        imageView2.setOnClickListener {
            biometricPrompt.authenticate(promptInfo)

        }
    }

    fun setFullScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
            window.statusBarColor = Color.TRANSPARENT

        }
    }

    fun setUpObserver() {

        loginViewModel.keyResponse.observe(this, {
            when (it.status) {
                Status.LOADING -> {
                    hideKeyboard()
                    progressDialog!!.show()
                }
                Status.SUCCESS -> {
                    progressDialog!!.dismiss()
                    apiKey=it.data?.key.toString()
                    Log.e("key", it.data?.key.toString())
                    callLoginApi()
                }


                Status.ERROR -> {
                    progressDialog!!.dismiss()
                    showToasty(this, it.message!!, "2")

                }

                Status.UNAUTHORIZED -> {
                    progressDialog!!.dismiss()
                    showToasty(this, "Username or Password wrong", "2")

                }

                Status.NETWORK_ERROR -> {
                    progressDialog!!.dismiss()
                    it.message?.let { message ->
                        showDialogN(R.layout.dialog_common) {
                            messageLTV?.text = String.format(message)
                            submitLBTN?.text = getStr(R.string.retry)
                            submitLBTN?.setSafeOnClickListener {
                                dismissLoader()
                                callKeyApi()
                            }
                        }
                    }

                }
            }
        })

        loginViewModel.loginResponse.observe(this, {
            when (it.status) {
                Status.LOADING -> {
                    hideKeyboard()
                    progressDialog!!.show()
                }
                Status.SUCCESS -> {
                    progressDialog!!.dismiss()
                    getName = utilities.getString(this@LoginActivity,"name")
                    getPassword = utilities.getString(this@LoginActivity,"password")
                    if (getName.equals(""))
                    {
                        if (getPassword.equals(""))
                        {
                            Prefs.setValue(
                                this@LoginActivity,
                                AppConstant.API_KEY,
                                apiKey
                            )
                            Prefs.setValue(
                                this@LoginActivity,
                                AppConstant.USER_ID,
                                edt_user_name.asString()
                            )
                            Prefs.setValue(
                                this@LoginActivity,
                                AppConstant.PASSWORD,
                                edt_pwd.asString()
                            )
                            start<DashboardActivity>()
                            finish()
                        }
                    }else{
                        Prefs.setValue(
                            this@LoginActivity,
                            AppConstant.API_KEY,
                            apiKey
                        )
                        Prefs.setValue(
                            this@LoginActivity,
                            AppConstant.USER_ID,
                            getName
                        )
                        Prefs.setValue(
                            this@LoginActivity,
                            AppConstant.PASSWORD,
                            getPassword
                        )
                        start<DashboardActivity>()
                        finish()
                    }
                    /*Prefs.setValue(
                        this@LoginActivity,
                        AppConstant.API_KEY,
                        apiKey
                    )
                    Prefs.setValue(
                        this@LoginActivity,
                        AppConstant.USER_ID,
                       edt_user_name.asString()
                    )
                    Prefs.setValue(
                        this@LoginActivity,
                        AppConstant.PASSWORD,
                        edt_pwd.asString()
                    )
                    start<DashboardActivity>()
                    finish()*/
                }


                Status.ERROR -> {
                    progressDialog!!.dismiss()
                    showToasty(this, it.message!!, "2")

                }

                Status.NETWORK_ERROR -> {
                    progressDialog!!.dismiss()
                    it.message?.let { message ->
                        showDialogN(R.layout.dialog_common) {
                            messageLTV?.text = String.format(message)
                            submitLBTN?.text = getStr(R.string.retry)
                            submitLBTN?.setSafeOnClickListener {
                                dismissLoader()
                                callKeyApi()
                            }
                        }
                    }

                }
            }
        })


    }

    private fun callKeyApi() {
        getName = utilities.getString(this@LoginActivity,"name")
        getPassword = utilities.getString(this@LoginActivity,"password")
        if (getName.equals("")){
            if (getPassword.equals(""))
            {
                val params = mapOf(
                    "userid" to edt_user_name.asString(),
                    "pwd" to edt_pwd.asString()

                )
                loginViewModel.getKey(params)
                setUpObserver()
            }
        }else{
            val params = mapOf(
                "userid" to getName,
                "pwd" to getPassword

            )
            loginViewModel.getKey(params)
            setUpObserver()
        }

    }

    private fun callLoginApi() {
        getName = utilities.getString(this@LoginActivity,"name")
        getPassword = utilities.getString(this@LoginActivity,"password")
        if (getName.equals("")){
            if (getPassword.equals(""))
            {
                val params = mapOf(
                    "userid" to edt_user_name.asString(),
                    "pwd" to edt_pwd.asString(),
                    "key" to apiKey

                )
                loginViewModel.login(params)
                setUpObserver()
                utilities.saveString(this@LoginActivity,"name",edt_user_name.asString())
                utilities.saveString(this@LoginActivity,"password",edt_pwd.asString())
            }
        }else{
            val params = mapOf(
                "userid" to getName,
                "pwd" to getPassword,
                "key" to apiKey

            )
            loginViewModel.login(params)
            setUpObserver()
        }
        /*val params = mapOf(
            "userid" to edt_user_name.asString(),
            "pwd" to edt_pwd.asString(),
            "key" to apiKey

        )
        loginViewModel.login(params)
        setUpObserver()*/

    }

    private fun validate(): Boolean {
        userName = edt_user_name.asString()
         pwd = edt_pwd.asString()

        return if (userName.isEmpty()) {
            showToasty(this, getString(R.string.error_user_name), "2")
            edt_user_name.requestFocus()
            false
        }else if (pwd.isEmpty()) {
            showToasty(this, getString(R.string.error_pwd), "2")
            edt_pwd.requestFocus()
            false
        }  else {
            true
        }
    }

    override fun onBackPressed() {
        finish()
    }
}