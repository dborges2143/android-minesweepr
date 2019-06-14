package com.borges.minesweeper

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.concurrent.scheduleAtFixedRate

class MainActivity : AppCompatActivity() {

    private val gridWidth = 10
    private val gridHeight = 20
    private val bombCount = 35

    lateinit var grid: Grid
    private var timer = Timer()
    private var totalTime = 0
    private var bombsLeft = bombCount

    private val mineColor by lazy { resources.getColor(R.color.colorMineCell, null) }
    private val concealedColor by lazy { resources.getColor(R.color.colorConcealedCell, null) }

    private fun buttonId(x: Int, y: Int): String = "button_${x}_$y"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonNewGame.isEnabled = false

        grid = Grid(width = gridWidth, height = gridHeight, bombCount = bombCount)
        updateBombsLeftLabel()
    }

    private fun newGame() {
        performActionOnAllButtons {
            it.text = ""
            it.setBackgroundColor(concealedColor)
            it.setTextColor(Color.WHITE)
            it.isEnabled = true
        }
        grid.reset()
        resetTimer()
        bombsLeft = bombCount
        updateBombsLeftLabel()
    }

    private fun updateBombsLeftLabel() {
        if (grid.isInitialized) bombsLeft = bombCount - grid.markedBombs
        textBombsLeft.text = "Bombs: $bombsLeft"
    }

    private fun performActionOnAllButtons(action: (Button) -> Unit) {
        repeat(gridWidth) { x ->
            repeat(gridHeight) { y ->
                val button: Button = findViewById(resources.getIdentifier(buttonId(x, y), "id", packageName))
                action(button)
            }
        }
    }

    private fun gameWon() {
        timer.cancel()
        highlightBombs()
        Toast.makeText(this, "YOU WIN!!", Toast.LENGTH_SHORT).show()
        disableGrid()
    }

    private fun gameLost() {
        timer.cancel()
        revealAllCells()
        Toast.makeText(this, "YOU LOSE", Toast.LENGTH_SHORT).show()
        disableGrid()
    }

    private fun disableGrid() {
        performActionOnAllButtons { it.isEnabled = false }
    }

    private fun initializeButtonGrid() {
        performActionOnAllButtons {

            it.setOnLongClickListener { view ->

                if (grid.isInitialized) {
                    val background = view.background as ColorDrawable
                    val cell = findCell(resources.getResourceName(view.id))

                    if (!cell.isRevealed) {
                        when (background.color) {
                            mineColor -> {
                                cell.unMarkBomb()
                                view.setBackgroundColor(concealedColor)
                            }
                            concealedColor -> {
                                cell.markAsBomb()
                                view.setBackgroundColor(mineColor)
                                if (grid.isClean) gameWon()
                            }
                        }
                        updateBombsLeftLabel()
                    }

                } else clickCell(it)

                true
            }
        }
    }

    fun clickCell(view: View) {
        if (grid.isInitialized) {
            val cell = findCell(resources.getResourceName(view.id))
            revealCell(cell, view as Button)

            when {
                cell.isBomb -> gameLost()
                cell.hasNoNeighboringBombs -> clickNeighbors(cell)
                grid.isClean -> gameWon()
            }

        } else {
            val safeCoordinate = findButtonCoordinate(resources.getResourceName(view.id))
            startNewGame(view, safeCoordinate)
        }
    }

    private fun startNewGame(view: View, safeCoordinate: Pair<Int, Int>) {
        grid.generateGrid(safeCoordinate)
        initializeButtonGrid()
        buttonNewGame.isEnabled = true
        buttonNewGame.setOnClickListener { newGame() }
        clickCell(view)
        timer.scheduleAtFixedRate(1000, 1000) { incrementTimer() }
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
            val cell = findCell(resources.getResourceName(it.id))
            if (cell.isBomb)
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
        val location = resourceName.substringAfter('_')
        val x = location[0].minus('0')
        val y = location.substring(2).toInt()
        return x to y
    }

    private fun revealCell(cell: Cell, button: Button) {
        button.text = cell.toString()
        button.setBackgroundColor(Color.GRAY)
        if (cell.isBomb) button.setTextColor(Color.RED)
        cell.isRevealed = true
    }

}
