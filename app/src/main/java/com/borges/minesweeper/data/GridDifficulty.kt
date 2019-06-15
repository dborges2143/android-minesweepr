package com.borges.minesweeper.data

enum class GridDifficulty (val width: Int, val height: Int, val bombCount: Int, val label: String) {
    EASY(6, 12, 10, "Easy"),
    MEDIUM(10, 20, 35, "Medium"),
    HARD(13, 27, 75, "Hard")
}