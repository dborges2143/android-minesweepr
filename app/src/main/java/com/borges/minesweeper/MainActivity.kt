package com.borges.minesweeper

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val gridWidth = 10
    private val gridHeight = 10
    private val bombCount = 10

    private val grid = Grid(width = gridWidth, height = gridHeight, bombCount = bombCount)

    private fun buttonId(x: Int, y: Int): String = "button_${x}_$y"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeButtonGrid()

        buttonNewGame.setOnClickListener { newGame() }
    }

    private fun newGame() {
        initializeButtonGrid()
        performActionOnAllButtons {
            it.text = ""
            it.setBackgroundColor(Color.parseColor("#FF00FFFF"))
        }
        grid.reset(width = gridWidth, height = gridHeight, bombCount = bombCount)
    }

    private fun performActionOnAllButtons(action: (Button) -> Unit) {
        repeat(grid.gridWidth) { x ->
            repeat(grid.gridHeight) { y ->
                val button: Button = findViewById(resources.getIdentifier(buttonId(x, y), "id", packageName))
                action(button)
            }
        }
    }

    private fun initializeButtonGrid() {
        performActionOnAllButtons {

            it.setOnLongClickListener { view ->
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
    }

    fun clickCell(view: View) {
        val cell = findCell(resources.getResourceName(view.id))
        revealCell(cell, view as Button)
        if (cell.isBomb) {
            revealAllCells()
            Toast.makeText(this, "YOU LOSE", Toast.LENGTH_SHORT).show()
        } else if (grid.isCleaned) {
            highlightBombs()
            Toast.makeText(this, "YOU WIN!!", Toast.LENGTH_SHORT).show()

        }
    }

    private fun highlightBombs() {
        performActionOnAllButtons {
            if (it.text == "X")
                it.setBackgroundColor(Color.GREEN)
        }
    }

    private fun revealAllCells() {
        performActionOnAllButtons {
            val cell = findCell(resources.getResourceName(it.id))
            revealCell(cell, it)
        }
    }

    private fun findCell(resourceName: String): Cell {
        val location = resourceName.substring(resourceName.length - 3)
        val x = location[0].minus('0')
        val y = location[2].minus('0')
        return grid.cell(Pair(x, y))
    }

    private fun revealCell(cell: Cell, button: Button) {
        button.text = cell.toString()
        button.setBackgroundColor(Color.GRAY)
        cell.isRevealed = true
    }

}
