package com.borges.minesweeper

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception
import java.util.*
import kotlin.concurrent.scheduleAtFixedRate

class MainActivity : AppCompatActivity() {

    private val gridWidth = 10
    private val gridHeight = 10
    private val bombCount = 20

    lateinit var grid: Grid
    private var totalTime = 0
    private var timer = Timer()

    private fun buttonId(x: Int, y: Int): String = "button_${x}_$y"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonNewGame.isEnabled = false

        grid = Grid(width = gridWidth, height = gridHeight, bombCount = bombCount)
    }

    private fun newGame() {
        performActionOnAllButtons {
            it.text = ""
            it.setBackgroundColor(Color.parseColor("#FF00FFFF"))
            it.setTextColor(Color.WHITE)
        }
        grid.reset()
        resetTimer()
    }

    private fun performActionOnAllButtons(action: (Button) -> Unit) {
        repeat(gridWidth) { x ->
            repeat(gridHeight) { y ->
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
        if (grid.isInitialized) {
            val cell = findCell(resources.getResourceName(view.id))
            revealCell(cell, view as Button)

            if (cell.hasNoNeighboringBombs) clickNeighbors(cell)

            if (cell.isBomb) {
                timer.cancel()
                revealAllCells()
                Toast.makeText(this, "YOU LOSE", Toast.LENGTH_SHORT).show()
            } else if (grid.isCleaned) {
                timer.cancel()
                highlightBombs()
                Toast.makeText(this, "YOU WIN!!", Toast.LENGTH_SHORT).show()
            }

        } else {
            val safeCoordinate = findButtonCoordinate(resources.getResourceName(view.id))
            grid.generateGrid(safeCoordinate)

            initializeButtonGrid()
            buttonNewGame.isEnabled = true
            buttonNewGame.setOnClickListener { newGame() }
            clickCell(view)
            timer.scheduleAtFixedRate(1000, 1000) { incrementTimer() }
        }
    }

    private fun incrementTimer() {
        totalTime += 1
        textTimer.text = "Time: $totalTime"
    }

    private fun resetTimer() {
        try {
            timer.cancel()
        } catch (e: Exception) {}
        timer = Timer()
        totalTime = 0
        textTimer.text = "Time: $totalTime"
    }

    private fun clickNeighbors(cell: Cell) {
        val neighbors = grid.neighbors(cell.locationX, cell.locationY)
        neighbors.filter { !it.isRevealed }.forEach { neighbor ->
            val neighborButton: Button = findViewById(resources.getIdentifier(buttonId(neighbor.locationX, neighbor.locationY), "id", packageName))
            clickCell(neighborButton)
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
        val coordinate = findButtonCoordinate(resourceName)
        return grid.cell(coordinate)
    }

    private fun findButtonCoordinate(resourceName: String): Pair<Int, Int> {
        val location = resourceName.substring(resourceName.length - 3)
        val x = location[0].minus('0')
        val y = location[2].minus('0')
        return Pair(x, y)
    }

    private fun revealCell(cell: Cell, button: Button) {
        button.text = cell.toString()
        button.setBackgroundColor(Color.GRAY)
        if (cell.isBomb) button.setTextColor(Color.RED)
        cell.isRevealed = true
    }

}
