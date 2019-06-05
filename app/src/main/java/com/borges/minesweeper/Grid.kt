package com.borges.minesweeper

import kotlin.random.Random

class Grid(width: Int, height: Int, bombCount: Int) {

    private lateinit var grid: Array<Array<Cell>>

    init {
        generateGrid(bombCount, width, height)
    }

    val gridWidth: Int get() = grid.size
    val gridHeight: Int get() = grid[0].size
    val bombCount: Int get() =
        grid.fold(0) { sum, row ->
            sum + row.filter { it.isBomb }.size
        }

    fun reset(width: Int,  height: Int, bombCount: Int) {
        generateGrid(bombCount, width, height)
    }

    private fun generateGrid(bombCount: Int, width: Int, height: Int) {
        initializeGrid(width, height)
        fillGridWithBombs(bombCount)
        fillNeighborBombCounts()
    }

    private fun initializeGrid(width: Int, height: Int) {
        grid = Array(width) { x -> Array(height) { y -> Cell(x, y) } }
    }

    private fun fillGridWithBombs(count: Int) {
        var bombsRemaining = count
        while (bombsRemaining > 0) {
            val randomX = Random.nextInt(0, gridWidth - 1)
            val randomY = Random.nextInt(0, gridHeight - 1)

            if (!grid[randomX][randomY].isBomb) {
                grid[randomX][randomY].setBomb()
                bombsRemaining--
            }
        }
    }

    private fun fillNeighborBombCounts() {
        repeat(gridWidth) { x ->
            repeat(gridHeight) { y ->
                if (!grid[x][y].isBomb)
                    grid[x][y].value = neighbors(x, y).filter { it.isBomb }.size
            }
        }
    }

    fun neighbors(x: Int, y: Int): List<Cell> {
        val neighbors = mutableListOf<Cell>()

        (-1..1).forEach { xPrime ->
            (-1..1).forEach { yPrime ->
                if (!isItself(xPrime, yPrime) && neighborExists(x + xPrime, y + yPrime)) {
                    neighbors.add(grid[x + xPrime][y + yPrime])
                }
            }
        }
        return neighbors
    }

    private fun isItself(xPrime: Int, yPrime: Int) = xPrime == 0 && yPrime == 0

    private fun neighborExists(x: Int, y: Int) =
        x in 0 until gridWidth &&
        y in 0 until gridHeight

    private fun print() {
        println("----- PRINTING GENERATED GRID -----")
        repeat(gridHeight) { y ->
            repeat(gridWidth) { x ->
                print("|")
                print("${grid[x][y]}".padStart(3, ' ').padEnd(5, ' '))
            }
            print("|")
            println()
        }
        println("------------ END GRID -------------")
    }

    fun cell(cell: Pair<Int, Int>): Cell {
        return grid[cell.first][cell.second]
    }

}