package com.zb.finpassword.utils

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Parcelable
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import es.dmoral.toasty.Toasty
import jp.wasabeef.blurry.Blurry
import java.io.Serializable


internal fun Context.showDialogN(layoutResourceId: Int, dialogBuilder: Dialog.() -> Unit) {
    Dialog(this).apply {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setCancelable(false)
        setContentView(layoutResourceId)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setWidth(0.8f)
        dialogBuilder()
        show()
    }
}


inline fun <T : Any, R> T?.ifNotNullOrElse(ifNotNullPath: (T) -> R, elsePath: () -> R) =
    let { if (it == null) elsePath() else ifNotNullPath(it) }

fun View.asString(): String {
    return when (this) {
        is AppCompatEditText -> text.toString().trim().ifNotNullOrElse({ it }, { "" })
        is AppCompatTextView -> text.toString().trim().ifNotNullOrElse({ it }, { "" })
        is AutoCompleteTextView -> text.toString().trim().ifNotNullOrElse({ it }, { "" })
        is MaterialButton -> text.toString().trim().ifNotNullOrElse({ it }, { "" })
        is MaterialTextView -> text.toString().trim().ifNotNullOrElse({ it }, { "" })
        else -> ""
    }
}
fun Activity.hideKeyboard() {
    val imm = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    //Find the currently focused view, so we can grab the correct window token from it.
    var view = this.currentFocus
    //If no view currently has focus, create a new one, just so we can grab a window token from it
    if (view == null) {
        view = View(this)
    }
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}
fun showToasty(context: Context, msg: String, type: String) {
    when (type) {
        "1" -> {
            Toasty.success(context, msg, Toast.LENGTH_SHORT, true).show()
        }
        "2" -> {
            Toasty.error(context, msg, Toast.LENGTH_SHORT, true).show()
        }
        "3" -> {
            Toasty.info(context, msg, Toast.LENGTH_SHORT, true).show()
        }
        else -> {
            Toasty.normal(context, msg).show()
        }
    }
}
fun ShowToast(response: String, activity: Context, isError: Boolean) {
    if (isError) {
        Toasty.error(activity, response).show()
    } else {
        Toasty.success(activity, response).show()
    }
}
fun Context.getStr(@StringRes id: Int) = resources.getString(id)
fun getStr1(mContext: Context, id: Int): String {
    return mContext.resources.getString(id)
}
inline fun <reified T : Activity> Context.start(
    vararg params: Pair<String, Any?>,
    isFinish: Boolean = false,
    requestCode: Int? = null
) {
    this as Activity
    val intent = Intent(this, T::class.java).apply {
        params.forEach {
            when (val value = it.second) {
                is Int -> putExtra(it.first, value)
                is String -> putExtra(it.first, value)
                is Double -> putExtra(it.first, value)
                is Float -> putExtra(it.first, value)
                is Boolean -> putExtra(it.first, value)
                is Serializable -> putExtra(it.first, value)
                is Parcelable -> putExtra(it.first, value)
                else -> throw IllegalArgumentException("Wrong param type!")
            }
            return@forEach
        }
    }
    if (requestCode != null) startActivityForResult(intent, requestCode) else startActivity(intent)
    if (isFinish) finish()
}
fun Context.start2(
    intent: Intent,
    isFinish: Boolean = false,
    bundle: Bundle? = null
) {
    this as AppCompatActivity
    startActivity(intent, bundle)
    if (isFinish) finish()
}
internal fun Dialog.setWidth(width: Float) {
    this.window?.setLayout(
            // (Resources.getSystem().displayMetrics.widthPixels * width).toInt(),
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
    )
}

internal fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.setVisible(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.GONE
}

fun View.setInVisible(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.INVISIBLE
}

/**
 * Click listener to stop multi click on view
 * */
fun View.setSafeOnClickListener(onSafeClick: (View) -> Unit) {
    val safeClickListener = SafeClickListener {
        onSafeClick(it)
    }
    setOnClickListener(safeClickListener)
}

internal fun Context.setBlurView(view: ConstraintLayout) {
    Blurry.with(this).radius(10).sampling(2).onto(view)
}

