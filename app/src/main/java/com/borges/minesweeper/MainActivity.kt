package com.borges.minesweeper

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"

    private val grid = Grid(width = 10, height = 10, bombCount = 20)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button_0_0.setOnLongClickListener { view ->
            val background = view.background as ColorDrawable

            if (!grid.cell(Pair(0, 0)).isRevealed) {
                if (background.color == Color.RED)
                    view.setBackgroundColor(Color.CYAN)
                else
                    view.setBackgroundColor(Color.RED)
            }

            true
        }

    }

    fun cellClick(view: View) {
        val cell = findCell(resources.getResourceName(view.id))
        revealCell(cell, view as Button)
    }

    private fun findCell(resourceName: String): Cell {
        val location = resourceName.substring(resourceName.length - 3)
        val x = location[0].minus('0')
        val y = location[2].minus('0')
        return grid.cell(Pair(x, y))
    }

    private fun revealCell(cell: Cell, button: Button) {
        println("revealing ${cell.locationX}, ${cell.locationY}")
        button.text = cell.toString()
        button.setBackgroundColor(Color.GRAY)
        cell.isRevealed = true

        grid.neighbors(cell.locationX, cell.locationY)
            .filter { it.value == 0 }
            .forEach { revealCell(it, findButtonByCell(it)) }
    }

    private fun findButtonByCell(cell: Cell): Button {
        val id = "button_${cell.locationX}_${cell.locationY}"
        println("id to find = $id")
        println("id found = ${resources.getIdentifier(id, "string", packageName)}")
        return findViewById(resources.getIdentifier(id, "string", packageName))
    }
}
