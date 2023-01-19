package com.zb.finpassword.ui.team_list

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.zb.finpassword.R
import com.zb.finpassword.data.models.Test.TeamResponse
import com.zb.finpassword.ui.add_team.AddTeamActivity
import com.zb.finpassword.ui.edit_team.EditTeamActivity
import com.zb.finpassword.utils.gone
import com.zb.finpassword.utils.setSafeOnClickListener
import com.zb.finpassword.utils.start2
import com.zb.finpassword.utils.visible
import kotlinx.android.synthetic.main.row_listing.view.*
import kotlinx.android.synthetic.main.row_team.view.*
import org.json.JSONArray
import org.json.JSONObject


class TeamDataAdapter(private val mActivity: Context, private val mDataList: MutableList<TeamResponse>, var category: String,val subKeyList:ArrayList<String>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val HEADER = 101
    private val ITEM = 102
    private val LOADER = 103
    var teamList = mutableListOf<SwipeTeamModel>()

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, listPosition: Int) {
        when (holder.itemViewType) {
            HEADER -> {
                val mHolder = holder as HeaderViewHolder
                mHolder.headerNameTV.text = mDataList[listPosition].name
                mHolder.mainHeaderLL.isFocusable = false
                mHolder.mainHeaderLL.isEnabled = false
                mHolder.imgAddCategory.setSafeOnClickListener {
                    mActivity.start2(
                        AddTeamActivity.getStartIntent(
                            mActivity,
                            category.toString(),
                            mDataList.get(listPosition).id.toString()
                        )
                    )
                }
                teamList.add(SwipeTeamModel("key", ""))


            }
            ITEM -> {


                val mHolder = holder as ItemViewHolder

                mHolder.itemNameTV.text = mDataList[listPosition].name

                if(listPosition==mDataList.size-1){
                    mHolder.view1.gone()
                }else{
                    mHolder.view1.visible()
                }


                mHolder.imgView.setSafeOnClickListener {
                  val intent = Intent(mActivity, EditTeamActivity::class.java)
                    intent.putExtra("category", category)
                    intent.putExtra("team_data", subKeyList[listPosition].toString())
                    mActivity.startActivity(intent)
                }
            }
        }
    }

    override fun getItemCount() = mDataList.size
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            HEADER -> HeaderViewHolder(inflater.inflate(R.layout.row_team_header, parent, false))
            ITEM -> ItemViewHolder(inflater.inflate(R.layout.row_team_swipe, parent, false))
            else -> ProgressVewHolder(inflater.inflate(R.layout.layout_loading_more, parent, false))
        }
    }


    class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val headerNameTV: AppCompatTextView = view.findViewById(R.id.tv_title_team)
        val mainHeaderLL: LinearLayout = view.findViewById(R.id.mainHeaderLL)
        val imgAddCategory: ImageView = view.findViewById(R.id.img_add_category)

    }

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemNameTV: AppCompatTextView = view.findViewById(R.id.tv_email_type)
        val imgView: ImageView = view.findViewById(R.id.img_view)
        val view1: View = view.findViewById(R.id.view1)
        public var viewBackground: RelativeLayout
        public var viewForeground: RelativeLayout

        init {

            viewBackground = view.findViewById(R.id.view_background)
            viewForeground = view.findViewById(R.id.view_foreground)
        }
    }

    inner class ProgressVewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun getItemViewType(position: Int): Int {
        return when (mDataList[position].type) {
            "header" -> {
                HEADER
            }
            "item" -> {
                ITEM
            }
            else -> {
                LOADER
            }
        }
    }
}