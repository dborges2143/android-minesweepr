package com.borges.minesweeper

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button

class MainAdapter(private val context: Context, private val numbers: List<String>) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val view = convertView ?: inflater.inflate(R.layout.row_item, null, false)

        val button = view?.findViewById<Button>(R.id.button_view)
        button?.text = numbers[position]

        return view
    }

    override fun getItem(position: Int): Any {
        return numbers[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return numbers.size
    }
}
