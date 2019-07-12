package com.borges.minesweeper.data

enum class GridDifficulty (val width: Int, val height: Int, val bombCount: Int, val label: String) {
    EASY(6, 12, 10, "Easy"),
    MEDIUM(8, 16, 20, "Medium"),
    HARD(10, 22, 40, "Hard")
}