package com.zb.finpassword.data.models


import com.squareup.moshi.Json
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CategoryResponse(
    @field:Json(name = "familie")
    val familie: Familie? = null,
    @field:Json(name = "privat")
    val privat: Privat? = null,
    @field:Json(name = "status")
    val status: String? = null,
    @field:Json(name = "team")
    val team: Team? = null
) : Parcelable {
    @Parcelize
    data class Familie(
        @field:Json(name = "bankkonto")
        val bankkonto: Int? = null,
        @field:Json(name = "dokument")
        val dokument: Int? = null,
        @field:Json(name = "email")
        val email: Int? = null,
        @field:Json(name = "kreditkarte")
        val kreditkarte: Int? = null,
        @field:Json(name = "login")
        val login: Int? = null,
        @field:Json(name = "notiz")
        val notiz: Int? = null,
        @field:Json(name = "passwort")
        val passwort: Int? = null,
        @field:Json(name = "reisepass")
        val reisepass: Int? = null,
        @field:Json(name = "treueprogramm")
        val treueprogramm: Int? = null,
        @field:Json(name = "wlan")
        val wlan: Int? = null
    ) : Parcelable

    @Parcelize
    data class Privat(
        @field:Json(name = "bankkonto")
        val bankkonto: String? = null,
        @field:Json(name = "dokument")
        val dokument: Int? = null,
        @field:Json(name = "email")
        val email: String? = null,
        @field:Json(name = "kreditkarte")
        val kreditkarte: String? = null,
        @field:Json(name = "login")
        val login: String? = null,
        @field:Json(name = "notiz")
        val notiz: String? = null,
        @field:Json(name = "passwort")
        val passwort: String? = null,
        @field:Json(name = "reisepass")
        val reisepass: String? = null,
        @field:Json(name = "treueprogramm")
        val treueprogramm: String? = null,
        @field:Json(name = "wlan")
        val wlan: String? = null
    ) : Parcelable

    @Parcelize
    data class Team(
        @field:Json(name = "datenbank")
        val datenbank: String? = null,
        @field:Json(name = "dokument")
        val dokument: Int? = null,
        @field:Json(name = "email")
        val email: String? = null,
        @field:Json(name = "login")
        val login: String? = null,
        @field:Json(name = "notiz")
        val notiz: Int? = null,
        @field:Json(name = "passwort")
        val passwort: Int? = null,
        @field:Json(name = "server")
        val server: String? = null,
        @field:Json(name = "softwarelizenz")
        val softwarelizenz: String? = null,
        @field:Json(name = "wlan")
        val wlan: String? = null
    ) : Parcelable
}