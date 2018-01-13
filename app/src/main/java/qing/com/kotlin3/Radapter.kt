package qing.com.kotlin3

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import qing.com.lightdatabinding.LightDataBingding

/**
 * package Kotlin3:qing.com.kotlin3.Radapter.class
 * 作者：zyq on 2017/11/30 14:59
 * 邮箱：zyq@posun.com
 */

class Radapter : RecyclerView.Adapter<Radapter.Holder>() {
    var string_array: Array<String> = arrayOf("How", "Are", "You", "How", "Are", "You", "How", "Are", "You", "How"
            , "Are", "You", "How", "Are", "You", "How", "Are", "You", "How", "Are", "You")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder? {
        var view = LayoutInflater.from(parent!!.context).inflate(R.layout.list_item, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind!!.dataNotify(string_array[position])
    }

    override fun getItemCount(): Int {
        return string_array.size
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var bind: LightDataBingding<String>? = null

        init {
            bind = LightDataBingding<String>()
            bind!!.bind_viewGroup(itemView)
            bind!!.bind_holder(R.id.text_item, { data: String? -> data })
        }

    }
}
