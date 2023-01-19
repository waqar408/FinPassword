package com.zb.finpassword.ui.privacy




import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zb.finpassword.R
import com.zb.finpassword.ui.family_listing.FamilyListActivity
import com.zb.finpassword.ui.listing.ListingActivity
import com.zb.finpassword.ui.team_list.TeamDataActivity
import com.zb.finpassword.utils.*
import com.zb.finpassword.utils.visible

import kotlinx.android.synthetic.main.row_home.view.*


class HomeAdapter(
    private val mActivity: Context,
    val privacyValueList:ArrayList<String>,
    val privacyKeyList:ArrayList<String>,
    var type:String

) :
    RecyclerView.Adapter<HomeAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_home, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: MyViewHolder,
        @SuppressLint("RecyclerView") listPosition: Int
    ) {
        holder.itemView.run {
            tv_title.text=privacyKeyList.get(listPosition)
            tv_no.text=privacyValueList.get(listPosition)

            cs_main.setSafeOnClickListener {
                if(type.equals("private")){
                    mActivity.start2(
                        ListingActivity.getStartIntent(
                           mActivity,
                            privacyKeyList.get(listPosition)
                        )
                    )
                }else if(type.equals("team")){
                    mActivity.start2(
                        TeamDataActivity.getStartIntent(
                            mActivity,
                            privacyKeyList.get(listPosition)
                        )
                    )
                }else if(type.equals("family")){
                    mActivity.start2(
                        FamilyListActivity.getStartIntent(
                            mActivity,
                            privacyKeyList.get(listPosition)
                        )
                    )
                }else{

                }
            }
            if(listPosition == privacyKeyList.size-1){
                view1.gone()
            }else{
                view1.visible()
            }

        }

    }

    override fun getItemCount(): Int {
        return privacyKeyList.size
    }

    /* fun addAll(mData: ArrayList<NoticeModel.Datum>?) {
         mModel.addAll(mData!!)
         notifyItemInserted(mModel.size - 1)
         notifyDataSetChanged()
     }

     fun clear() {
         mModel.clear()
         notifyDataSetChanged()
     }*/
}