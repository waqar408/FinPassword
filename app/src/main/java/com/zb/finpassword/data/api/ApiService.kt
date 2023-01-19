package com.zb.finpassword.data.api

import com.google.gson.JsonObject
import com.zb.finpassword.data.models.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*


interface ApiService {

    @GET("api/key.php")
    suspend fun getKey(
        @QueryMap params: Map<String, String>
    ): Response<KeyResponse>

    @GET("api/login.php")
    suspend fun login(
        @QueryMap params: Map<String, String>
    ): Response<LoginResponse>

    @GET("api/category.php")
    suspend fun getCategory(
        @QueryMap params: Map<String, String>
    ): Response<CategoryResponse>


    @GET("api/password.php")
    fun getCategoryDetail(
        @Query("userid") userid: String,
        @Query("pwd") pwd: String,
        @Query("key") key: String,
        @Query("typ") typ: String,
        @Query("cat") cat: String,
        @Query("action") action: String
    ): Call<JsonObject>


    @FormUrlEncoded
    @POST("api/register.php")
    suspend fun signup(
        @FieldMap params: Map<String, String>
    ): Response<SignUpResponse>

    @GET("api/fields.php")
    fun getField(
        @Query("userid") userid: String,
        @Query("pwd") pwd: String,
        @Query("key") key: String,
        @Query("cat") cat: String,
    ): Call<JsonObject>

    @FormUrlEncoded
    @POST("api/password.php")
    suspend fun addPrivateData(
        @FieldMap params: Map<String, String>
    ): Response<AddPrivacyDataResponse>

    @DELETE("api/password.php")
    fun deleteItem(
        @Query("userid") userid: String,
        @Query("pwd") pwd: String,
        @Query("key") key: String,
        @Query("typ") typ: String,
        @Query("cat") cat: String,
        @Query("action") action: String,
        @Query("id") id: String
    ): Call<DeleteResponse>

    @DELETE("api/password.php")
    fun deleteTeamItem(
        @Query("userid") userid: String,
        @Query("pwd") pwd: String,
        @Query("key") key: String,
        @Query("typ") typ: String,
        @Query("cat") cat: String,
        @Query("action") action: String,
        @Query("id") id: String,
        @Query("teamid") teamid: String

    ): Call<DeleteResponse>


}