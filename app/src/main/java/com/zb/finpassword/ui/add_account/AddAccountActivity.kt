package com.zb.finpassword.ui.add_account

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.util.Patterns
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.nosugar.pref.AppConstant
import com.zb.deepstudy.enums.Status
import com.zb.finpassword.R
import com.zb.finpassword.Retrofit.RetroClient
import com.zb.finpassword.data.api.ApiService
import com.zb.finpassword.ui.add_account.ViewModel.AddPrivacyViewModel
import com.zb.finpassword.ui.base.BaseActivity
import com.zb.finpassword.ui.dashboard.DashboardActivity
import com.zb.finpassword.ui.listing.ListingActivity
import com.zb.finpassword.utils.*
import kotlinx.android.synthetic.main.activity_add_account.*
import kotlinx.android.synthetic.main.activity_listing.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.dialog_common.*
import kotlinx.android.synthetic.main.toolbar.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern


class AddAccountActivity : BaseActivity() {
    var apiService: ApiService? = null
    var keyList: ArrayList<String> = ArrayList<String>()
    var mainKeyList: ArrayList<String> = ArrayList<String>()
    var fieldKeyList: ArrayList<String> = ArrayList<String>()
    var paramValueKeyList: ArrayList<String> = ArrayList<String>()

    private val editTextList: ArrayList<EditText> = ArrayList()
    var list = ArrayList<String>()
    var category = ""
    private val addPrivacyDataModel: AddPrivacyViewModel by viewModel()

    var i = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_account)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        setUi()
    }

    fun setUi() {
        progressDialog = MyCustomProgressDialog(this)

        val b = intent.extras
        if (b != null) {
            category = b.getString(CATEGORY).toString()


        }
        progressDialog = MyCustomProgressDialog(this@AddAccountActivity)
        setFullScreen()
        tv_title.text = "Add " + category
        img_back.setSafeOnClickListener {
            onBackPressed()
        }
        btn_save.setSafeOnClickListener {
            val stringBuilder = StringBuilder()
            for (editText in editTextList) {
                if (!editText.equals("")) {
                    stringBuilder.append(editText.text.toString() + ",")

                }
            }

            list = stringBuilder.toString().split(",") as ArrayList<String>
            list.removeAt(list.size - 1)
            val gson = Gson()
            val arrayData = gson.toJson(list)
            Log.e("fieldKey", arrayData.toString())
            Log.e("fieldKey", fieldKeyList.toString())
            val keyValue = arrayData.toString().replace("[", "")
                .replace("]", "").replace("\"", "")

           val valueKeyList: List<String> = keyValue.split(",")
            Log.e("fieldKey", valueKeyList.toString())

            paramValueKeyList.clear()
            for (i in 0 until fieldKeyList.size) {
                paramValueKeyList.add(valueKeyList.get(i))
            }



            var isValid=true
            var errorTxt=""
            var pos=0

            for (i in 0 until fieldKeyList!!.size) {
                if(fieldKeyList!!.get(i).equals("email")){
                    pos=i
                    break
                }
            }


            for (i in 0 until fieldKeyList!!.size) {
                paramValueKeyList.add(valueKeyList.get(i))

                if (valueKeyList.get(i).toString().equals("")) {
                    isValid=false
                    errorTxt=fieldKeyList.get(i).toString()
                    break
                }

            }
            if(isValid==false){
                showToasty(this, "Please enter valid " + errorTxt, "2")

            }else{
                if(fieldKeyList!!.contains("email")){
                    if(isEmailValid(valueKeyList.get(pos))){
                        callAddPrivacyDataApi()

                    }else{
                        showToasty(this, "Please enter valid " + errorTxt, "2")

                    }

                }else{
                    callAddPrivacyDataApi()
                }

            }

/*
            for (i in 0 until fieldKeyList.size) {
                paramValueKeyList.add(valueKeyList.get(i))
                if (valueKeyList.get(i).toString().equals("")) {
                    showToasty(this, "Please enter valid " + fieldKeyList.get(i), "2")
                    break
                }else{
                    if (fieldKeyList!!.get(i).toString().equals("email")) {

                        if (!Patterns.EMAIL_ADDRESS.matcher(valueKeyList.get(i).toString()).matches()) {
                            showToasty(this, getString(R.string.error_email1), "2")
                            false
                            break
                        }else{
                            callAddPrivacyDataApi()
                            break
                        }

                    }else{
                        callAddPrivacyDataApi()
                        break
                    }




                }
            }
*/

        }
        /*for (j in 0 until 4) {
            i++
            setDynamicLayout()
        }*/
        getFieldData()
    }
    fun isEmailValid(email: String?): Boolean {
        val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
        val pattern: Pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher: Matcher = pattern.matcher(email)
        return matcher.matches()
    }
    companion object {
        const val CATEGORY = "category"

        fun getStartIntent(context: Context, category: String): Intent {
            return Intent(context, AddAccountActivity::class.java).putExtra(
                CATEGORY,
                category
            )
        }
    }


    fun setDynamicLayout(name: String) {
        // Create EditText
        val tv1 = TextView(this)
        tv1.setText(name)

        tv1.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.app_color));
        val editTextParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT
        )
        if (i == 1) {
            editTextParams.setMargins(45, 120, 0, 0)

        } else {
            editTextParams.setMargins(45, 60, 0, 0)

        }
        tv1.setLayoutParams(editTextParams)

        tv1.setTypeface(Typeface.createFromAsset(getAssets(), "font1.otf"));
        tv1.setTextSize(
            TypedValue.COMPLEX_UNIT_PX,
            getResources().getDimension(R.dimen.text_size_14sp)
        );


        // Create EditText
        val editText = EditText(this)
        val editTextParams1 = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 150
        )
        editText.setTag("edt" + i)
        Log.e("edtId", i.toString())
        editTextParams1.setMargins(45, 12, 45, 0)
        editText.setLayoutParams(editTextParams1)
        editText.setTextCursorDrawable(R.drawable.cursor_bg)
        editText.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        editText.setBackgroundResource(R.drawable.edt_bg)
        editText.setPadding(60, 0, 0, 0)
        editText.setTextColor(Color.parseColor("#000000"))
        editText.setTextSize(
            TypedValue.COMPLEX_UNIT_PX,
            getResources().getDimension(R.dimen.text_size_14sp)
        );

        if (name.equals("passwort")) {
            editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        } else if (name.equals("email")) {

            editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        } else {
            editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES

        }

        editTextList.add(editText)
        ll_edit_text?.addView(tv1)
        ll_edit_text?.addView(editText)
    }

    fun setFullScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
            window.statusBarColor = Color.TRANSPARENT

        }
    }

    override fun onBackPressed() {
        start2(
            ListingActivity.getStartIntent(
                this@AddAccountActivity,
                category
            )
        )
        finish()
    }

    fun getFieldData() {
        progressDialog.show()
        apiService = RetroClient.getApiService()
        val call: Call<JsonObject> =
            apiService!!.getField(
                Prefs.getValue(this@AddAccountActivity, AppConstant.USER_ID, "").toString(),
                Prefs.getValue(this@AddAccountActivity, AppConstant.PASSWORD, "").toString(),
                Prefs.getValue(this@AddAccountActivity, AppConstant.API_KEY, "").toString(),
                category,
            )
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                progressDialog.dismiss()
                if (response.isSuccessful()) {
                    val privacy = response.body().toString().replace("(", "")
                        .replace(")", "")

                    val privacyList: List<String> = privacy.split(",")

                    for (i in 0 until privacyList.size) {
                        if (i == 1) {
                            mainKeyList.add(privacyList.get(i))
                        }
                    }
                    val resultKey = mainKeyList.toString().substringBeforeLast(":[")


                    var key = resultKey.replace("[", "")
                        .replace("\"", "")

                    Log.e(
                        "mainKey",
                        response.body()?.get(key).toString().replace("[", "").replace("]", "")
                            .replace("{", "").replace("}", "")
                    )

                    val fieldName =
                        response.body()?.get(key).toString().replace("[", "").replace("]", "")
                            .replace("{", "").replace("}", "")


                    val fieldList: List<String> = fieldName.split(",")
                    val fieldKey = fieldList.toString().substringAfterLast(":").replace("[", "")
                        .replace("]", "")

                    for (i in 0 until fieldList.size) {
                        var txtKey = fieldList.get(i)
                        fieldKeyList.add(
                            txtKey.toString().substringAfterLast(":").replace("[", "")
                                .replace("]", "").replace("\"", "")
                        )
                    }



                    if(fieldKeyList.size>0){
                        for (i in 0 until fieldKeyList.size) {
                            setDynamicLayout(fieldKeyList.get(i))
                        }
                        ns_main.visible()
                        tv_no_data_add.gone()
                        btn_save.visible()
                    }else{
                        ns_main.gone()
                        tv_no_data_add.visible()
                        btn_save.gone()
                    }





                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                progressDialog.dismiss()
            }
        })
    }

    fun setUpObserver() {

        addPrivacyDataModel.addPrivacyResponse.observe(this, {
            when (it.status) {
                Status.LOADING -> {
                    hideKeyboard()
                    progressDialog!!.show()
                }
                Status.SUCCESS -> {
                    progressDialog!!.dismiss()
                    showToasty(this, it.data?.mesage.toString(), "1")

                    start2(
                        ListingActivity.getStartIntent(
                            this@AddAccountActivity,
                           category
                        )
                    )
                    finish()
                    Log.e("key", it.data?.mesage.toString())
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
                                callAddPrivacyDataApi()
                            }
                        }
                    }

                }
            }
        })


    }

    private fun callAddPrivacyDataApi() {

        val data: MutableMap<String, String> = HashMap()
        data["userid"] = Prefs.getValue(this@AddAccountActivity, AppConstant.USER_ID, "").toString()
        data["pwd"] = Prefs.getValue(this@AddAccountActivity, AppConstant.PASSWORD, "").toString()
        data["key"] = Prefs.getValue(this@AddAccountActivity, AppConstant.API_KEY, "").toString()
        data["typ"] = "privat"
        data["cat"] = category
        data["action"] = "insert"

        for(i in 0 until fieldKeyList.size){
            data[fieldKeyList.get(i)] = paramValueKeyList.get(i).toLowerCase()

        }

        addPrivacyDataModel.addPrivacyData(data)
        setUpObserver()
          /*  val params = mapOf(
                "userid" to "torsten.husmann@web.de",
                "pwd" to "test",
                "key" to Prefs.getValue(this@AddAccountActivity, AppConstant.API_KEY, "").toString(),
                "typ" to "privat",
                "cat" to category,




            )

            addPrivacyDataModel.addPrivacyData(params)
        setUpObserver()*/

    }


}