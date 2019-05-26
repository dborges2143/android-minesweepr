package com.borges.minesweeper.modules

import kotlin.random.Random

class GridGenerator {
    private val BOMB = -1

    fun generate(bombCount: Int, gridWidth: Int, gridHeight: Int): Array<IntArray> {

        val grid = Array(gridHeight) { IntArray(gridWidth) }

        repeat(gridWidth) { x ->
            grid[x] = intArrayOf()

            repeat(gridHeight) { y ->
                grid[x][y] = 0
            }
        }

        var bombsRemaining = bombCount
        while (bombsRemaining > 0) {
            val randomX = Random.nextInt(0, gridWidth)
            val randomY = Random.nextInt(0, gridHeight)

            if (grid[randomX][randomY] != BOMB) {
                grid[randomX][randomY] = BOMB
                bombsRemaining--
            }
        }

        return fillNeighborBombCounts(grid)
    }

    private fun fillNeighborBombCounts(grid: Array<IntArray>): Array<IntArray> {

        repeat(grid.size) { x ->
            repeat(grid[x].size) { y ->

                val topLeft = grid[x-1][y-1]
                val topCenter = grid[x][y+1]
                val topRight = grid[x+1][y+1]

                val centerLeft = grid[x-1][y]
                val centerRight = grid[x+1][y]

                val bottomLeft = grid[x-1][y-1]
                val bottomCenter = grid[x][y-1]
                val bottomRight = grid[x+1][y+1]

                val neighbors = intArrayOf(topLeft, topCenter, topRight, centerLeft, centerRight, bottomLeft, bottomCenter, bottomRight)

                grid[x][y] = neighbors.filter { it == BOMB }.size
            }
        }

        return grid
    }

    private fun neighbors(grid: Array<IntArray>, position: Int): IntArray {
        val neighbors = intArrayOf()

        repeat(3) { x ->
            repeat(3) { y ->
                if (neighborExists(grid, x, y))
                    neighbors[neighbors.size] = grid[x][y]
            }
        }

        return neighbors
    }

    private fun neighborExists(grid: Array<IntArray>, x: Int, y: Int): Boolean {
        return x >= 0 && x < grid.size &&
                y >= 0 && x < grid[x].size
    }

}