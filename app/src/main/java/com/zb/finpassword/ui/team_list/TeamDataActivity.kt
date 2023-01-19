package com.zb.finpassword.ui.team_list

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
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.gson.JsonObject
import com.nosugar.pref.AppConstant
import com.zb.finpassword.R
import com.zb.finpassword.Retrofit.RetroClient
import com.zb.finpassword.data.api.ApiService
import com.zb.finpassword.data.models.DeleteResponse
import com.zb.finpassword.data.models.Test.MainResponse
import com.zb.finpassword.data.models.Test.TeamResponse
import com.zb.finpassword.ui.base.BaseActivity
import com.zb.finpassword.ui.dashboard.DashboardActivity
import com.zb.finpassword.ui.listing.SwipeListAdapter
import com.zb.finpassword.utils.*
import kotlinx.android.synthetic.main.activity_listing.*
import kotlinx.android.synthetic.main.toolbar.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class TeamDataActivity : BaseActivity() ,OnTeamDeleteClick, RecyclerItemTouchHelper1.RecyclerItemTouchHelperListener{
    var apiService: ApiService? = null
    var keyList: ArrayList<String> = ArrayList<String>()
    var mainKeyList: ArrayList<String> = ArrayList<String>()
    var category = ""
    val teamDataList = mutableListOf<TeamResponse>()
    var teamAdapter: TeamDataAdapter?=null
    var teamList = mutableListOf<SwipeTeamModel>()
    var subKeyList: ArrayList<String> = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listing)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        setUi()
    }

    companion object {
        const val CATEGORY = "category"

        fun getStartIntent(context: Context, category: String): Intent {
            return Intent(context, TeamDataActivity::class.java).putExtra(
                CATEGORY,
                category
            )
        }
    }

    fun setUi() {
        val b = intent.extras
        if (b != null) {
            category = b.getString(CATEGORY).toString()


        }
        progressDialog = MyCustomProgressDialog(this@TeamDataActivity)

        setFullScreen()

        tv_title.text = category
        /*   img_plus.visible()
           img_plus.setSafeOnClickListener {
               start<AddAccountActivity>()
           }*/
        img_back.setSafeOnClickListener {
            onBackPressed()
        }

        getCategoryData()

    }

    fun setFullScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
            window.statusBarColor = Color.TRANSPARENT

        }
    }


    fun getCategoryData() {
        progressDialog.show()
        apiService = RetroClient.getApiService()
        val call: Call<JsonObject> =
            apiService!!.getCategoryDetail(
                Prefs.getValue(this@TeamDataActivity, AppConstant.USER_ID, "").toString(),
                Prefs.getValue(this@TeamDataActivity, AppConstant.PASSWORD, "").toString(),
                Prefs.getValue(this@TeamDataActivity, AppConstant.API_KEY, "").toString(),
                "team",
                category,
                "select"
            )
        call.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                progressDialog.dismiss()
                subKeyList.clear()
                teamDataList.clear()
                keyList.clear()
                if (response.isSuccessful()) {
                    try{
                        val results: MutableMap<String, String> = Hashtable()
                        val mainArray = JSONArray()

                        //Main Array Key
                        val obj = JSONObject(response.body().toString())
                        val teams: JSONObject = obj.getJSONObject("teams")
                        val iter = teams.keys()
                        while (iter.hasNext()) {
                            val key = iter.next()
                            results[key] = teams.getString(key + "")
                            keyList.add(key)
                        }


                        for (i in 0 until keyList.size) {
                            val mainObjectKey = JSONObject()
                            val teamKey = obj.getJSONObject("teams").get(keyList.get(i)) as JSONObject
                            mainObjectKey.put("entries", teamKey)
                            mainArray.put(mainObjectKey)
                        }

                        try {
                            val iterator: Iterator<*> = teams.keys()
                            while (iterator.hasNext()) {
                                val mainResponse = MainResponse()
                                val key = iterator.next()!!
                                val objSub = JSONObject(teams.get(key.toString()).toString())
                                val sId = if (objSub.has("id")) objSub.get("id").toString() else ""
                                val sName = if (objSub.has("name")) objSub.get("name").toString() else ""
                                teamDataList.add(TeamResponse(sId,"header",sName,""))

                                subKeyList.add("")
                                if (objSub.has("entries")){


                                    try{
                                        val entries: JSONObject = objSub.getJSONObject("entries")
                                        val iteratorSub: Iterator<*> = entries.keys()
                                        while (iteratorSub.hasNext()) {
                                            val keySub = iteratorSub.next()!!
                                            val objSubEntries = JSONObject(entries.get(keySub.toString()).toString())
                                            val name = if (objSubEntries.has("name")) objSubEntries.get("name").toString() else ""
                                            val id = if (objSubEntries.has("id")) objSubEntries.get("id").toString() else ""
                                            val teamid = if (objSubEntries.has("teamid")) objSubEntries.get("teamid").toString() else ""
                                            teamDataList.add(TeamResponse(id,"item",name,teamid))
                                            subKeyList.add(objSubEntries.toString())

                                        }
                                    }catch (e:java.lang.Exception){

                                    }
                                }



                                Log.i("==>APP", "onResponse: "+subKeyList.toString())


                            }
                        }catch (e:Exception){
                            Log.i("==>APP", "onResponse: "+e.message)
                        }



                        if(teamDataList.size>0){
                            rv_listing.apply {
                                teamAdapter= TeamDataAdapter(this@TeamDataActivity, teamDataList,category,subKeyList)
                                adapter = teamAdapter
                            }
                            setSwipe()
                            tv_no_data.gone()
                        }else{
                            tv_no_data.visible()
                        }




                    }catch (e:Exception){
tv_no_data.visible()
                    }



                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                progressDialog.dismiss()
            }
        })
    }

    override fun onBackPressed() {
        start2(
            DashboardActivity.getStartIntent(
                this@TeamDataActivity,
                "team"
            )
        )
        finish()
    }

    override fun onDeleteClick(listPosition: Int, teamObj: SwipeTeamModel) {
        var jsonData =JSONObject(teamObj.value.toString())
        val id= jsonData.get("id").toString()
        Log.d("delId", "onDeleteClick: "+id)
    }



    fun deleteItem(id: String, pos: Int,teamId:String) {
        teamDataList.removeAt(pos)
        Log.d("LOG:-- ", "" + id.toString())
        Log.d("LOG:-- ", "" + teamId.toString())

        rv_listing.adapter?.notifyDataSetChanged()
        progressDialog.show()
        apiService = RetroClient.getApiService()
        val call: Call<DeleteResponse> =
            apiService!!.deleteTeamItem(
                Prefs.getValue(this@TeamDataActivity, AppConstant.USER_ID, "").toString(),
                Prefs.getValue(this@TeamDataActivity, AppConstant.PASSWORD, "").toString(),
                Prefs.getValue(this@TeamDataActivity, AppConstant.API_KEY, "").toString(),
                "team",
                category,
                "delete",
                id,
                teamId
            )
        call.enqueue(object : Callback<DeleteResponse> {
            override fun onResponse(
                call: Call<DeleteResponse>,
                response: Response<DeleteResponse>
            ) {
                progressDialog.dismiss()

                Log.e("errorCode", response.body()?.mesage.toString())

                if (response.isSuccessful()) {
                    showToasty(this@TeamDataActivity, response.body()?.mesage.toString(), "1")
                    Log.e("errorCode", response.body()?.mesage.toString())
                    getCategoryData()
                } else {
                    showToasty(this@TeamDataActivity, response.body()?.code.toString(), "1")
                    Log.e("errorCode", response.body()?.code.toString())
                }
            }

            override fun onFailure(call: Call<DeleteResponse>, t: Throwable) {
                showToasty(this@TeamDataActivity, t.message.toString(), "1")
                Log.e("errorCode", t.message.toString())

                progressDialog.dismiss()
            }
        })
    }

    fun setSwipe() {

        val itemTouchHelperCallback: ItemTouchHelper.SimpleCallback =
            RecyclerItemTouchHelper1(0, ItemTouchHelper.LEFT, this)
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(rv_listing)


        val itemTouchHelperCallback1: ItemTouchHelper.SimpleCallback =
            object : ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT or ItemTouchHelper.UP
            ) {


                override fun onChildDraw(
                    c: Canvas,
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
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
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false

                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                }
            }

        // attaching the touch helper to recycler view

        // attaching the touch helper to recycler view
        ItemTouchHelper(itemTouchHelperCallback1).attachToRecyclerView(rv_listing)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int, position: Int) {
        if (viewHolder is TeamDataAdapter.ItemViewHolder) {

            // get the removed item name to display it in snack bar
            val name: String = teamDataList.get(viewHolder!!.adapterPosition).name

            try {
                val position = viewHolder.adapterPosition
                Log.i("==>APP", "onSwiped: "+teamDataList[position].id)
                deleteItem(teamDataList[position].id,position,teamDataList[position].teamId)
            } catch (e: Exception) {
                Log.i("LOG:-- ", "" + e.message)
            }

            // showing snack bar with Undo option
            val snackbar: Snackbar = Snackbar
                .make(root_layout, "$name removed", Snackbar.LENGTH_LONG)

            snackbar.setActionTextColor(Color.YELLOW)
            snackbar.show()
        }
    }

}
