package com.zb.finpassword.ui.setting

import android.app.Dialog
import android.os.Bundle
import android.view.*
import android.widget.TextView
import com.nosugar.pref.AppConstant
import com.nosugar.ui.base.BaseFragment
import com.zb.finpassword.R
import com.zb.finpassword.ui.change_pwd.ChangePwdActivity
import com.zb.finpassword.ui.edit_profile.EditProfileActivity
import com.zb.finpassword.ui.login.LoginActivity
import com.zb.finpassword.utils.Prefs
import com.zb.finpassword.utils.gone
import com.zb.finpassword.utils.setSafeOnClickListener
import com.zb.finpassword.utils.start
import kotlinx.android.synthetic.main.fragment_setting.*
import kotlinx.android.synthetic.main.toolbar.*
import java.util.regex.Matcher
import java.util.regex.Pattern

class SettingFragment : BaseFragment(R.layout.fragment_setting) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUi()
    }

    fun setUi(){

        if(isEmailValid(Prefs.getValue(requireActivity(), AppConstant.USER_ID, "").toString())){
            tv_email_setting.text=Prefs.getValue(requireActivity(), AppConstant.USER_ID, "").toString()
            tv_user_name_setting.gone()
            tv_user_name.gone()
            view_user_name.gone()
        }else{
            tv_user_name_setting.text=Prefs.getValue(requireActivity(), AppConstant.USER_ID, "").toString()
            tv_email_setting.gone()
            tv_email.gone()
            view_email.gone()
            view_company.gone()
        }
        ll_change_pwd.setSafeOnClickListener {
            requireActivity().start<ChangePwdActivity>()
        }
        tv_logout.setSafeOnClickListener {
            logoutDialog()
        }
        tv_edit_profile.setSafeOnClickListener {
            requireActivity().start<EditProfileActivity>()
        }
        tv_title.text=getString(R.string.setting)
        img_back.gone()
    }
    private fun logoutDialog() {

        val dialog1 = Dialog(requireActivity())
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog1.getWindow()!!.setBackgroundDrawableResource(android.R.color.transparent)

        dialog1.setCancelable(false)
        dialog1.setContentView(R.layout.dialog_exit)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog1.window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        lp.gravity = Gravity.CENTER
        dialog1.window!!.attributes = lp
        val tvNo = dialog1.findViewById(R.id.tvNo) as TextView
        val tvYes = dialog1.findViewById(R.id.tvYes) as TextView
        val tvTitle = dialog1.findViewById(R.id.tv_title) as TextView



        tvNo.setOnClickListener(View.OnClickListener {
            dialog1.dismiss()
        })
        tvYes.setOnClickListener(View.OnClickListener {
            dialog1.dismiss()
            Prefs.removeValue(context =requireActivity(), key = AppConstant.API_KEY)
            Prefs.clearAllData(requireActivity())
            requireActivity().start<LoginActivity>(isFinish = true)
            requireActivity().finish()
        })
        dialog1.show()

    }
    fun isEmailValid(email: String?): Boolean {
        val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
        val pattern: Pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher: Matcher = pattern.matcher(email)
        return matcher.matches()
    }
}