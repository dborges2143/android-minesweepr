package com.borges.minesweeper.data

import com.borges.minesweeper.R

class Cell(val locationX: Int, val locationY: Int) {

    var value = 0

    val textColor: Int
        get() = when (value) {
            BOMB -> R.color.colorMineCell
            1 -> R.color.colorOneNeighbor
            2 -> R.color.colorTwoNeighbor
            3 -> R.color.colorThreeNeighbor
            4 -> R.color.colorFourNeighbor
            5 -> R.color.colorFiveNeighbor
            6 -> R.color.colorSixNeighbor
            7 -> R.color.colorSevenNeighbor
            8 -> R.color.colorEightNeighbor
            else -> R.color.colorConcealedCell
        }

    val backgroundColor: Int
        get() =
            if (isBomb)
                R.color.colorMineCellBackground
            else
                R.color.colorRevealedCellBackground

    var isRevealed = false
    var isMarkedAsBomb = false
        private set

    val isBomb: Boolean
        get() = value == BOMB

    val hasNoNeighboringBombs: Boolean
        get() = value == 0

    fun setBomb() {
        value = BOMB
    }

    fun unMarkBomb() {
        isMarkedAsBomb = false
    }

    fun markAsBomb() {
        isMarkedAsBomb = true
    }

    override fun toString(): String {
        return when (value) {
            BOMB -> "X"
            0 -> ""
            else -> value.toString()
        }
    }
}