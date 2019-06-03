package com.borges.minesweeper

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.GridView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"

    private val grid = Grid(width = 10, height = 10, bombCount = 20)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun cellClick(view: View) {
        val cell = getCell(resources.getResourceName(view.id))
        clickCell(cell, view as Button)
    }

    private fun getCell(resourceName: String): Pair<Int, Int> {
        val location = resourceName.substring(resourceName.length - 3)
        return Pair(location[0].toInt(), location[2].toInt())
    }

    private fun clickCell(cell: Pair<Int, Int>, button: Button) {

        button.text = grid.cellValue(cell).toString()
        button.setBackgroundColor(Color.BLUE)

    }
}
