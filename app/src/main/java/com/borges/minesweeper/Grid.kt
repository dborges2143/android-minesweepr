package com.borges.minesweeper

import kotlin.random.Random

class Grid(val width: Int, val height: Int, val bombCount: Int) {

    private lateinit var grid: Array<Array<Cell>>

    val isInitialized: Boolean
        get() = ::grid.isInitialized && grid.isNotEmpty()

    val isClean: Boolean
        get() = allBombsMarked || allNonBombsRevealed

    private val allBombsMarked: Boolean
        get() = grid.all { row ->
            row
                .filter { it.isBomb }
                .all { it.isMarkedAsBomb }
        }

    private val allNonBombsRevealed: Boolean
        get() = grid.all { row ->
            row
                .filter { !it.isBomb }
                .all { it.isRevealed }
        }

    val markedBombs: Int
        get() = grid.sumBy { row -> row.sumBy { if (it.isMarkedAsBomb) 1 else 0 } }

    fun reset() {
        grid = emptyArray()
    }

    fun generateGrid(safeCoordinate: Pair<Int, Int>) {
        initializeGrid(width, height)
        fillGridWithBombs(bombCount, safeCoordinate)
        fillNeighborBombCounts()
    }

    private fun initializeGrid(width: Int, height: Int) {
        grid = Array(width) { x -> Array(height) { y -> Cell(x, y) } }
    }

    private fun fillGridWithBombs(count: Int, safeCoordinate: Pair<Int, Int>) {
        var bombsRemaining = count
        while (bombsRemaining > 0) {
            val randomX = Random.nextInt(0, width - 1)
            val randomY = Random.nextInt(0, height - 1)

            if (!grid[randomX][randomY].isBomb && !isBombNearSafeCoordinate(Pair(randomX, randomY), safeCoordinate)) {
                grid[randomX][randomY].setBomb()
                bombsRemaining--
            }
        }
    }

    private fun isBombNearSafeCoordinate(bombCoordinate: Pair<Int, Int>, safeCoordinate: Pair<Int, Int>): Boolean {
        (-1..1).forEach { xPrime ->
            (-1..1).forEach { yPrime ->
                if (Pair(safeCoordinate.first + xPrime, safeCoordinate.second + yPrime) == bombCoordinate)
                    return true
            }
        }
        return false
    }

    private fun fillNeighborBombCounts() {
        repeat(width) { x ->
            repeat(height) { y ->
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
        x in 0 until width &&
        y in 0 until height

    private fun print() {
        println("----- PRINTING GENERATED GRID -----")
        repeat(height) { y ->
            repeat(width) { x ->
                print("|")
                print("${grid[x][y]}".padStart(3, ' ').padEnd(5, ' '))
            }
            print("|")
            println()
        }
        println("------------ END GRID -------------")
    }

    fun cell(coordinate: Pair<Int, Int>): Cell {
        return grid[coordinate.first][coordinate.second]
    }

}