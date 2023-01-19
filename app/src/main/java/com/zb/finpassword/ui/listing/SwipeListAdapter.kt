package com.zb.finpassword.ui.listing

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.zb.finpassword.R
import com.zb.finpassword.data.models.Test.TeamResponse
import com.zb.finpassword.ui.edit_privacy.EditPrivacyDataActivity
import com.zb.finpassword.utils.gone
import com.zb.finpassword.utils.setSafeOnClickListener
import com.zb.finpassword.utils.visible
import kotlinx.android.synthetic.main.row_listing.view.*


class SwipeListAdapter(
    private val context: Context,
    mModel: MutableList<TeamResponse>,
    var category: String,
    val keyList:ArrayList<String>

) :
    RecyclerView.Adapter<SwipeListAdapter.MyViewHolder?>() {
    private val mModel: MutableList<TeamResponse>

    var idList: java.util.ArrayList<String> = java.util.ArrayList<String>()
    var paramKeyList: java.util.ArrayList<String> = java.util.ArrayList<String>()
    var paramValueList: java.util.ArrayList<String> = java.util.ArrayList<String>()

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {


        public var viewBackground: RelativeLayout
        public var viewForeground: RelativeLayout

        init {

            viewBackground = view.findViewById(R.id.view_background)
            viewForeground = view.findViewById(R.id.view_foreground)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_swipe, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(
        holder: MyViewHolder,
        position: Int
    ) {

        val item: TeamResponse = mModel[position]
        holder.itemView.run {

            tv_email_type.text = item.name.toString()

            if(position == mModel.size-1){
                view1.gone()
            }else{
                view1.visible()
            }
            img_view.setSafeOnClickListener {


                val intent = Intent(context, EditPrivacyDataActivity::class.java)
                intent.putExtra("key", keyList.get(position).toString())
                intent.putExtra("category", category)
                context.startActivity(intent)


            }





        }
    }

    override fun getItemCount(): Int {
        return mModel.size
    }

    fun removeItem(position: Int) {
        mModel.removeAt(position)
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position)
    }


    init {
        this.mModel = mModel
    }
}