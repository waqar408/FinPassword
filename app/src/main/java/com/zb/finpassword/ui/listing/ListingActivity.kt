package com.zb.finpassword.ui.listing

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.nosugar.pref.AppConstant
import com.zb.finpassword.R
import com.zb.finpassword.Retrofit.RetroClient
import com.zb.finpassword.data.api.ApiService
import com.zb.finpassword.data.models.DeleteResponse
import com.zb.finpassword.data.models.Test.MainResponse
import com.zb.finpassword.data.models.Test.TeamResponse
import com.zb.finpassword.ui.add_account.AddAccountActivity
import com.zb.finpassword.ui.base.BaseActivity
import com.zb.finpassword.ui.dashboard.DashboardActivity
import com.zb.finpassword.ui.team_list.TeamDataAdapter
import com.zb.finpassword.utils.*
import kotlinx.android.synthetic.main.activity_listing.*
import kotlinx.android.synthetic.main.dialog_common.*
import kotlinx.android.synthetic.main.fragment_privacy.*
import kotlinx.android.synthetic.main.toolbar.*
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList


class ListingActivity : BaseActivity(), RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {
    var apiService: ApiService? = null
    var keyList: ArrayList<String> = ArrayList<String>()
    var categoryDetailList: ArrayList<ListingModel> = ArrayList<ListingModel>()
    var category = ""
    lateinit var listingAdapter: SwipeListAdapter
    var privateKeyList: java.util.ArrayList<String> = java.util.ArrayList<String>()
    var privateValueList: java.util.ArrayList<String> = java.util.ArrayList<String>()
    var aList: ArrayList<HashMap<String, String>> = ArrayList<HashMap<String, String>>();
    val teamDataList = mutableListOf<TeamResponse>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listing)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        setUi()
    }

    fun setUi() {
        progressDialog = MyCustomProgressDialog(this@ListingActivity)
        val b = intent.extras
        if (b != null) {
            category = b.getString(CATEGORY).toString()


        }
        setFullScreen()

        tv_title.text = category
        img_plus.visible()
        img_plus.setSafeOnClickListener {
            start2(
                AddAccountActivity.getStartIntent(
                    this@ListingActivity,
                    category
                )
            )
        }
        img_back.setSafeOnClickListener {
            onBackPressed()
        }

        getCategoryData()
        setSwipe()
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
        const val CATEGORY = "category"

        fun getStartIntent(context: Context, category: String): Intent {
            return Intent(context, ListingActivity::class.java).putExtra(
                CATEGORY,
                category
            )
        }
    }

    fun getCategoryData() {
        categoryDetailList.clear()

        progressDialog.show()
        apiService = RetroClient.getApiService()
        val call: Call<JsonObject> =
            apiService!!.getCategoryDetail(
                Prefs.getValue(this@ListingActivity, AppConstant.USER_ID, "").toString(),
                Prefs.getValue(this@ListingActivity, AppConstant.PASSWORD, "").toString(),
                Prefs.getValue(this@ListingActivity, AppConstant.API_KEY, "").toString(),
                "privat",
                category,
                "select"
            )
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                progressDialog.dismiss()
                teamDataList.clear()
                privateKeyList.clear()
                if (response.isSuccessful()) {
                    val results: MutableMap<String, String> = Hashtable()

                    try {
                        //The response is your JSON as a string
                        val obj = JSONObject(response.body().toString())
                        val categories: JSONObject = obj.getJSONObject("entries")
                        val iter = categories.keys()
                        while (iter.hasNext()) {
                            val key = iter.next()
                            results[key] = categories.getString(key + "")
                            keyList.add(key)
                        }

                        Log.d("privacyKey",keyList.toString())
                        try {
                            val iterator: Iterator<*> = categories.keys()
                            while (iterator.hasNext()) {
                                val mainResponse = MainResponse()
                                val key = iterator.next()!!
                                val objSub = JSONObject(categories.get(key.toString()).toString())
                                val sId = if (objSub.has("id")) objSub.get("id").toString() else ""
                                val sName = if (objSub.has("name")) objSub.get("name").toString() else ""
                                teamDataList.add(TeamResponse(sId,"header",sName,""))
                                privateKeyList.add(objSub.toString())

                            }
                        }catch (e:Exception){
                            Log.i("==>APP", "onResponse: "+e.message)
                        }
                        if(teamDataList.size>0){
                            rv_listing.apply {
                                listingAdapter= SwipeListAdapter(this@ListingActivity, teamDataList,category,privateKeyList)
                                adapter = listingAdapter
                            }
                            tv_no_data.gone()
                        }else{
                            tv_no_data.visible()
                        }



                    } catch (e: JSONException) {
                        e.printStackTrace()
                        tv_no_data.visible()
                    }



                    Log.e("categoryDetail1", keyList.toString())


                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                progressDialog.dismiss()
            }
        })
    }

    fun setSwipe() {

        val itemTouchHelperCallback: ItemTouchHelper.SimpleCallback =
            RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this)
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(rv_listing)


        val itemTouchHelperCallback1: ItemTouchHelper.SimpleCallback =
            object : ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT or ItemTouchHelper.UP
            ) {


                override fun onChildDraw(
                    c: Canvas,
                    recyclerView: RecyclerView,
                    viewHolder: ViewHolder,
                    dX: Float,
                    dY: Float,
                    actionState: Int,
                    isCurrentlyActive: Boolean
                ) {
                    super.onChildDraw(
                        c,
                        recyclerView,
                        viewHolder,
                        dX,
                        dY,
                        actionState,
                        isCurrentlyActive
                    )
                }

                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: ViewHolder,
                    target: ViewHolder
                ): Boolean {
                    return false

                }

                override fun onSwiped(viewHolder: ViewHolder, direction: Int) {

                }
            }

        // attaching the touch helper to recycler view

        // attaching the touch helper to recycler view
        ItemTouchHelper(itemTouchHelperCallback1).attachToRecyclerView(rv_listing)
    }

    override fun onSwiped(viewHolder: ViewHolder?, direction: Int, position: Int) {
        if (viewHolder is SwipeListAdapter.MyViewHolder) {

            // get the removed item name to display it in snack bar
            val name: String = teamDataList.get(viewHolder!!.adapterPosition).name

            deleteItem(
                teamDataList.get(viewHolder.adapterPosition).id,
                viewHolder.adapterPosition
            )

            // showing snack bar with Undo option
            val snackbar: Snackbar = Snackbar
                .make(root_layout, "$name removed", Snackbar.LENGTH_LONG)

            snackbar.setActionTextColor(Color.YELLOW)
            snackbar.show()
        }
    }


    fun deleteItem(id: String, pos: Int) {
        listingAdapter.removeItem(pos)
        progressDialog.show()
        apiService = RetroClient.getApiService()
        val call: Call<DeleteResponse> =
            apiService!!.deleteItem(
                Prefs.getValue(this@ListingActivity, AppConstant.USER_ID, "").toString(),
                Prefs.getValue(this@ListingActivity, AppConstant.PASSWORD, "").toString(),
                Prefs.getValue(this@ListingActivity, AppConstant.API_KEY, "").toString(),
                "privat",
                category,
                "delete",
                id
            )
        call.enqueue(object : Callback<DeleteResponse> {
            override fun onResponse(
                call: Call<DeleteResponse>,
                response: Response<DeleteResponse>
            ) {
                progressDialog.dismiss()
                showToasty(this@ListingActivity, response.body()?.mesage.toString(), "1")
                Log.e("errorCode", response.body()?.mesage.toString())

                if (response.isSuccessful()) {
                    showToasty(this@ListingActivity, response.body()?.mesage.toString(), "1")
                    Log.e("errorCode", response.body()?.mesage.toString())
                    getCategoryData()
                } else {
                    showToasty(this@ListingActivity, response.body()?.code.toString(), "1")
                    Log.e("errorCode", response.body()?.code.toString())
                }
            }

            override fun onFailure(call: Call<DeleteResponse>, t: Throwable) {
                showToasty(this@ListingActivity, t.message.toString(), "1")
                Log.e("errorCode", t.message.toString())

                progressDialog.dismiss()
            }
        })
    }

    override fun onBackPressed() {
        start2(
            DashboardActivity.getStartIntent(
                this@ListingActivity,
                "privacy"
            )
        )
        finish()
    }

}