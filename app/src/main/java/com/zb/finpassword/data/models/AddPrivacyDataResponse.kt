package com.zb.finpassword.data.models


import com.squareup.moshi.Json
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AddPrivacyDataResponse(
    @field:Json(name = "mesage")
    val mesage: String? = null
) : Parcelable