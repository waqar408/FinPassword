package com.zb.finpassword.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


 class ListResponse {
    @SerializedName("code")
    @Expose
    var code1: String? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("entries")
    var categories: Map<String, String>? = null
}