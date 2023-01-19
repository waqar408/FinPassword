package com.zb.finpassword.ui.dashboard

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.nosugar.pref.AppConstant
import com.zb.finpassword.R
import com.zb.finpassword.ui.family.FamilyFragment
import com.zb.finpassword.ui.listing.ListingActivity
import com.zb.finpassword.ui.login.LoginActivity
import com.zb.finpassword.ui.privacy.PrivacyFragment
import com.zb.finpassword.ui.setting.SettingFragment
import com.zb.finpassword.ui.team.TeamFragment
import com.zb.finpassword.utils.Prefs
import com.zb.finpassword.utils.setSafeOnClickListener
import com.zb.finpassword.utils.start
import com.zb.finpassword.utils.visible
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.layout_bottom.*

class DashboardActivity : AppCompatActivity() {
    var isShow = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        setUi()
    }

    fun setUi() {
        val b = intent.extras
        if (b != null) {
            isShow = b.getString(IS_SHOW).toString()


        }
        setBottom()
        setFullScreen()
    }

    fun setFullScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
            window.statusBarColor = Color.TRANSPARENT

        }
    }

    companion object {
        const val IS_SHOW = "is_show"

        fun getStartIntent(context: Context, isShow: String): Intent {
            return Intent(context, DashboardActivity::class.java).putExtra(
                IS_SHOW,
                isShow
            )
        }
    }

    fun setBottom() {
        if (isShow.equals("team")) {
            setBottomIcon()
            img_team.setColorFilter(
                ContextCompat.getColor(this, R.color.white),
                android.graphics.PorterDuff.Mode.MULTIPLY
            );
            tv_team.setTextColor(Color.parseColor("#FFFFFF"))
            replaceFragment(TeamFragment())
        } else if (isShow.equals("privacy")) {
            setBottomIcon()
            img_user.setColorFilter(
                ContextCompat.getColor(this, R.color.white),
                android.graphics.PorterDuff.Mode.MULTIPLY
            );
            tv_user.setTextColor(Color.parseColor("#FFFFFF"))

            replaceFragment(PrivacyFragment())
        } else if (isShow.equals("family")) {
            setBottomIcon()
            img_family.setColorFilter(
                ContextCompat.getColor(this, R.color.white),
                android.graphics.PorterDuff.Mode.MULTIPLY
            );
            tv_family.setTextColor(Color.parseColor("#FFFFFF"))
            replaceFragment(FamilyFragment())
        } else {
            replaceFragment(PrivacyFragment())

        }

        ll_user.setSafeOnClickListener {
            setBottomIcon()
            img_user.setColorFilter(
                ContextCompat.getColor(this, R.color.white),
                android.graphics.PorterDuff.Mode.MULTIPLY
            );
            tv_user.setTextColor(Color.parseColor("#FFFFFF"))

            replaceFragment(PrivacyFragment())
        }
        ll_family.setSafeOnClickListener {
            setBottomIcon()
            img_family.setColorFilter(
                ContextCompat.getColor(this, R.color.white),
                android.graphics.PorterDuff.Mode.MULTIPLY
            );
            tv_family.setTextColor(Color.parseColor("#FFFFFF"))
            replaceFragment(FamilyFragment())

        }
        ll_team.setSafeOnClickListener {
            setBottomIcon()
            img_team.setColorFilter(
                ContextCompat.getColor(this, R.color.white),
                android.graphics.PorterDuff.Mode.MULTIPLY
            );
            tv_team.setTextColor(Color.parseColor("#FFFFFF"))
            replaceFragment(TeamFragment())

        }
        ll_setting.setSafeOnClickListener {
            setBottomIcon()
            img_setting.setColorFilter(
                ContextCompat.getColor(this, R.color.white),
                android.graphics.PorterDuff.Mode.MULTIPLY
            );
            tv_setting.setTextColor(Color.parseColor("#FFFFFF"))

            replaceFragment(SettingFragment())
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frmContainer, fragment)
            .commit()
    }

    private fun setBottomIcon() {
        llBottom.visible()
        img_user.setColorFilter(
            ContextCompat.getColor(this, R.color.unselect_color),
            android.graphics.PorterDuff.Mode.MULTIPLY
        );
        img_family.setColorFilter(
            ContextCompat.getColor(this, R.color.unselect_color),
            android.graphics.PorterDuff.Mode.MULTIPLY
        );
        img_team.setColorFilter(
            ContextCompat.getColor(this, R.color.unselect_color),
            android.graphics.PorterDuff.Mode.MULTIPLY
        );
        img_setting.setColorFilter(
            ContextCompat.getColor(this, R.color.unselect_color),
            android.graphics.PorterDuff.Mode.MULTIPLY
        );


        tv_user.setTextColor(Color.parseColor("#CFCECE"))
        tv_family.setTextColor(Color.parseColor("#CFCECE"))
        tv_team.setTextColor(Color.parseColor("#CFCECE"))
        tv_team.setTextColor(Color.parseColor("#CFCECE"))
        tv_setting.setTextColor(Color.parseColor("#CFCECE"))


    }

    override fun onBackPressed() {
        exitDialog()
    }

    private fun exitDialog() {

        val dialog1 = Dialog(this@DashboardActivity)
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
        tvTitle.text = "Are you sure you want to exit"


        tvNo.setOnClickListener(View.OnClickListener {
            dialog1.dismiss()
        })
        tvYes.setOnClickListener(View.OnClickListener {
            dialog1.dismiss()
            finish()
        })
        dialog1.show()

    }

}