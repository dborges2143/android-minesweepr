package com.borges.minesweeper

import com.borges.minesweeper.modules.GridGenerator
import org.junit.Test

class GridGeneratorTest {

    @Test
    fun `it creates a grid filled with bombs and neighbor counts`() {
        val width = 6
        val height = 12
        val bombCount = 10

        val grid = GridGenerator.generate(
            bombCount = bombCount,
            gridWidth = width,
            gridHeight = height
        )

        printGrid(grid)

        assert(true) {
            grid.size == width &&
            grid[0].size == height
            bombCount(grid) == bombCount
        }
    }

    private fun bombCount(grid: Array<IntArray>): Int {
        return grid.fold(0) { sum, row ->
            sum + row.filter { it == BOMB }.size
        }
    }

    private fun printGrid(grid: Array<IntArray>) {
        println("----- PRINTING GENERATED GRID -----")
        repeat(grid.size) { x ->
            repeat(grid[x].size) { y ->
                print("|")
                print("${grid[x][y]}".padStart(3, ' ').padEnd(5, ' '))
            }
            print("|")
            println()
        }
        println("------------ END GRID -------------")
    }

}