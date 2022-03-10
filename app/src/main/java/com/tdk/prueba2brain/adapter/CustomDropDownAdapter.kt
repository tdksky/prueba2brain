package com.tdk.prueba2brain.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.tdk.prueba2brain.R


class CustomDropDownAdapter(val context: Context, var items: List<String>) : BaseAdapter(){

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View{

        val view: View
        val vh: ItemHolder
        if (convertView == null){
            view = inflater.inflate(R.layout.custom_item_spinner, parent, false)
            vh = ItemHolder(view)
            view?.tag = vh
        } else{
            view = convertView
            vh = view.tag as ItemHolder
        }
        vh.label.text = items[position]

        return view
    }

    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(position: Int): Any {
        return items[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong();
    }
    private class ItemHolder(row: View?) {
        val label: TextView = row?.findViewById(R.id.textViewPrice) as TextView

    }
}