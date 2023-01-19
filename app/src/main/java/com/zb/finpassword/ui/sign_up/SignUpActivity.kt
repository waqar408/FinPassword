package com.zb.finpassword.ui.sign_up

import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import com.nosugar.pref.AppConstant
import com.zb.deepstudy.enums.Status
import com.zb.finpassword.R
import com.zb.finpassword.ui.base.BaseActivity
import com.zb.finpassword.ui.dashboard.DashboardActivity
import com.zb.finpassword.ui.login.LoginActivity
import com.zb.finpassword.ui.login.ViewModel.LoginViewModel
import com.zb.finpassword.ui.sign_up.ViewModel.SignUpViewModel
import com.zb.finpassword.utils.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.activity_sign_up.edt_pwd
import kotlinx.android.synthetic.main.activity_sign_up.edt_user_name
import kotlinx.android.synthetic.main.dialog_common.*
import kotlinx.android.synthetic.main.toolbar.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignUpActivity : BaseActivity() {
    private val signUpViewModel: SignUpViewModel by viewModel()
    var apiKey=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        setUi()
    }
    fun setUi(){
        progressDialog = MyCustomProgressDialog(this)

        setFullScreen()
        img_back.setSafeOnClickListener {
            onBackPressed()
        }
        tv_acc.setSafeOnClickListener {
            start<LoginActivity>()
            finish()
        }
        btn_sign_up.setSafeOnClickListener {
            if(validate()){
                callSignUpApi()

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

    override fun onBackPressed() {
        finish()
    }

    fun setUpObserver() {

        signUpViewModel.keyResponse.observe(this, {
            when (it.status) {
                Status.LOADING -> {
                    hideKeyboard()
                    progressDialog!!.show()
                }
                Status.SUCCESS -> {
                    progressDialog!!.dismiss()
                    apiKey=it.data?.key.toString()
                    Log.e("key", it.data?.key.toString())
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

        signUpViewModel.signUpResponse.observe(this, {
            when (it.status) {
                Status.LOADING -> {
                    hideKeyboard()
                    progressDialog!!.show()
                }
                Status.SUCCESS -> {
                    progressDialog!!.dismiss()
                    Prefs.setValue(
                        this@SignUpActivity,
                        AppConstant.API_KEY,
                        apiKey
                    )
                    Prefs.setValue(
                        this@SignUpActivity,
                        AppConstant.USER_ID,
                        edt_user_name.asString()
                    )
                    Prefs.setValue(
                        this@SignUpActivity,
                        AppConstant.PASSWORD,
                        edt_pwd.asString()
                    )
                    start<DashboardActivity>()
                    finish()
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

        val params = mapOf(
            "userid" to "torsten.husmann@web.de",
            "pwd" to "test"

        )
        signUpViewModel.getKey(params)
        setUpObserver()

    }

    private fun callSignUpApi() {

        val params = mapOf(
            "username" to edt_user_name.asString(),
            "pwd" to edt_pwd.asString(),
            "key" to apiKey,
            "pwd2" to edt_confirm_pwd.asString(),
            "email" to edt_email.asString(),
            "name" to edt_company.asString(),
            "plz" to edt_postal_code.asString(),
            "strasse" to edt_street.asString(),
            "ort" to edt_city.asString(),
            "nummer" to edt_house_no.asString()






        )
        signUpViewModel.signUp(params)
        setUpObserver()

    }

    private fun validate(): Boolean {
        val name = edt_company.asString()
        val userName = edt_user_name.asString()
        val email = edt_email.asString()
        val city = edt_city.asString()
        val street = edt_street.asString()
        val houseNo = edt_house_no.asString()
        val pinCode = edt_postal_code.asString()
        val pwd = edt_pwd.asString()
        val confirmPwd = edt_confirm_pwd.asString()

        return if (name.isEmpty()) {
            showToasty(this, getString(R.string.error_name), "2")
            edt_company.requestFocus()
            false
        }else if (userName.isEmpty()) {
            showToasty(this, getString(R.string.error_user_name), "2")
            edt_user_name.requestFocus()
            false
        }else if (email.isEmpty()) {
            showToasty(this, getString(R.string.error_email), "2")
            edt_email.requestFocus()
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showToasty(this, getString(R.string.error_email1), "2")
            edt_email.requestFocus()
            false
        }else if (city.isEmpty()) {
            showToasty(this, getString(R.string.error_city), "2")
            edt_city.requestFocus()
            false
        }else if (street.isEmpty()) {
            showToasty(this, getString(R.string.error_street), "2")
            edt_street.requestFocus()
            false
        }else if (houseNo.isEmpty()) {
            showToasty(this, getString(R.string.error_house_no), "2")
            edt_house_no.requestFocus()
            false
        }else if (pinCode.isEmpty()) {
            showToasty(this, getString(R.string.error_pin_code), "2")
            false
        }else if (pwd.isEmpty()) {
            showToasty(this, getString(R.string.error_pwd), "2")
            false
        }else if (confirmPwd.isEmpty()) {
            showToasty(this, getString(R.string.error_confirm_pwd), "2")
            edt_confirm_pwd.requestFocus()
            false
        }else if (!pwd.equals(confirmPwd)) {
            showToasty(this, getString(R.string.error_pwd_mismatch), "2")
            edt_pwd.requestFocus()
            false
        }else {
            true
        }
    }



}