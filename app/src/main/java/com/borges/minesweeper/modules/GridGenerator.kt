package com.borges.minesweeper.modules

import com.borges.minesweeper.BOMB
import kotlin.random.Random

object GridGenerator {

    fun generate(bombCount: Int, gridWidth: Int, gridHeight: Int): Array<IntArray> {
        var grid = initializeGrid(gridWidth, gridHeight)
        grid = fillGridWithBombs(grid, bombCount)

        return fillNeighborBombCounts(grid)
    }

    private fun initializeGrid(width: Int, height: Int): Array<IntArray> {
        val grid = Array(width) { IntArray(height) }

        repeat(width) { x ->
            grid[x] = IntArray(height)

            repeat(height) { y ->
                grid[x][y] = 0
            }
        }

        return grid
    }

    private fun fillGridWithBombs(grid: Array<IntArray>, count: Int): Array<IntArray> {
        var bombsRemaining = count
        val gridWidth = grid.size
        val gridHeight = grid[0].size
        println("gridWidth = $gridWidth, gridHeight = $gridHeight")
        while (bombsRemaining > 0) {
            val randomX = Random.nextInt(0, gridWidth-1)
            val randomY = Random.nextInt(0, gridHeight-1)

            println("randomX = $randomX, randomY = $randomY")

            if (grid[randomX][randomY] != BOMB) {
                grid[randomX][randomY] = BOMB
                bombsRemaining--
            }
        }

        return grid
    }

    private fun fillNeighborBombCounts(grid: Array<IntArray>): Array<IntArray> {
        repeat(grid.size) { x ->
            repeat(grid[x].size) { y ->
                if (grid[x][y] != BOMB) {
                    grid[x][y] = neighbors(grid, x, y)
                        .apply {
                            println("Neighbors for $x, $y (Total bombs: ${filter { it == BOMB }.size})")
                            repeat(size) { print("${get(it)} ") }
                            println()
                        }
                        .filter { it == BOMB }.size
                }
            }
        }

        return grid
    }

    private fun neighbors(grid: Array<IntArray>, x: Int, y: Int): IntArray {
//        println("GETTING NEIGHBORS FOR x = [$x], y = [$y]")
        val neighbors = mutableListOf<Int>()

        (-1..1).forEach { xPrime ->
//            println("xPrime = $xPrime")
            (-1..1).forEach { yPrime ->
//                println("yPrime = $yPrime")
                if (!isItself(xPrime, yPrime) && neighborExists(grid, x + xPrime, y + yPrime)) {
//                    println("Neighbor at ${x + xPrime}, ${y + yPrime}")
                    neighbors.add(grid[x + xPrime][y + yPrime])
                }
            }
        }
//        println("-------------------------------------")
        return neighbors.toIntArray()
    }

    private fun isItself(xPrime: Int, yPrime: Int) = xPrime == 0 && yPrime == 0

    private fun neighborExists(grid: Array<IntArray>, x: Int, y: Int): Boolean {
//        println("CHECKING IF NEIGHBOR EXISTS FOR x = [$x], y = [$y]")
//        println(
//            "$x >= 0 && $x < grid.size &&\n$y >= 0 && x < grid[x].size = ${x >= 0 && x < grid.size &&
//                    y >= 0 && x < grid[x].size}"
//        )
        return x >= 0 && x < grid.size &&
            y >= 0 && y < grid[x].size

    }

}