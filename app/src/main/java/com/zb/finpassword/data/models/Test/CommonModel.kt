package com.zb.finpassword.data.models.Test

import androidx.annotation.Keep
import com.google.gson.JsonObject
import org.json.JSONObject
import java.io.Serializable
import java.util.*


data class TeamResponse(
    var id: String = "",
    var type: String = "",
    var name: String = "",
    var teamId: String = "",

    )




@Keep
class MainResponse {
    var id: String = ""
    var userid: String = ""
    var name: String = ""
    var typ: String = ""
    var active: String = ""
    var entries: HashMap<String, Entries>? = null
}

@Keep
data class Entries(
    var server: String,
    var art: String,
    var passwort: String,
    var notiz: String,
    var datenbank: String,
    var typ: String,
    var version: String,
    var sid: String,
    var port: String,
    var name: String,
    var benutzer: String,
    var id: String,
    var teamid: String,
) : Serializable


