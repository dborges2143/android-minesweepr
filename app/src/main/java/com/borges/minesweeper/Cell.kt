package com.borges.minesweeper

class Cell(x: Int, y: Int) {

    val locationX: Int = x
    val locationY: Int = y

    var value = 0

    var isRevealed = false

    val isBomb: Boolean
        get() = value == BOMB

    fun setBomb() {
        value = BOMB
    }

    override fun toString(): String {
        return when (value) {
            BOMB -> "X"
            0 -> ""
            else -> value.toString()
        }
    }
}