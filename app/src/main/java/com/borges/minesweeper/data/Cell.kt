package com.borges.minesweeper.data

class Cell(x: Int, y: Int) {

    val locationX: Int = x
    val locationY: Int = y

    var value = 0

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