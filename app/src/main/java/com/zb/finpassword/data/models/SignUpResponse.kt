package com.zb.finpassword.data.models


import com.squareup.moshi.Json
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SignUpResponse(
    @field:Json(name = "message")
    val message: String? = null,
    @field:Json(name = "status")
    val status: String? = null
) : Parcelable