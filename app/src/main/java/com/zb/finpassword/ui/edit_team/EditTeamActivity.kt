package com.zb.finpassword.ui.edit_team

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
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.nosugar.pref.AppConstant
import com.zb.deepstudy.enums.Status
import com.zb.finpassword.R
import com.zb.finpassword.ui.add_account.ViewModel.AddPrivacyViewModel
import com.zb.finpassword.ui.base.BaseActivity
import com.zb.finpassword.ui.listing.ListingModel
import com.zb.finpassword.ui.team_list.SwipeTeamModel
import com.zb.finpassword.ui.team_list.TeamDataActivity
import com.zb.finpassword.utils.*
import kotlinx.android.synthetic.main.activity_add_account.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.dialog_common.*
import kotlinx.android.synthetic.main.toolbar.*
import org.json.JSONObject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

class EditTeamActivity : BaseActivity() {
    var keyList: ArrayList<String> = ArrayList<String>()
    var valueList: ArrayList<String> = ArrayList<String>()
    var i = 0
    private val editTextList: ArrayList<EditText> = ArrayList()
    var list = ArrayList<String>()
    var category = ""
    var id = ""
    var paramValueKeyList: ArrayList<String> = ArrayList<String>()
    var fieldId = ""
    var teamId = ""
    private val addPrivacyDataModel: AddPrivacyViewModel by viewModel()
    var teamKeyList: ArrayList<String>? = ArrayList<String>()
    var teamValueList: ArrayList<String>? = ArrayList<String>()
    var teamData = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_account)
        setUi()
    }

    companion object {
        const val CATEGORY = "category"
        const val TEAM_ID = "team_id"
        const val FIELD_ID = "field_id"

        fun getStartIntent(
            context: Context,
            category: String,
            teamId: String,
            fieldId: String
        ): Intent {
            return Intent(context, EditTeamActivity::class.java).putExtra(
                CATEGORY,
                category
            ).putExtra(
                TEAM_ID,
                teamId
            ).putExtra(
                FIELD_ID,
                fieldId
            )
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

    fun setUi() {
        setData()
        progressDialog = MyCustomProgressDialog(this)
        btn_save.visible()

        val b = intent.extras
        if (b != null) {
            category = b.getString(CATEGORY).toString()


        }
        setFullScreen()
        tv_title.text = "Edit " + category
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

            val keyValue = arrayData.toString().replace("[", "")
                .replace("]", "").replace("\"", "")

            val valueKeyList: List<String> = keyValue.split(",")
            Log.e("fieldKey", valueKeyList.toString())

            for (i in 0 until teamKeyList!!.size) {
                paramValueKeyList.add(valueKeyList.get(i))
            }

            var isValid=true
            var errorTxt=""
            var pos=0

            for (i in 0 until teamKeyList!!.size) {
                if(teamKeyList!!.get(i).equals("email")){
                    pos=i
                    break
                }
            }
            for (i in 0 until teamKeyList!!.size) {
                paramValueKeyList.add(valueKeyList.get(i))

                if (valueKeyList.get(i).toString().equals("")) {
                    isValid=false
                    errorTxt=teamKeyList!!.get(i).toString()

                    break
                }

            }
            if(isValid==false){
                showToasty(this, "Please enter valid " + errorTxt, "2")

            }else{
                if(teamKeyList!!.contains("email")){
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
            for (i in 0 until teamKeyList!!.size) {
                paramValueKeyList.add(valueKeyList.get(i))
                if (valueKeyList.get(i).toString().equals("")) {
                    showToasty(this, "Please enter " + keyList.get(i), "2")
                    break
                } else {
                    if (teamKeyList!!.get(i).toString().equals("email")) {

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
                  */
/* callAddPrivacyDataApi()
                    break*//*


                }
            }
*/

        }
    }
    fun isEmailValid(email: String?): Boolean {
        val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
        val pattern: Pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher: Matcher = pattern.matcher(email)
        return matcher.matches()
    }

    fun setData() {

        teamData = intent.getStringExtra("team_data").toString()
        Log.d("fieldKey", "setData: "+teamData.toString())

        val jsonObject = JSONObject(teamData.toString())
        val iterator: Iterator<*> = jsonObject.keys()
        while (iterator.hasNext()) {
            val key = iterator.next()!!
            val value: Any = jsonObject.get(key.toString())
            if (key.toString().equals("teamid")) {
                teamId = value.toString()
            } else if (key.toString().equals("id")) {
                fieldId = value.toString()
            }
            if (!key.toString().equals("id") && !key.toString().equals("version") && !key.toString().equals("typ") && !key.toString().equals("teamid")) {
                setDynamicLayout(key.toString(), value.toString())
                teamKeyList!!.add(key.toString())

            }
        }

    }

    fun setDynamicLayout(name: String, value: String) {
        if (!name.equals("id") && !name.equals("teamid")) {

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
                editText.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            } else if (name.equals("email")) {

                editText.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            } else {
                editText.inputType =
                     InputType.TYPE_TEXT_FLAG_CAP_SENTENCES

            }
            try{

            }catch (e:Exception){

            }

            editText.setText(value)

            editTextList.add(editText)
            ll_edit_text?.addView(tv1)
            ll_edit_text?.addView(editText)
        }

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
                        TeamDataActivity.getStartIntent(
                            this@EditTeamActivity,
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
        data["userid"] = Prefs.getValue(this@EditTeamActivity, AppConstant.USER_ID, "").toString()
        data["pwd"] = Prefs.getValue(this@EditTeamActivity, AppConstant.PASSWORD, "").toString()
        data["key"] = Prefs.getValue(this@EditTeamActivity, AppConstant.API_KEY, "").toString()
        data["typ"] = "team"
        data["cat"] = category
        data["action"] = "update"
        data["version"] = "3.2"
        data["teamid"] = teamId
        data["id"] = fieldId

        for (i in 0 until teamKeyList!!.size) {
            data[teamKeyList!!.get(i)] = paramValueKeyList.get(i).toLowerCase()

        }

        addPrivacyDataModel.addPrivacyData(data)
        setUpObserver()

    }

    override fun onBackPressed() {

        start2(
            TeamDataActivity.getStartIntent(
                this@EditTeamActivity,
                category
            )
        )
        finish()
    }
}