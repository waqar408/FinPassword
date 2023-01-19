package com.zb.finpassword.data.models


import com.squareup.moshi.Json
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class KeyResponse(
    @field:Json(name = "key")
    val key: String? = null,
    @field:Json(name = "status")
    val status: String? = null
) : Parcelable