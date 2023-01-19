package com.zb.finpassword.ui.listing


import android.R.attr.data
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
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
import kotlinx.android.synthetic.main.row_home.view.*
import kotlinx.android.synthetic.main.row_listing.view.*
import kotlinx.android.synthetic.main.row_listing.view.view1


class ListingAdapter(
    private val mActivity: Context,
    val mModel: ArrayList<TeamResponse>,
    var category: String
) :
    RecyclerView.Adapter<ListingAdapter.MyViewHolder>() {


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_listing, parent, false)
        return MyViewHolder(view)
    }

    fun removeItem(position: Int) {
        mModel.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun onBindViewHolder(
        holder: MyViewHolder,
        @SuppressLint("RecyclerView") listPosition: Int
    ) {
        holder.itemView.run {

            tv_email_type.text = mModel.get(listPosition).name.toString()
            if(listPosition == mModel.size-1){
                view1.gone()
            }else{
                view1.visible()
            }
            img_view.setSafeOnClickListener {
                /*val intent = Intent(mActivity, EditPrivacyDataActivity::class.java)
                intent.putExtra("privatData", mModel[listPosition])
                intent.putExtra("category", category)

                mActivity.startActivity(intent)*/
            }

        }

    }

    override fun getItemCount(): Int {
        return mModel.size
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