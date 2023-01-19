package com.zb.finpassword.data.models


import com.squareup.moshi.Json
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DeleteResponse(
    @field:Json(name = "code")
    val code: String? = null,
    @field:Json(name = "deleted")
    val deleted: String? = null,
    @field:Json(name = "mesage")
    val mesage: String? = null
) : Parcelable